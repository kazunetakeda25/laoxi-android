package com.rider.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by chirag on 20/6/16.
 */
public class DateTimeClass {
    private static DateTimeClass dateTimeClass = new DateTimeClass();
    int mMinute, mHours, mDay, mMonth, mYear;
    int selMinute, selHours, selDay, selMonth, selYear, mTimeSet, Hours, Mins;
    private DateCallBack dateCallBack;

    private DateTimeClass() {
    }

    public static DateTimeClass getInstance() {
        return dateTimeClass;
    }

    public  String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a");
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
    }

    public ArrayList<String> slotGenerate(String time1, String time2, String d, String g) {

        long duration = Integer.parseInt(d) * 60 * 1000;
        long gap = Integer.parseInt(g) * 60 * 1000;
        String format = "hh:mm a";
        ArrayList<String> strings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date dateObj1 = sdf.parse(time1);
            Date dateObj2 = sdf.parse(time2);
            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot1 = new Date(dif);
                dif += duration;
                Date slot2 = new Date(dif);
                dif += gap;
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                strings.add(sdf1.format(slot1) + " To " + sdf2.format(slot2));
                Log.d("TAG", "Hour slot = " + sdf1.format(slot1) + " - " + sdf2.format(slot2));
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return strings;
    }

    public String localToUTCTime(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        TimeZone utcZone = TimeZone.getDefault();
        simpleDateFormat.setTimeZone(utcZone);

        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("HH:mm");
            simple.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateS = simple.format(date);
//            DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
//            DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    public String utcToLocalTime(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                //2016-05-18 13:12:52
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("dd / MM / yyyy hh:mm");
            TimeZone utcZone = TimeZone.getDefault();
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);
//            DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
//            DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    public String utcToLocalTimeNew(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //2016-05-18 13:12:52
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone utcZone = TimeZone.getDefault();
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);
//            DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
//            DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    Calendar getCalender(String date) {
        Calendar calendar = null;
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = form.parse(date);
            calendar = Calendar.getInstance();
           /* calendar.set(Calendar.MONTH,d1.getMonth());
            calendar.set(Calendar.YEAR,d1.getYear());*/
            calendar.setTime(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }



    public String get12Hours(String _24HourTime) {
        String time = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            time = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public String convertDate(String date) {
        String time = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("dd MMM yyy");
            Date _24HourDt = _24HourSDF.parse(date);
            time = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public String formateDob(String date) {
        String time = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("dd-MM-yyyy");
            Date _24HourDt = _24HourSDF.parse(date);
            time = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public String formatWs(String date) {
        String time = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("yyyy-MM-dd");
            Date _24HourDt = _24HourSDF.parse(date);
            time = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public String timeDiff(String time1, String time2) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        int min = 0;
        try {
            Date date1 = (Date) format.parse(time1);
            Date date2 = (Date) format.parse(time2);
            //time difference in milliseconds
            long timeDiff = date1.getTime() - date2.getTime();

            min = (int) ((timeDiff / 1000) / 60);
            //new date object with time difference
            Date diffDate = new Date(timeDiff);
            //formatted date string
            String timeDiffString = format.format(diffDate);
            System.out.println("Time Diff = " + timeDiffString + "min" + min);
        } catch (ParseException e) {
             e.printStackTrace();
        }
        return String.valueOf(Math.abs(min));
    }

    public int calTimeDiff(String time1, String time2) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        int min = 0;
        try {
            Date date1 = (Date) format.parse(time1);
            Date date2 = (Date) format.parse(time2);
            //time difference in milliseconds
            long timeDiff = date1.getTime() - date2.getTime();

            min = (int) ((timeDiff / 1000) / 60);
            //new date object with time difference
            Date diffDate = new Date(timeDiff);
            //formatted date string
            String timeDiffString = format.format(diffDate);
            System.out.println("Time Diff = " + timeDiffString + "min" + min);
        } catch (ParseException e) {
             e.printStackTrace();
        }
        return min;
    }

    public interface DateCallBack {
        void selDate(String date);


    }

}
