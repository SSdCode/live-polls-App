package com.sdcode.livepolls;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
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

import com.sdcode.livepolls.database.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    Button btn;
    EditText fName, lName, city, email, mo, pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        casting();
        btn = (Button) findViewById(R.id.btnSub);
        final CheckBox CBViewHidePass = (CheckBox) findViewById(R.id.cbox);

        CBViewHidePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Firstname = fName.getText().toString().trim();
                String Lastname = lName.getText().toString().trim();
                String City = city.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Mono = mo.getText().toString().trim();
                String Password = pass1.getText().toString().trim();
                String RePassword = pass2.getText().toString().trim();

                if (Firstname.isEmpty() || Lastname.isEmpty() || City.isEmpty() || Email.isEmpty() || Mono.isEmpty() || Password.isEmpty() || RePassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter all details!", Toast.LENGTH_SHORT).show();
                } else if (!Password.equals(RePassword)) {
                    Toast.makeText(getApplicationContext(), "Password not match!", Toast.LENGTH_SHORT).show();
                } else if (Email.equals("Admin")) {
                    Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                } else {

                    Boolean ans = checkUser(Email, Mono);
                    if (ans) {
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase database = helper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("FNAME", Firstname);
                        values.put("LNAME", Lastname);
                        values.put("CITY", City);
                        values.put("EMAIL", Email);
                        values.put("MONO", Mono);
                        values.put("PASSWORD", Password);
                        database.insert("Users", null, values);
                        Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "User Already Register!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private Boolean checkUser(String email, String mono) {
//Users(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(255),LNAME VARCHAR(255),CITY VARCHAR(255),EMAIL VARCHAR(255),MONO VARCHAR(255),PASSWORD VARCHAR(255))";
        DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase database = helper.getReadableDatabase();

        String readData = "SELECT * FROM Users";
        Cursor cursor = database.rawQuery(readData, null);

        if (cursor.getCount() == 0) {
            return true;
        } else {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(4);
                String mobno = cursor.getString(5);
                if (email.equals(name) || mono.equals(mobno)) {
                    return false;
                }
            } while (cursor.moveToNext());
        }
        return true;
    }

    private void casting() {
        fName = findViewById(R.id.etFname);
        lName = findViewById(R.id.etLname);
        city = findViewById(R.id.etCity);
        email = findViewById(R.id.etEmail);
        mo = findViewById(R.id.etMno);
        pass1 = findViewById(R.id.etPass1);
        pass2 = findViewById(R.id.etPass2);
    }
}
