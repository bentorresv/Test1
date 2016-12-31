package com.matias.core;

/*
 * Created by BenTorres on 01/11/2015.
 */
class CheckLog {

    private int hours;
    private int minutes;
    private int seconds;
    private int day;
    private int month;
    private int year;
    private int am_pm;
    private boolean check_in;

    CheckLog(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    CheckLog(int hours, int minutes, int seconds, int am_pm, boolean check_in) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.check_in = check_in;
        this.am_pm = am_pm;
    }

    boolean isCheck_in() {
        return check_in;
    }

    void setCheck_in(boolean check_in) {
        this.check_in = check_in;
    }

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    int getDay() {
        return day;
    }

    void setDay(int day) {
        this.day = day;
    }

    int getSeconds() {
        return seconds;
    }

    void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    int getMinutes() {
        return minutes;
    }

    void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    int getHours() {
        return hours;
    }

    void setHours(int hours) {
        this.hours = hours;
    }

    int getAm_pm() {
        return am_pm;
    }

    void setAm_pm(int am_pm) {
        this.am_pm = am_pm;
    }

    String get_month_string(int month_int){

        String month_string = "";
        switch(month_int){
            case 0: month_string = "Ene"; break;
            case 1: month_string = "Feb"; break;
            case 2: month_string = "Mar"; break;
            case 3: month_string = "Abr"; break;
            case 4: month_string = "May"; break;
            case 5: month_string = "Jun"; break;
            case 6: month_string = "Jul"; break;
            case 7: month_string = "Ago"; break;
            case 8: month_string = "Sep"; break;
            case 9: month_string = "Oct"; break;
            case 10: month_string = "Nov"; break;
            case 11: month_string = "Dic"; break;
        }
        return month_string;
    }

    String get_am_pm_string(){
        String am_pm_string = "";
        switch (am_pm){
            case 0: am_pm_string = "am"; break;
            case 1: am_pm_string = "pm"; break;
        }
        return am_pm_string;
    }

    int get_time_in_minutes(){
        return((hours * 60) + minutes);
    }

    int getHours_12format(){
        if (hours > 12) return hours - 12;
        else return hours;
    }

}
