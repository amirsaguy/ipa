package com.ipa.common.accounts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IpaAccountInfo implements Serializable{

	private static final long serialVersionUID = 1768180569488524463L;
	
	protected List<UISettingsGroup> uisettings;
	protected  Map<String, Object> values;
	
	public IpaAccountInfo() {
	}

	public void merge(IpaAccountInfo info){
		if (info != null){
			if (info.getUisettings() != null){
				if (uisettings == null){
					uisettings = new ArrayList<UISettingsGroup>();
				}
				uisettings.addAll(info.getUisettings());
			}			
		}
	}
	
	protected Set<String> getSettingsKeys(){
		HashSet<String> set = new HashSet<String>();
		if (uisettings != null){
			for (int i = 0; i < uisettings.size(); i++) {
				UISettingsGroup group = (UISettingsGroup) uisettings.get(i);
				group.addKeysToSet(set);
			}			
		}
		return set;
	}
	
	public void updateValues(IpaAccountInfo info){
		if (info.getValues() != null){
			if (values == null){
				values = new HashMap<String, Object>();
			}
			Set<String> set = getSettingsKeys();
			if (set != null){
				 Map<String, Object> map = info.getValues();
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next();
					if (set.contains(key)){
						values.put(key, map.get(key));
					}					
				}
			}
		}
	}
	
	public void addValue(String key,String value){
		if (key != null && key.length() > 0 && value != null){
			if (values == null){
				values = new HashMap<String, Object>();
			}
			values.put(key, value);
		}
	}


	public List<UISettingsGroup> getUisettings() {
		return uisettings;
	}


	public void setUisettings(List<UISettingsGroup> uisettings) {
		this.uisettings = uisettings;
	}


	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	public Object getValue(String key){
		if (values != null){
			return values.get(key);
		}
		return null;
	}


}
