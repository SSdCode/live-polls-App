package com.sdcode.livepolls.extraclasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdcode.livepolls.R;

import java.util.ArrayList;

public class MyPollsRVAdapter extends RecyclerView.Adapter<MyPollsRVAdapter.RVViewHolderClass> {
    private MyPollsRVAdapter.OnItemClickListener mListener;
    ArrayList<ModelClass> modelClassList;

    public int getQid(int position) {
        return modelClassList.get(position).getQid();
    }

    public String getop1(int position) {
        return modelClassList.get(position).getOption1();
    }

    public String getop2(int position) {
        return modelClassList.get(position).getOption2();
    }

    public String getop3(int position) {
        return modelClassList.get(position).getOption3();
    }

    public String getop4(int position) {
        return modelClassList.get(position).getOption4();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onVoteClick(int position);

        void onResultClick(int position);
    }

    public void setOnItemClickListener(MyPollsRVAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public MyPollsRVAdapter(ArrayList<ModelClass> objectModelClassList) {
        modelClassList = objectModelClassList;
    }

    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_row_mypolls, parent, false);
        return new MyPollsRVAdapter.RVViewHolderClass(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position) {
        ModelClass modelClass = modelClassList.get(position);

        holder.question.setText(modelClass.getQuestionCurrent());
        if(modelClass.getLiveStatus().equals("1"))
            holder.liveStatus.setText("Yes");
        else
            holder.liveStatus.setText("No");


        if(modelClass.getPublicStatus().equals("1"))
            holder.publicStatus.setText("Yes");
        else
            holder.publicStatus.setText("No");
    }

//    public String result(int position) {
//        return modelClassList.get(position).getImageName();
//    }

    @Override
    public int getItemCount() {
        try {
            return modelClassList.size();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder {
        TextView question;
        TextView liveStatus;
        TextView publicStatus;
        Button btnVote, btnResult;


        public RVViewHolderClass(@NonNull View itemView, final MyPollsRVAdapter.OnItemClickListener listener) {
            super(itemView);

            question = itemView.findViewById(R.id.tvQuestion);
            liveStatus = itemView.findViewById(R.id.tvLiveStatus);
            publicStatus = itemView.findViewById(R.id.tvPublicStatus);
            btnVote = itemView.findViewById(R.id.btnVote);
            btnResult = itemView.findViewById(R.id.btnResult);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            btnVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onVoteClick(position);
                        }
                    }
                }
            });

            btnResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onResultClick(position);
                        }
                    }
                }
            });
        }
    }
}
