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


        Cursor cursor = database.rawQuery("SELECT * FROM Question", null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (qid == cursor.getInt(0)) {
                    tvQuestion.setText(cursor.getString(1));
                    rbOp1.setText(op1);
                    rbOp2.setText(op2);
                    rbOp3.setText(op3);
                    rbOp4.setText(op4);
                }
            }
            cursor.close();
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
                    Cursor cursor = database.rawQuery("SELECT * FROM Choice", null);

                    cursor.moveToPosition(-1);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(2).equals(choiceSelected)) {
                            votes = cursor.getInt(3) + 1;
                            break;
                        }
                    }
                    ContentValues cv = new ContentValues();
                    cv.put("votes",votes); //These Fields should be your String values of actual column names

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