package com.example.goku.basket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by goku on 1/5/17.
 */

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Button btnRegister,btnLinkToLogin;
    private FloatingActionButton fabChooseImage;
    private TextInputLayout nameWrapper,emailWrapper,phoneWrapper,passwordWrapper;
    private EditText editTextName,editTextEmail,editTextPhone,editTextPassword;
    private ImageView imageView;

    private String name,email,phoneNo,password;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private Uri filePath;
    private CardView cardViewProfilePick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnSignIn);

        fabChooseImage = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        imageView = (ImageView) findViewById(R.id.imageView3);
        cardViewProfilePick = (CardView) findViewById(R.id.cardViewProfilePic);

        nameWrapper = (TextInputLayout) findViewById(R.id.input_layout_name);
        emailWrapper = (TextInputLayout) findViewById(R.id.input_layout_email_1);
        phoneWrapper = (TextInputLayout) findViewById(R.id.input_layout_phone);
        passwordWrapper = (TextInputLayout) findViewById(R.id.input_layout_password_1);

        editTextName = (EditText) findViewById(R.id.input_name);
        editTextEmail = (EditText) findViewById(R.id.input_email_1);
        editTextPhone = (EditText) findViewById(R.id.input_phone);
        editTextPassword = (EditText) findViewById(R.id.input_password_1);

        editTextName.addTextChangedListener(new MyTextWatcher(editTextName));
        editTextPhone.addTextChangedListener(new MyTextWatcher(editTextPhone));
        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));


        fabChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To Choose profile Image
                onSelectImageClick(v);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Registration.


                if (!validateName()) {
                } else if (!validateEmail()) {
                } else if (!validatePhone()) {
                } else if (!validatePassword()) {
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link to Login Screen.
                //Navigate to Login Screen
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validateName(){
        name = editTextName.getText().toString().trim();
        if(name.isEmpty()){
            nameWrapper.setError("Please Enter Name!");
            return false;
        }
        else{
            nameWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        password = editTextPassword.getText().toString().trim();

        if(password.length() > 6){
            passwordWrapper.setErrorEnabled(false);
        }
        else {
            passwordWrapper.setError("Not a Valid Password!");
            return false;
        }
        return true;
    }

    private boolean validatePhone() {

        phoneNo = editTextPhone.getText().toString().trim();
        if(phoneNo.length() == 10){
            phoneWrapper.setErrorEnabled(false);
        }
        else{
            phoneWrapper.setError("Not a valid Password!");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        email = editTextEmail.getText().toString().trim();
        matcher = pattern.matcher(email);
        if(!matcher.matches()){
            emailWrapper.setError("Not a valid Email!");
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

                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email_1:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_password_1:
                    validatePassword();
                    break;

            }
        }
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                filePath = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
               // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (filePath != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(filePath);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
        .setMinCropResultSize(200,200)
                .setMaxCropResultSize(300,300)
                .start(this);
    }
}