package com.velen.whoplaysfirst.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefsManager {

    private static final String STORED_WHEEL_NAMES = "storedWheelNames";
    private static SharedPrefsManager instance;

    private SharedPrefsManager(){}

    public static SharedPrefsManager getInstance() {
        if(instance == null) {
            instance = new SharedPrefsManager();
        }
        return instance;
    }

    private SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    private SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean storeWheelOptions(Context context, String name, List<String> options) {
        List<String> storedNames = retrieveStoredWheelNames(context);
        if(!storedNames.contains(name)) {
            Set<String> set = new HashSet<>();
            set.addAll(options);
            getEditor(context).putStringSet(name, set).commit();
            storeWheelName(context, name);
            return true;
        } else {
            return false;
        }
    }

    public void removeWheelOptions(Context context, String name) {
        getEditor(context).remove(name).commit();
        List<String> storedNames = retrieveStoredWheelNames(context);
        storedNames.remove(name);
        storeWheelNames(context, storedNames);
    }

    public List<String> retrieveStoredWheelOptions(Context context, String name) {
        List<String> options = new ArrayList<>();
        Set<String> set = getPrefs(context).getStringSet(name, null);
        if(set == null) {
            set = new HashSet<>();
        }
        options.addAll(set);
        return options;
    }

    public List<String> retrieveStoredWheelNames(Context context) {
        List<String> names = new ArrayList<>();
        Set<String> set = getPrefs(context).getStringSet(STORED_WHEEL_NAMES, null);
        if(set == null) {
            set = new HashSet<>();
        }
        names.addAll(set);
        return names;
    }

    public void storeWheelName(Context context, String name) {
        List<String> names = retrieveStoredWheelNames(context);
        names.add(name);
        storeWheelNames(context, names);
    }

    private void storeWheelNames(Context context, List<String> names) {
        Set<String> set = new HashSet<>();
        set.addAll(names);
        getEditor(context).putStringSet(STORED_WHEEL_NAMES, set).commit();
    }

}
