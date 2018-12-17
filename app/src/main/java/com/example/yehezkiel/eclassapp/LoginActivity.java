package com.example.yehezkiel.eclassapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends Activity {


    private Button loginBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Dialog AlertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);



        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        AlertDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).build();

//        progressBar = findViewById(R.id.progressBar);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Email tidak boleh kosong !!")
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Password tidak boleh kosong !!")
                            .show();


                    return;
                }


//                progressBar.setVisibility(View.VISIBLE);

                AlertDialog = new SpotsDialog.Builder()
                        .setContext(LoginActivity.this)
                        .setMessage("Logging in...")
                        .setCancelable(true).build();

                AlertDialog.show();



                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AlertDialog.dismiss();
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Password minimal 6 karakter !! ")
                                        .show();
                            } else {
                                passwordInput.setText("");
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Autentikasi gagal, periksa kembali email dan password Anda !!")
                                        .show();
                            }
                        } else {



                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}

