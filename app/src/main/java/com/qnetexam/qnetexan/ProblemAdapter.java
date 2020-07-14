package com.qnetexam.qnetexan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProblemList> problemLists;

    public ProblemAdapter(Context context, ArrayList<ProblemList> problemLists){

        inflater = LayoutInflater.from(context);
        this.problemLists = problemLists;

    }

    @NonNull
    @Override
    public ProblemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ViewType){

        View view = inflater.inflate(R.layout.problem_lists, viewGroup, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ProblemAdapter.MyViewHolder holder, int position){

        holder.questionView.setText(problemLists.get(position).getQuestion());
        holder.answerView.setText(problemLists.get(position).getAnswer());

    }

    @Override
    public int getItemCount(){
        return problemLists.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView questionView;
        TextView answerView;

        public MyViewHolder(View itemView){
            super(itemView);

            questionView = itemView.findViewById(R.id.question);
            answerView = itemView.findViewById(R.id.answer);

        }
    }

}
