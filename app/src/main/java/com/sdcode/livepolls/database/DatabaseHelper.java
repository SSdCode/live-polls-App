package com.sdcode.livepolls.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sdcode.livepolls.extraclasses.Messagee;
import com.sdcode.livepolls.extraclasses.ModelClass;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    private static final String myDb = "MyDb";
    private static final int version = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, myDb, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createtable1 = "CREATE TABLE Users(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(255),LNAME VARCHAR(255),CITY VARCHAR(255),EMAIL VARCHAR(255),MONO VARCHAR(255),PASSWORD VARCHAR(255))";
        db.execSQL(createtable1);

        String createtable2 = "CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";
        db.execSQL(createtable2);

        String createtable3 = "CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
        db.execSQL(createtable3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<ModelClass> getMyPollsData(String userEmail) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<ModelClass> objectModelClassList = new ArrayList<>();

        Cursor cursor_questions = database.rawQuery("select * from Question", null);
        Cursor cursor_choices = database.rawQuery("select * from Choice", null);

        if (cursor_questions.getCount() != 0 && cursor_choices.getCount() != 0) {
            cursor_questions.moveToPosition(-1);
            cursor_choices.moveToPosition(-1);
//            String userEmail = AfterUseVariables.getEmail();

            while (cursor_questions.moveToNext()) {

                String question = cursor_questions.getString(1);
                int liveStatus = cursor_questions.getInt(2);
                int publicStatus = cursor_questions.getInt(3);
                String email = cursor_questions.getString(4);
                int qid = cursor_questions.getInt(0);

                String choice1 = "null";
                String choice2 = "null";
                String choice3 = "null";
                String choice4 = "null";

                if (userEmail.equals(email)) {
                    int choice = 0;
                    while (cursor_choices.moveToNext()) {
                        int questionCurrent = cursor_choices.getInt(1);

                        if (qid == questionCurrent) {
                            choice++;
                            String choiceText = cursor_choices.getString(2);
                            switch (choice) {
                                case 1:
                                    choice1 = choiceText;
                                    break;
                                case 2:
                                    choice2 = choiceText;
                                    break;
                                case 3:
                                    choice3 = choiceText;
                                    break;
                                case 4:
                                    choice4 = choiceText;
                                    break;
                                default:
                                    Messagee.message(context, "choice no is wrong!");
                            }
                        }

                    }
                    cursor_choices.moveToPosition(-1);
                    objectModelClassList.add(new ModelClass(question, choice1, choice2, choice3, choice4, qid, liveStatus, publicStatus));
                }
            }
            cursor_choices.close();
            cursor_questions.close();
            return objectModelClassList;
        } else {
            Messagee.message(context, "No values in database");
            cursor_choices.close();
            cursor_questions.close();
            return null;
        }
    }

    public ArrayList<ModelClass> getAllPollsData() {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<ModelClass> objectModelClassList = new ArrayList<>();

        Cursor cursor_questions = database.rawQuery("select * from Question", null);
        Cursor cursor_choices = database.rawQuery("select * from Choice", null);

        if (cursor_questions.getCount() != 0 && cursor_choices.getCount() != 0) {
            cursor_questions.moveToPosition(-1);
            cursor_choices.moveToPosition(-1);

            while (cursor_questions.moveToNext()) {

                String question = cursor_questions.getString(1);
                int liveStatus = cursor_questions.getInt(2);
                int publicStatus = cursor_questions.getInt(3);
                int qid = cursor_questions.getInt(0);

                if (publicStatus == 1) {

                    String choice1 = "null";
                    String choice2 = "null";
                    String choice3 = "null";
                    String choice4 = "null";

                    int choice = 0;
                    while (cursor_choices.moveToNext()) {
                        int questionCurrent = cursor_choices.getInt(1);

                        if (qid == questionCurrent) {
                            choice++;
                            String choiceText = cursor_choices.getString(2);
                            switch (choice) {
                                case 1:
                                    choice1 = choiceText;
                                    break;
                                case 2:
                                    choice2 = choiceText;
                                    break;
                                case 3:
                                    choice3 = choiceText;
                                    break;
                                case 4:
                                    choice4 = choiceText;
                                    break;
                                default:
                                    Messagee.message(context, "choice no is wrong!");
                            }
                        }
                    }
                    cursor_choices.moveToPosition(-1);
                    objectModelClassList.add(new ModelClass(question, choice1, choice2, choice3, choice4, qid, liveStatus, publicStatus));
                }
            }
            cursor_choices.close();
            cursor_questions.close();
            return objectModelClassList;
        } else {
            Messagee.message(context, "No values in database");
            cursor_choices.close();
            cursor_questions.close();
            return null;
        }
    }
}
