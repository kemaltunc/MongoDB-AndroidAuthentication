package com.example.kemal.mongodb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button login_btn;
    private TextView tv_signup;

    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

        mEmail = findViewById(R.id.ed_email);
        mPassword = findViewById(R.id.ed_password);
        login_btn = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                Login(email, password);
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }

    private void Login(String email, String password) {

        progressDialog.setTitle("Lütfen bekleyin...");
        progressDialog.setMessage("Hesabınız oluşturuluyor...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        Call<ResponseBody> call = Client.getmInstance().getApi()
                .loginUser(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        String id = jsonObject.getString("id");
                        sessionManager.createSession(id);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        progressDialog.dismiss();
    }
}
