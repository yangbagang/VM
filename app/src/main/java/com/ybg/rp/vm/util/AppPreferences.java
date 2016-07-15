/**
 * 
 */
package com.ybg.rp.vm.util;

import android.content.SharedPreferences;

/**
 * @author 杨拔纲
 * 
 */
public class AppPreferences {

	private static AppPreferences preference = null;

	private SharedPreferences sharedPreferences = null;

	SharedPreferences.Editor editor = null;

	private boolean init = false;

	private AppPreferences() {

	}

	public static AppPreferences getInstance() {
		if (preference == null) {
			preference = new AppPreferences();
		}
		return preference;
	}

	public void init(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
		editor = this.sharedPreferences.edit();
		init = true;
	}

	public boolean hasInit() {
		return init;
	}

	public String getString(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}

	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}

	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public long getLong(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}

	public void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public float getFloat(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}

	public void setFloat(String key, float value) {
		editor.putFloat(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}

	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public String getVMCode() {
		return getString(VM_CODE, "");
	}

	public void setVmCode(String vmCode) {
		setString(VM_CODE, vmCode);
	}

	public boolean isFirstUse() {
		return "".equals(getVMCode());
	}

	public String getVMId() {
		return getString(VM_ID, "0");
	}

	public void setVmId(String vmId) {
		setString(VM_ID, vmId);
	}

	private static String VM_CODE = "vm_code";
	private static String VM_ID = "vm_id";
}
