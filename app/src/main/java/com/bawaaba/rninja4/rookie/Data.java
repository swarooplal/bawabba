package com.bawaaba.rninja4.rookie;

import java.util.ArrayList;

/**
 * Created by rninja4 on 8/29/17.
 */

public class Data {

    public static ArrayList<Information>getdata(){

        ArrayList<Information>data=new ArrayList<>();

        int[] images={

                R.drawable.accounting_bawaba, R.drawable.program_bawabba, R.drawable.architecture_bawaba,
                R.drawable.arts_bawaba, R.drawable.beauty_bawaba, R.drawable.child_bawaba,
                R.drawable.cleaning_bawaba, R.drawable.consultant_bawaba, R.drawable.design_bawaba,
                R.drawable.digital_bawaba, R.drawable.events_bawaba, R.drawable.food_bawaba,
                R.drawable.health_bawaba, R.drawable.education_bawaba, R.drawable.marketing_bawaba,
                R.drawable.music_bawaba, R.drawable.sports_bawaba, R.drawable.travel_bawaba,
                R.drawable.photo_bawaba, R.drawable.writer_bawaba,


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
