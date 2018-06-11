package com.example.enter.oceny;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Array;
import java.util.List;

/**
 * Created by enter on 23.03.18.
 */

public class MarksAdapter extends ArrayAdapter<MarksModel>{
    private List<MarksModel> marks;
    private Activity context;

    public MarksAdapter(Activity context, List<MarksModel> marks)
    {
        super(context,0, marks);

        this.marks = marks;
        this.context = context;
    }

    public View getView(int row, View view, ViewGroup parent){
        MarksModel currentMark = marks.get(row);
        if(view == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.source, parent, false);
        }

        TextView markText = (TextView) view.findViewById(R.id.text);
        markText.setText(currentMark.getName()); //przypisanie nazwy z modelu do etykiety
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(marksListener(currentMark));//przypisanie słuchacza do radio group

        return view;
    }
    private RadioGroup.OnCheckedChangeListener marksListener(final MarksModel currMark)
    {
        return new RadioGroup.OnCheckedChangeListener()
        {
            @Override //sprawdzenie które checkboxy są wciśnięte
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if(checkedId == R.id.b1){
                    currMark.setMark(1);
                }else if(checkedId == R.id.b2){
                    currMark.setMark(2);
                } else if(checkedId == R.id.b3){
                    currMark.setMark(3);
                } else if(checkedId == R.id.b4){
                    currMark.setMark(4);
                }else currMark.setMark(0);

            }
        };
    }




}



