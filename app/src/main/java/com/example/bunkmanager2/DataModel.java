package com.example.bunkmanager2;

public class DataModel {
    String Subject , Category;
    String Absent , Percentage , Present , Total;


    DataModel(){

    }

    public DataModel(String subject, String category, String absent, String percentage, String present, String total) {
        Subject = subject;
        Absent = absent;
        Percentage = percentage;
        Category = category;
        Present = present;
        Total = total;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getAbsent() {
        return Absent;
    }

    public void setAbsent(String absent) {
        Absent = absent;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    public String getPresent() {
        return Present;
    }

    public void setPresent(String present) {
        Present = present;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
