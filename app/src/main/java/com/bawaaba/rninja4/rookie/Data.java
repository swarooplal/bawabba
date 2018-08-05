package com.bawaaba.rninja4.rookie;

import java.util.ArrayList;

/**
 * Created by rninja4 on 8/29/17.
 */

public class Data {

    public static ArrayList<Information>getdata(){

        ArrayList<Information>data=new ArrayList<>();

        int[] images={

                R.drawable.ic_accounting_and_legal, R.drawable.ic_programming, R.drawable.ic_architect_and_engineering,
                R.drawable.ic_art_and_craft, R.drawable.ic_beauty_and_skincare, R.drawable.ic_child_and_pet,
                R.drawable.ic_cleaning_maintainance_moving, R.drawable.ic_consultants, R.drawable.ic_fashion_designers,
                R.drawable.ic_digital_designers, R.drawable.ic_event_entertainment, R.drawable.ic_food_beverage,
                R.drawable.ic_health_wellbeing, R.drawable.ic_hobby_academics, R.drawable.ic_marketing,
                R.drawable.ic_music_, R.drawable.ic_sports_coaching, R.drawable.ic_travel_and_tourism,
                R.drawable.ic_photographers_and_videographers, R.drawable.ic_writers_and_translators,


        };

        String[]categories={
                "Accounting and Legal", "Programming", "Architecture and Engineering",
                "Arts and Crafts", "Beauty and Skincare", "Child and pet Care",
                "Cleaning,Maintenance and Moving", "Consultants", "Designers",
                "Digital Designers", "Entertainment and Events", "Food and Beverage",
                "Health and Well Being", "Hobby and Academic Classes", "Marketing",
                "Music and Audio", "Sports Coaching", "Travel and Tourism",
                "Video and Photography", "Writers and Translators",
        };

        for (int i=0;i<images.length;i++){

            Information current=new Information();
            current.titile=categories[i];
            current.imageID=images[i];
            data.add(current);
        }
        return data;
    }
}
