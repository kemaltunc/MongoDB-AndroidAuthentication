package com.example.kemal.mongodb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String ID = "ID";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sharedPref",context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void createSession(String id) {
        editor.putString(ID, id);
        editor.commit();
    }

    public String getId() {
        return sharedPreferences.getString(ID, "");
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
