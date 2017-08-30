package com.ipa.common.accounts;

import java.io.Serializable;
import java.util.Set;

public class UISettingsEntity implements Serializable{
		
	private static final long serialVersionUID = 6744692856815132928L;

	protected String name;
	protected String displayName;
	protected String type;
	protected String value;

	public UISettingsEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	protected void addKeysToSet(Set<String> set){
		set.add(getName());
	}

}
