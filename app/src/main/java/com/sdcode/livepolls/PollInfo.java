package com.sdcode.livepolls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdcode.livepolls.database.DatabaseHelper;
import com.sdcode.livepolls.extraclasses.Messagee;

public class PollInfo extends AppCompatActivity {

    TextView tvQuestion, tvOp1, tvOp2, tvOp3, tvOp4, tvLiveStatus, tvPublicStatus;
    String question, liveStatus, publicStatus;
    Button btn_delete_poll, btn_Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_info);

        casting();

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Intent i = getIntent();
        final int qid = i.getIntExtra("qid", 1);

        final String op1 = i.getStringExtra("op1");
        final String op2 = i.getStringExtra("op2");
        final String op3 = i.getStringExtra("op3");
        final String op4 = i.getStringExtra("op4");


//"CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";

        Cursor cursor = database.rawQuery("SELECT * FROM Question", null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {

            while (cursor.moveToNext()) {
                if (qid == cursor.getInt(0)) {

                    question = cursor.getString(1);



//                    liveStatus = cursor.getString(2);
                    if(cursor.getString(2).equals("1"))
                        liveStatus = "Yes";
                    else
                        liveStatus = "No";

//                    publicStatus = cursor.getString(3);
                    if(cursor.getString(3).equals("1"))
                        publicStatus = "Yes";
                    else
                        publicStatus = "No";


                    tvQuestion.setText(question);
                    tvOp1.setText(op1);
                    tvOp2.setText(op2);
                    tvOp3.setText(op3);
                    tvOp4.setText(op4);
                    tvLiveStatus.setText(liveStatus);
                    tvPublicStatus.setText(publicStatus);
                }
            }
            cursor.close();
        }

        btn_delete_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase database = helper.getReadableDatabase();

//"CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";
                database.delete("Question", "sno = ?", new String[]{String.valueOf(qid)});
//"CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
                database.delete("Choice", "questionId = ?", new String[]{String.valueOf(qid)});

                Intent i = new Intent(getApplicationContext(), UserHome.class);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), "Poll Remove Successful", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ResultPoll.class);
                i.putExtra("qid", qid);
                i.putExtra("op1", op1);
                i.putExtra("op2", op2);
                i.putExtra("op3", op3);
                i.putExtra("op4", op4);
                startActivity(i);
            }
        });
    }

    private void casting() {
        tvQuestion = findViewById(R.id.tvMyQuestion);
        tvOp1 = findViewById(R.id.tvMyOption1);
        tvOp2 = findViewById(R.id.tvMyOption2);
        tvOp3 = findViewById(R.id.tvMyOption3);
        tvOp4 = findViewById(R.id.tvMyOption4);
        tvLiveStatus = findViewById(R.id.tvMyLiveStatus);
        tvPublicStatus = findViewById(R.id.tvMyPublicStatus);
        btn_delete_poll = findViewById(R.id.btnDeleteMyPoll);
        btn_Result = findViewById(R.id.btnResult);
    }
}