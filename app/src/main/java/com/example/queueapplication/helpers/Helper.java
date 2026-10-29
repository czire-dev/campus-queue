package com.example.queueapplication.helpers;

public class Helper {
    public static String formatPosition(int position) {
        switch (position) {
            case 1:
                return position + "st";
            case 2:
                return position + "nd";
            case 3:
                return position + "rd";
            default:
                return position + "th";
        }
    }
}
