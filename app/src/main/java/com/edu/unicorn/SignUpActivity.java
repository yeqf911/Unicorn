package com.edu.unicorn;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.edu.unicorn.db.SqliteHelper;

public class SignUpActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private EditText usernameEdt;
    private EditText passwordEdt;
    private EditText reEnterPasswordText;
    private EditText emailEdt;
    private EditText phoneEdt;
    private Button signUpButton;
    private TextView loginLink;

    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        dbHelper = new SqliteHelper(this, SqliteHelper.DB_NAME, null, SqliteHelper.Version);
    }

    private void initView() {
        usernameEdt = findViewById(R.id.input_name);
        passwordEdt = findViewById(R.id.input_password);
        reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        emailEdt = findViewById(R.id.input_email);
        phoneEdt = findViewById(R.id.input_phone);
        signUpButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);

        signUpButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.link_login:
                returnToLogin();
                break;
        }
    }

    private void signUp() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(SignUpActivity.this, "请输入正确的信息", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpButton.setEnabled(false);

        String username = usernameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String phone = phoneEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        // TODO: Implement your own signup logic here.
        if (checkIsUserAlreadyExist(email)) {
            Toast.makeText(SignUpActivity.this, "该邮箱已被注册，注册失败", Toast.LENGTH_SHORT).show();
            signUpButton.setEnabled(true);
        } else {
            if (register(username, password, email, phone)) {
                onSignUpSuccess();
            } else {
                onSignUpFailed();
            }
        }
    }

    private boolean register(String username, String password, String email, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("password", password);
        values.put("email", email);
        values.put("phone", phone);
        db.insert(SqliteHelper.USER_TABLE, null, values);
        db.close();
        return true;
    }

    private boolean checkIsUserAlreadyExist(String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from " + SqliteHelper.USER_TABLE + " where email =?";
        Cursor cursor = db.rawQuery(Query, new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private void onSignUpSuccess() {
        signUpButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("email", emailEdt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onSignUpFailed() {
        signUpButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();
    }

    private void returnToLogin() {
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String name = usernameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String mobile = phoneEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            usernameEdt.setError("at least 3 characters");
            valid = false;
        } else {
            usernameEdt.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("enter a valid email address");
            valid = false;
        } else {
            emailEdt.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 11) {
            phoneEdt.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phoneEdt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEdt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEdt.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }

}
