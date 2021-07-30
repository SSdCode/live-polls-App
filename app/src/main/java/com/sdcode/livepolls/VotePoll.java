package com.sdcode.livepolls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sdcode.livepolls.database.DatabaseHelper;
import com.sdcode.livepolls.extraclasses.Messagee;

public class VotePoll extends AppCompatActivity {
    TextView tvQuestion;
    RadioGroup radioGroup;
    RadioButton rbOptionSelected, rbOp1, rbOp2, rbOp3, rbOp4;
    Button btnVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);


        tvQuestion = findViewById(R.id.tvQuestionVote);
        radioGroup = findViewById(R.id.radioGroup);
        btnVote = findViewById(R.id.btnVote);
        rbOp1 = findViewById(R.id.radioOption1);
        rbOp2 = findViewById(R.id.radioOption2);
        rbOp3 = findViewById(R.id.radioOption3);
        rbOp4 = findViewById(R.id.radioOption4);


        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Intent i = getIntent();
        final int qid = i.getIntExtra("qid", 1);
        final String op1 = i.getStringExtra("op1");
        final String op2 = i.getStringExtra("op2");
        final String op3 = i.getStringExtra("op3");
        final String op4 = i.getStringExtra("op4");


//"CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";
        Cursor cursor_question = database.rawQuery("SELECT * FROM Question", null);
//"CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
        Cursor cursor_choice = database.rawQuery("SELECT * FROM Choice", null);
        cursor_choice.moveToPosition(-1);
        cursor_question.moveToPosition(-1);
        if (cursor_question.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor_question.moveToNext()) {
                if (qid == cursor_question.getInt(0)) {
                    tvQuestion.setText(cursor_question.getString(1));
                }
            }
            int choice = 0;
            while (cursor_choice.moveToNext()) {
                if (qid == cursor_choice.getInt(1)) {
                    choice++;
                    String choiceText = cursor_choice.getString(2);
                    switch (choice) {
                        case 1:
                            rbOp1.setText(choiceText);
                            break;
                        case 2:
                            rbOp2.setText(choiceText);
                            break;
                        case 3:
                            rbOp3.setText(choiceText);
                            break;
                        case 4:
                            rbOp4.setText(choiceText);
                            break;
                        default:
                            Messagee.message(getApplicationContext(), "choice no is wrong!");
                    }
                }

            }
            cursor_question.close();
            cursor_choice.close();
        }


        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                rbOptionSelected = (RadioButton) findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(getApplicationContext(), "First Select Option!", Toast.LENGTH_SHORT).show();
                } else {
                    String choiceSelected = rbOptionSelected.getText().toString();
                    int votes = 0;


//"CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
                    Cursor cursor_choice = database.rawQuery("SELECT * FROM Choice", null);

                    cursor_question.moveToPosition(-1);
                    cursor_choice.moveToPosition(-1);

                    while (cursor_choice.moveToNext()) {
                        if (cursor_choice.getInt(1) == qid) {
                            if (cursor_choice.getString(2).equals(choiceSelected)) {
                                votes = cursor_choice.getInt(3) + 1;
                                break;
                            }
                        }
                    }


                    ContentValues cv = new ContentValues();
                    cv.put("votes", votes); //These Fields should be your String values of actual column names

                    database.update("Choice", cv, "choice_text = ? and questionId = ?", new String[]{choiceSelected, String.valueOf(qid)});
//                    database.execSQL("UPDATE " + "Choice" + " SET votes =  " + votes + " WHERE choice_text = " + "'" + choiceSelected + "'");

                    Intent i = new Intent(getApplicationContext(), ResultPoll.class);
                    i.putExtra("qid", qid);
                    i.putExtra("op1", op1);
                    i.putExtra("op2", op2);
                    i.putExtra("op3", op3);
                    i.putExtra("op4", op4);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), choiceSelected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}