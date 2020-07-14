package com.qnetexam.qnetexan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Flip_Front_View extends Fragment {
    TextView textView;
    String string, Quest;
    /**
     * A fragment representing the front of the card.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.flip_front_view, container, false);
        if (getArguments() != null){
            string = getArguments().getString("rndQue");
            textView = viewGroup.findViewById(R.id.question_card);
            textView.setText(string);
        }

        return viewGroup;
    }
}

    