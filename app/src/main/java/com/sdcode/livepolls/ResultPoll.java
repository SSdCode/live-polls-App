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

public class ResultPoll extends AppCompatActivity {
    TextView tvQuestion, tvOp1, tvOp2, tvOp3, tvOp4, tvResOp1, tvResOp2, tvResOp3, tvResOp4;
    Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_poll);

        tvQuestion = findViewById(R.id.tvQuestionResult);
        tvOp1 = findViewById(R.id.tvOption1);
        tvOp2 = findViewById(R.id.tvOption2);
        tvOp3 = findViewById(R.id.tvOption3);
        tvOp4 = findViewById(R.id.tvOption4);
        tvResOp1 = findViewById(R.id.tvOption1Result);
        tvResOp2 = findViewById(R.id.tvOption2Result);
        tvResOp3 = findViewById(R.id.tvOption3Result);
        tvResOp4 = findViewById(R.id.tvOption4Result);
        btnBackHome = findViewById(R.id.btnBackHome);


        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Intent i = getIntent();
        int qid = i.getIntExtra("qid", 1);
        String op1 = i.getStringExtra("op1");
        String op2 = i.getStringExtra("op2");
        String op3 = i.getStringExtra("op3");
        String op4 = i.getStringExtra("op4");


//"CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";

        Cursor cursor = database.rawQuery("SELECT * FROM Question", null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {

            while (cursor.moveToNext()) {
                if (qid == cursor.getInt(0)) {

                    tvQuestion.setText(cursor.getString(1));
                    tvOp1.setText(op1);
                    tvOp2.setText(op2);
                    tvOp3.setText(op3);
                    tvOp4.setText(op4);

//"CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
                    Cursor cursor_choice = database.rawQuery("SELECT * FROM Choice", null);

                    cursor_choice.moveToPosition(-1);
                    while (cursor_choice.moveToNext()) {
                        if (cursor_choice.getInt(1) == qid){
                            if (cursor_choice.getString(2).equals(op1)) {
                                tvResOp1.setText(String.valueOf(cursor_choice.getInt(3)));
                            } else if (cursor_choice.getString(2).equals(op2)) {
                                tvResOp2.setText(String.valueOf(cursor_choice.getInt(3)));
                            } else if (cursor_choice.getString(2).equals(op3)) {
                                tvResOp3.setText(String.valueOf(cursor_choice.getInt(3)));
                            } else if (cursor_choice.getString(2).equals(op4)) {
                                tvResOp4.setText(String.valueOf(cursor_choice.getInt(3)));
                            }
                        }
                    }
                    cursor_choice.moveToPosition(-1);
                }
            }
            cursor.close();
        }

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(getApplicationContext(), VotePoll.class);
//                i.putExtra("qid", qid);
//                i.putExtra("op1", op1);
//                i.putExtra("op2", op2);
//                i.putExtra("op3", op3);
//                i.putExtra("op4", op4);
//                startActivity(i);
                Intent i = new Intent(getApplicationContext(), UserHome.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), UserHome.class);
        startActivity(i);
    }
}
