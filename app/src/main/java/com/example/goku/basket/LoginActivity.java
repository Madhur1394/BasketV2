package com.example.goku.basket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by goku on 1/5/17.
 */

public class LoginActivity extends AppCompatActivity {

    public Button btnLogin, btnGoogleLogin, btnFacebookLogin, btnLinkToRegister;
    public TextInputLayout emailWrapper, passwrdWrapper;
    public EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private String email, password;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGoogleLogin = (Button) findViewById(R.id.googleSignInBtn);
        btnFacebookLogin = (Button) findViewById(R.id.facebookLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        progressBar.setVisibility(View.INVISIBLE);

        //Get Firebase Instance
        mAuth =FirebaseAuth.getInstance();

        emailWrapper = (TextInputLayout) findViewById(R.id.input_layout_email);
        passwrdWrapper = (TextInputLayout) findViewById(R.id.input_layout_password);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);

        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));

        try {
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(LoginActivity.this, "You Got An Error", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        catch (Exception e){
            Toast.makeText(this, "Hiii", Toast.LENGTH_LONG).show();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(v);
                //For Login

                if (!validateEmail()) {}
                else if (!validatePassword()) {}
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                Toast.makeText(getApplicationContext(),"Login is Successful",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        }
                    });

                   }

            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Google Sign In Login
                signIn();
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

    //For Hiding Keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}