package com.example.enter.oceny;

import java.util.List;



class MarksModel {
    private String name;
    private int mark;

    //konstruktor
    public MarksModel(String name)
    {
        this.name = name;
    }

   //dostep do name
    public String getName()
    {
        return name;
    }

    //dostep do mark
    public int getMark()
    {
        return mark;
    }

    //ustawienie pola mark
    public void setMark(int mark)
    {
        this.mark = mark;
    }

    //wyliczenie średniej
    public static double average(List<MarksModel> marks)
    {
        int quantity = marks.size();
        int sum = 0;

        for(int i = 0; i < quantity; i++)
        {
            MarksModel currentMark = marks.get(i);
            sum += currentMark.getMark()+1;
        }

        return (double)sum / quantity;
    }
//sprawdzenie czy wszystko zaznaczone
    public static boolean allChecked(List<MarksModel> marks)
    {
        int quantity = marks.size();

        for(int i = 0; i < quantity; i++)
        {
            MarksModel currentMark = (MarksModel)marks.get(i);

            if(currentMark.getMark() == 0)
            {
                return false;
            }
        }

        return true;


}
}
