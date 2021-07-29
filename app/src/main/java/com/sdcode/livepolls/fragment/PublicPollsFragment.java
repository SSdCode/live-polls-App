package com.sdcode.livepolls.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdcode.livepolls.R;
import com.sdcode.livepolls.ResultPoll;
import com.sdcode.livepolls.VotePoll;
import com.sdcode.livepolls.database.DatabaseHelper;
import com.sdcode.livepolls.extraclasses.Messagee;
import com.sdcode.livepolls.extraclasses.MyPollsRVAdapter;

import static android.content.Context.MODE_PRIVATE;

public class PublicPollsFragment extends Fragment {
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    MyPollsRVAdapter myPollsRVAdapter;

    public PublicPollsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_public_polls, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        String email = preferences.getString("userEmail", "null");
        /*View All Food Details*/
        recyclerView = v.findViewById(R.id.publicPollsRV);
        databaseHelper = new DatabaseHelper(getContext());

        myPollsRVAdapter = new MyPollsRVAdapter(databaseHelper.getAllPollsData());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myPollsRVAdapter);


        myPollsRVAdapter.setOnItemClickListener(new MyPollsRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Messagee.message(getContext(), "Clicked on " + position);
            }

            @Override
            public void onVoteClick(int position) {
                int qid = myPollsRVAdapter.getQid(position);
                String op1 = myPollsRVAdapter.getop1(position);
                String op2 = myPollsRVAdapter.getop2(position);
                String op3 = myPollsRVAdapter.getop3(position);
                String op4 = myPollsRVAdapter.getop4(position);

                Intent i = new Intent(getContext(), VotePoll.class);
                i.putExtra("qid", qid);
                i.putExtra("op1", op1);
                i.putExtra("op2", op2);
                i.putExtra("op3", op3);
                i.putExtra("op4", op4);
                startActivity(i);
            }

            @Override
            public void onResultClick(int position) {
                int qid = myPollsRVAdapter.getQid(position);
                String op1 = myPollsRVAdapter.getop1(position);
                String op2 = myPollsRVAdapter.getop2(position);
                String op3 = myPollsRVAdapter.getop3(position);
                String op4 = myPollsRVAdapter.getop4(position);

                Intent i = new Intent(getContext(), ResultPoll.class);
                i.putExtra("qid", qid);
                i.putExtra("op1", op1);
                i.putExtra("op2", op2);
                i.putExtra("op3", op3);
                i.putExtra("op4", op4);
                startActivity(i);
            }


        });
        return v;
    }
}