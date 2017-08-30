package com.ipa.common.accounts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class UISettingsGroup implements Serializable{
	
	private static final long serialVersionUID = -3112618997384085509L;
	
	protected String name;
	protected String displayName;
	protected String type;
	protected String url;
	
	protected List<UISettingsEntity> data; 
	

	public UISettingsGroup() {
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


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public List<UISettingsEntity> getData() {
		return data;
	}


	public void setData(List<UISettingsEntity> data) {
		this.data = data;
	}
	
	public void mergeData(List<UISettingsEntity> data){
		if (this.data == null){
			this.data= new ArrayList<UISettingsEntity>();
		}
		HashSet<String> keys = new HashSet<String>();
		for (int i = 0; i < this.data.size(); i++) {
			UISettingsEntity uiSettingsEntity = (UISettingsEntity) this.data.get(i);
			keys.add(uiSettingsEntity.getName());
			
		}
		for (int i = 0; i < data.size(); i++) {
			UISettingsEntity uiSettingsEntity = (UISettingsEntity) data.get(i);
			if (!keys.contains(uiSettingsEntity.getName())){
				this.data.add(uiSettingsEntity);
			}
			
		}
	}
	
	@JsonIgnore
	public void merge(UISettingsGroup group){
		if (group != null){
			if (group.data != null){
				mergeData(group.data);
			}
		}
	}
	
	protected void addKeysToSet(Set<String> set){
		if (data != null){
			for (int i = 0; i < data.size(); i++) {
				UISettingsEntity entity = (UISettingsEntity) data.get(i);
				entity.addKeysToSet(set);
			}
		}
	}

}
