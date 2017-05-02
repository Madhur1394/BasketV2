package com.example.goku.basket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by goku on 1/5/17.
 */

public class LoginActivity extends AppCompatActivity {

    public Button btnLogin, btnGoogleLogin, btnFacebookLogin, btnLinkToRegister;
    public TextInputLayout emailWrapper, passwrdWrapper;
    public EditText editTextEmail, editTextPassword;

    private String email, password;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGoogleLogin = (Button) findViewById(R.id.googleSignInBtn);
        btnFacebookLogin = (Button) findViewById(R.id.facebookLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnSignUp);

        emailWrapper = (TextInputLayout) findViewById(R.id.input_layout_email);
        passwrdWrapper = (TextInputLayout) findViewById(R.id.input_layout_password);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);

        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Login

                if (!validateEmail()) {}
                else if (!validatePassword()) {}
                else {
                    Toast.makeText(getApplicationContext(),"Login Succesfully",Toast.LENGTH_LONG).show();
                   }

            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Google Sign In Login
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Facebook Login
            }
        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Link to Register User
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });


    }

    private boolean validatePassword() {
        password = editTextPassword.getText().toString().trim();
        if(password.length() < 6) {
            passwrdWrapper.setError("Not a valid Password!");
            passwrdWrapper.requestFocus();
            return false;
        }
        else{
            passwrdWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        email = editTextEmail.getText().toString().trim();
        matcher = pattern.matcher(email);
        if(!matcher.matches()){
            emailWrapper.setError("Not a valid Email!");
            emailWrapper.requestFocus();
            return false;
        }
        else{
            emailWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;

            }
        }
    }
}
