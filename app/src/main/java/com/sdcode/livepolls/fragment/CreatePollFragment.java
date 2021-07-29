package com.sdcode.livepolls.fragment;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sdcode.livepolls.R;
import com.sdcode.livepolls.database.DatabaseHelper;
import com.sdcode.livepolls.extraclasses.Messagee;

import static android.content.Context.MODE_PRIVATE;

public class CreatePollFragment extends Fragment {


    public CreatePollFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_poll, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        String email = preferences.getString("userEmail", "null");

        TextView tvTitle, title_Question, title_Options;
        EditText etQuestion, etOption1, etOption2, etOption3, etOption4;
        CheckBox publicCheck;
        Button btnCreatePoll, btnCreatePollOptions,btnMyPollsOptions,btnPublicPollsOptions;


        final String[] question = new String[1];
        final String[] option1 = new String[1];
        final String[] option2 = new String[1];
        final String[] option3 = new String[1];
        final String[] option4 = new String[1];

        publicCheck = v.findViewById(R.id.publicCheck);
        tvTitle = v.findViewById(R.id.title_CreatePoll);

        tvTitle.setText("Welcome " + email);

        btnCreatePollOptions = v.findViewById(R.id.btnCreatePollOptions);
        btnMyPollsOptions = v.findViewById(R.id.btnMyPollsOptions);
        btnPublicPollsOptions = v.findViewById(R.id.btnPublicPollsOptions);

        title_Question = v.findViewById(R.id.title_Question);
        title_Options = v.findViewById(R.id.title_Options);
        etQuestion = v.findViewById(R.id.etQuestion);
        etOption1 = v.findViewById(R.id.etOption1);
        etOption2 = v.findViewById(R.id.etOption2);
        etOption3 = v.findViewById(R.id.etOption3);
        etOption4 = v.findViewById(R.id.etOption4);
        btnCreatePoll = v.findViewById(R.id.btnCreatePoll);


        title_Question.setVisibility(v.GONE);
        title_Options.setVisibility(v.GONE);
        etQuestion.setVisibility(v.GONE);
        etOption1.setVisibility(v.GONE);
        etOption2.setVisibility(v.GONE);
        etOption3.setVisibility(v.GONE);
        etOption4.setVisibility(v.GONE);
        btnCreatePoll.setVisibility(v.GONE);
        publicCheck.setVisibility(v.GONE);

        btnCreatePollOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCreatePollOptions.setVisibility(v.GONE);
                btnMyPollsOptions.setVisibility(v.GONE);
                btnPublicPollsOptions.setVisibility(v.GONE);
                etQuestion.setVisibility(v.VISIBLE);
                etOption1.setVisibility(v.VISIBLE);
                etOption2.setVisibility(v.VISIBLE);
                etOption3.setVisibility(v.VISIBLE);
                etOption4.setVisibility(v.VISIBLE);
                btnCreatePoll.setVisibility(v.VISIBLE);
                publicCheck.setVisibility(v.VISIBLE);
                title_Question.setVisibility(v.VISIBLE);
                title_Options.setVisibility(v.VISIBLE);
            }
        });


        btnMyPollsOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPollsFragment fragment = new MyPollsFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });
        btnPublicPollsOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicPollsFragment fragment = new PublicPollsFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });



        final int[] publicFlag = {1};


        String finalEmail = email;
        btnCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question[0] = etQuestion.getText().toString();
                option1[0] = etOption1.getText().toString();
                option2[0] = etOption2.getText().toString();
                option3[0] = etOption3.getText().toString();
                option4[0] = etOption4.getText().toString();

                if (!publicCheck.isChecked()) {
                    publicFlag[0] = 0;
                }

                if (question[0].matches("")) {
                    Messagee.message(getContext(), "question cannot be empty!");
                } else if (option1[0].matches("") || option2[0].matches("") || option3[0].matches("") || option4[0].matches("")) {
                    Messagee.message(getContext(), "Option cannot be empty!");
                } else {
                    DatabaseHelper helper = new DatabaseHelper(getContext());
                    SQLiteDatabase database = helper.getWritableDatabase();

//"CREATE TABLE Users(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(255),LNAME VARCHAR(255),CITY VARCHAR(255),EMAIL VARCHAR(255),MONO VARCHAR(255),PASSWORD VARCHAR(255))";
//"CREATE TABLE Question(sno INTEGER PRIMARY KEY AUTOINCREMENT,question_text VARCHAR(255),livestatus INTEGER, publicstatus INTEGER,EMAIL VARCHAR(255),timestamp datetime default current_timestamp)";
//"CREATE TABLE Choice(chId INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL REFERENCES Question (sno), choice_text VARCHAR(255),votes INTEGER)";
                    ContentValues question_values = new ContentValues();
                    question_values.put("question_text", question[0]);
                    question_values.put("livestatus", 1);
                    question_values.put("publicstatus", publicFlag[0]);
                    question_values.put("EMAIL", finalEmail);

                    long id = database.insert("Question", null, question_values);

                    ContentValues choice_values1 = new ContentValues();
                    choice_values1.put("questionId", id);
                    choice_values1.put("choice_text", option1[0]);
                    choice_values1.put("votes", 0);

                    ContentValues choice_values2 = new ContentValues();
                    choice_values2.put("questionId", id);
                    choice_values2.put("choice_text", option2[0]);
                    choice_values2.put("votes", 0);

                    ContentValues choice_values3 = new ContentValues();
                    choice_values3.put("questionId", id);
                    choice_values3.put("choice_text", option3[0]);
                    choice_values3.put("votes", 0);

                    ContentValues choice_values4 = new ContentValues();
                    choice_values4.put("questionId", id);
                    choice_values4.put("choice_text", option4[0]);
                    choice_values4.put("votes", 0);


                    database.insert("Choice", null, choice_values1);
                    database.insert("Choice", null, choice_values2);
                    database.insert("Choice", null, choice_values3);
                    database.insert("Choice", null, choice_values4);

                    Messagee.message(getContext(), "Poll Created");

                    Fragment fragment = new MyPollsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        return v;
    }
}