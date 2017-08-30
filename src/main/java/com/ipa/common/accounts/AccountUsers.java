package com.ipa.common.accounts;

import java.util.ArrayList;
import java.util.List;

public class AccountUsers {
	
	protected List<AccountUser> users;

	public AccountUsers() {
	}

	public List<AccountUser> getUsers() {
		return users;
	}

	public void setUsers(List<AccountUser> users) {
		this.users = users;
	}
	
	public void addUser(AccountUser user){
		if (users == null){
			users = new ArrayList<AccountUser>();
		}else {
			for (int i = 0; i < users.size(); i++) {
				AccountUser accountUser = (AccountUser) users.get(i);
				if (accountUser.getUsername().equals(user.getUsername())){
					users.set(i, user);
					return;
				}
			}
		}
		users.add(user);
	}

}
