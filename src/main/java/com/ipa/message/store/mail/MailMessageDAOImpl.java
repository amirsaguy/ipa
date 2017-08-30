package com.ipa.message.store.mail;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ipa.common.message.mail.MailMessage;
import com.mongodb.WriteResult;

public class MailMessageDAOImpl implements MailMessageDAO{
	
	private MongoOperations mongoOps;
	private static final String MESSAGES_COLLECTION = "Messages";
	
	public MailMessageDAOImpl(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public void create(MailMessage mailMessage) {
		this.mongoOps.insert(mailMessage, MESSAGES_COLLECTION);
	}

	@Override
	public MailMessage readById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return this.mongoOps.findOne(query, MailMessage.class, MESSAGES_COLLECTION);
	}

	@Override
	public void update(MailMessage mailMessage) {
		this.mongoOps.save(mailMessage, MESSAGES_COLLECTION);
	}

	@Override
	public int deleteById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		WriteResult result = this.mongoOps.remove(query, MailMessage.class, MESSAGES_COLLECTION);
		return result.getN();
	}

}
