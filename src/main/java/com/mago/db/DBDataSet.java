package com.mago.db;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DBDataSet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> datas = Collections.synchronizedMap(new HashMap<String, Object>());
	
	public void addInteger(String key, int value) {
		datas.put(key, new Integer(value));
	}
	
	public void addLong(String key, long value) {
		datas.put(key, new Long(value));
	}
	
	public void addFloat(String key, float value) {
		datas.put(key, new Float(value));
	}
	
	public void addDouble(String key, double value) {
		datas.put(key, new Double(value));
	}
	
	public void addString(String key, String value) throws DBException {
		if (value != null) {
			datas.put(key, value);
		} else {
			throw new DBException("Value cannot be NULL.");
		}
	}
	
	public Map<String, Object> getDataSet() {
		return datas;
	}
	
	public void addTinyInt(String key, int value) throws DBException {
		if (value >= -128 && value <= 127) {
			datas.put(key, new Integer(value));
		} else {
			throw new DBException("Value is not a tinyint.");
		}
	}

}
