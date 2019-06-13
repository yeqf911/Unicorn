package com.edu.unicorn;

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

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGN_UP = 0;

    private EditText emailEdt;
    private EditText passwordEdt;
    private Button loginButton;
    private TextView signUpLink;

    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        dbHelper = new SqliteHelper(this, SqliteHelper.DB_NAME, null, SqliteHelper.Version);
    }

    private void initView() {
        emailEdt = findViewById(R.id.input_email);
        passwordEdt = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signUpLink = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(this);
        signUpLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startMainActivity();
//                login();
                break;
            case R.id.link_signup:
                signUp();
                break;
        }
    }

    private void signUp() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            Toast.makeText(this, "非法邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        loginButton.setEnabled(false);

        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + SqliteHelper.USER_TABLE + " where email=?";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        if (cursor.moveToFirst()) {
            String authSql = "select * from " + SqliteHelper.USER_TABLE + " where email=? and password=?";
            cursor = db.rawQuery(authSql, new String[]{email, password});

            if (cursor.moveToFirst()) {
                cursor.close();
                startMainActivity();
            } else {
                Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        } else {
            Toast.makeText(this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show();
        }

        loginButton.setEnabled(true);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    // 检查输入的合法性
    public boolean validate() {
        boolean valid = true;
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("enter a valid email address");
            valid = false;
        } else {
            emailEdt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            passwordEdt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEdt.setError(null);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                String email = data.getStringExtra("email");
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                emailEdt.setText(email);
            }
        }
    }
}
