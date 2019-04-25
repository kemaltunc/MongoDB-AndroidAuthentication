package com.example.kemal.mongodb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.se.omapi.SEService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kemal.mongodb.Retrofit.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPassword;
    private Button btn_register;

    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

        mName = findViewById(R.id.ed_name);
        mEmail = findViewById(R.id.ed_email);
        mPassword = findViewById(R.id.ed_password);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                Register(name, email, password);
            }
        });

    }

    private void Register(String name, String email, String password) {

        progressDialog.setTitle("Lütfen bekleyin...");
        progressDialog.setMessage("Hesabınız oluşturuluyor...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);

        Call<ResponseBody> call = Client.getmInstance().getApi()
                .createUser(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        String id = jsonObject.getString("id");
                        sessionManager.createSession(id);

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        progressDialog.dismiss();

    }
}
