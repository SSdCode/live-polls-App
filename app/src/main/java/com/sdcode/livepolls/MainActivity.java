package com.sdcode.livepolls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.sdcode.livepolls.admin.AdminSide;
import com.sdcode.livepolls.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    Button RegLinkC;
    EditText uname;
    EditText pass;
    Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String userEmail = preferences.getString("userEmail", "null");

        if (isLoggedIn){
            Intent i = new Intent(getApplicationContext(), UserHome.class);
            startActivity(i);
            finish();
        }


        RegLinkC = findViewById(R.id.RegLink);
        btnLog = findViewById(R.id.btnLogin);
        uname = findViewById(R.id.etUname);
        pass = findViewById(R.id.etPassMain);

        final CheckBox CBViewHidePass = (CheckBox) findViewById(R.id.checkBox);

        CBViewHidePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        RegLinkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = uname.getText().toString().trim();
                final String userPassword = pass.getText().toString().trim();
                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username or passwords are empty", Toast.LENGTH_SHORT).show();
                } else if (userEmail.equals("Admin") & userPassword.equals("Admin")) {
                    Intent i = new Intent(MainActivity.this, AdminSide.class);
                    startActivity(i);
                } else {
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    SQLiteDatabase database = helper.getReadableDatabase();

                    String[] columns = {"EMAIL", "PASSWORD"};
                    String[] cValues = {userEmail, userPassword};
                    Cursor cursor = database.query("Users", columns, "EMAIL = ? AND PASSWORD = ?", cValues, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {

                            SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userEmail", userEmail);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), UserHome.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter valid details!", Toast.LENGTH_SHORT).show();
                        }
                    }//if close
                }
            }
        });
    }
}