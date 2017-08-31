package com.ipa;

import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipa.adapter.SiteAdapter;
import com.ipa.adapter.SiteAdapterManager;
import com.ipa.common.accounts.AccountUser;
import com.ipa.common.accounts.IpaAccount;
import com.ipa.common.message.mail.MailMessage;
import com.ipa.common.messagingaccounts.MessagingAccount;
import com.ipa.common.messagingaccounts.mail.MailAccount;
import com.ipa.message.sources.MessagesFetchResponse;
import com.ipa.message.sources.mail.MailFetchRequest;
import com.ipa.message.sources.mail.MailFetcher;
import com.ipa.message.store.mail.MailRepository;

public class IpaTest {
	
	public static void main(String[] args) throws Exception{
		IpaTest ipaTest = new IpaTest();
		ipaTest.testRunSite();
		//ipaTest.cleanSubject();
		//ipaTest.testJson(args);
		//ipaTest.getAccountJson();
		System.out.println("done");
		System.exit(0);
	}
	
	public void testRunSite(){
		String initProp = System.getProperty("ipa.site.config.path");
		if (initProp == null || initProp.length() == 0){
			System.setProperty("ipa.site.config.path", "demo/siteInit.prop");
		}
		SiteAdapterManager siteAdapterManager = SiteAdapterManager.getInstance();
		SiteAdapter siteAdapter = siteAdapterManager.getSiteAdapter();
		AccountUser user = siteAdapter.getUser("amirsaguy");
		if (user != null){
			ArrayList<MessagingAccount> accounts = user.getMessagingAccounts();
			if (accounts != null){
				MessagingAccount messagingAccount = user.getMessagingAccountByName("amirsaguy@gmail.com");
				//MessagingAccount messagingAccount = user.getMessagingAccountByName("amirsaguy@yahoo.com");
				if (messagingAccount != null){
					MailFetchRequest fetchRequest = new MailFetchRequest();
					fetchRequest.setAccount(messagingAccount);
					fetchRequest.setMaxNumberOfMessages(20);
					fetchRequest.setSearchTerm(getSearchTerm());
					fetchRequest.addMessageUidRange(MailFetcher.INBOX_FOLDER, -1, -1);
					fetchRequest.addMessageUidRange(MailFetcher.SENT_ITEMS_INBOX, 0, 0);
					fetchRequest.setStripHtml(true);
					//remark
					
					MailFetcher mailFetcher = new MailFetcher(fetchRequest);
					mailFetcher.init();
					MessagesFetchResponse response = mailFetcher.fetchData();
					if (response != null && response.size() > 0) {
						MailRepository mailRepository = MailRepository.getInstance();
						for (com.ipa.common.message.Message message : response.getMessages()) {
							mailRepository.storeMail(message);
						}
					}
				}
			}
			
		}

	}
	
	protected SearchTerm getSearchTerm(){
		SearchTerm searchTerm = new SearchTerm() {

			private static final long serialVersionUID = -1138619417902867741L;

			@Override
			public boolean match(Message msg) {
				// TODO Auto-generated method stub
				try {
					String subject = msg.getSubject();
					if (subject != null){
						return subject.toLowerCase().indexOf("mail thread") != -1;
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		};
		return searchTerm;
	}
	
	public void getAccountJson() throws Exception{
		IpaAccount account = new IpaAccount();
		account.setName("test");
		account.setAccountType("private");
		account.setDescription("Test account");
		account.setDisplayName("Test account");
		AccountUser user = new AccountUser();
		user.setAccount("test");
		user.setUsername("amirsaguy");
		user.setDisplayName("Amir Saguy");
		user.setEmail("amirsaguy@gmail.com");
		user.setPassword("d0ramz0e");
		account.addUser(user);
		MailAccount messageAcc = new MailAccount();
		messageAcc.setId("1");
		messageAcc.setName("my gmail account");
		messageAcc.setDescription("This is my gmail account");
		messageAcc.setProtocol("imaps");
		messageAcc.setHost("imap.gmail.com");
		messageAcc.setPort("993");
		messageAcc.setUseSSL(true);
		messageAcc.setUser("amirsaguy");
		messageAcc.setPassword("ami(1411");
		user.addMessagingAccount(messageAcc);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.enableDefaultTyping();
		String json = mapper.writeValueAsString(account);
		System.out.println("account (orig.) = " + json);
		IpaAccount anotherAccount = mapper.readValue(json, IpaAccount.class);
		System.out.println("another account = " + mapper.writeValueAsString(anotherAccount));
		
		ArrayList<AccountUser> users = account.getUsers();
		json = mapper.writeValueAsString(users);
		System.out.println("users (orig.) = " + json);
		@SuppressWarnings("unchecked")
		ArrayList<AccountUser> users2 = mapper.readValue(json, ArrayList.class);
		System.out.println("another account = " + mapper.writeValueAsString(users2));
				
	}
	
	public void testJson(String[] args) throws Exception
	{
		BaseClass base = new BaseClass();
		A a = new A();
		B b = new B();
		C c = new C();

		ObjectMapper mapper = new ObjectMapper();

		String baseJson = mapper.writeValueAsString(base);
		System.out.println(baseJson); // {"type":"BaseClass","baseName":"base name"}
		String aJson = mapper.writeValueAsString(a);
		System.out.println(aJson); // {"type":"a","baseName":"base name","aName":"a name"}
		String bJson = mapper.writeValueAsString(b);
		System.out.println(bJson); // {"type":"b","baseName":"base name","bName":"b name"}
		String cJson = mapper.writeValueAsString(c);
		System.out.println(cJson); // {"type":"c","baseName":"base name","cName":"c name"}

		BaseClass baseCopy = mapper.readValue(baseJson, BaseClass.class);
		System.out.println(baseCopy); // baseName: base name
		BaseClass aCopy = mapper.readValue(aJson, BaseClass.class);
		System.out.println(aCopy); // baseName: base name, aName: a name
		BaseClass bCopy = mapper.readValue(bJson, BaseClass.class);
		System.out.println(bCopy); // baseName: base name, bName: b name
		BaseClass cCopy = mapper.readValue(cJson, BaseClass.class);
		System.out.println(cCopy); // baseName: base name, cName: c name
	}
	
	public void cleanSubject() {
		MailMessage mailMessage = new MailMessage();
		mailMessage.setSubject("RE: Thread Test");
		System.out.println("Clean mail subject: " + mailMessage.getCleanSubject());
	}
}

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type")  
@JsonSubTypes({  
    @Type(value = A.class, name = "a"),  
    @Type(value = B.class, name = "b"),  
    @Type(value = C.class, name = "c") }) 
class BaseClass {
	public String baseName = "base name";
	@Override public String toString() {return "baseName: " + baseName;}
}

class A extends BaseClass
{
	public String aName = "a name";
	@Override public String toString() {return super.toString() + ", aName: " + aName;}
}

class B extends BaseClass
{
	public String bName = "b name";
	@Override public String toString() {return super.toString() + ", bName: " + bName;}
}

class C extends BaseClass
{
	public String cName = "c name";
	@Override public String toString() {return super.toString() + ", cName: " + cName;}
}
	
