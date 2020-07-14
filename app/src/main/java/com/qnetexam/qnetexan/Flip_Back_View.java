package com.qnetexam.qnetexan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Flip_Back_View extends Fragment {
    TextView textView;
    String string;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.flip_back_view, container, false);
        if (getArguments() != null){
            string = getArguments().getString("rndAns");
            textView = viewGroup.findViewById(R.id.answer_card);
            textView.setText(string);
        }

        return viewGroup;
    }
}
