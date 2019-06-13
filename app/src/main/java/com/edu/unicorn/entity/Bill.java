package com.edu.unicorn.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill {
    public static String TYPE_FOOD = "餐饮";
    public static String TYPE_SHOPPING = "购物";
    public static String TYPE_TRAFFC = "交通";
    public static String TYPE_ENTERTAINMENT = "娱乐";
    public static String TYPE_MEDICAL = "医疗";
    public static String TYPE_STUDY = "学习";
    public static String TYPE_SPORT = "运动";


    public static String WAY_CREDIT_CARD = "信用卡";
    public static String WAY_CASH = "现金";

    private int id = 0;
    private int userId = 0;
    private String type;
    private double income;
    private double outcome;
    private Date date;
    private String way;
    private String comment;

    public Bill(String type, double income, double outcome, Date date, String way, String comment) {
        this.type = type;
        this.income = income;
        this.outcome = outcome;
        this.date = date;
        this.way = way;
        this.comment = comment;
    }

    public Bill() {
        this.type = "";
        this.income = 0.0;
        this.outcome = 0.0;
        this.date = new Date();
        this.way = "";
        this.comment = "";
    }

    public Date getDate() {
        return date;
    }

    public double getIncome() {
        return income;
    }

    public double getOutcome() {
        return outcome;
    }

    public String getType() {
        return type;
    }

    public String getWay() {
        return way;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String dateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(this.date);
        return str;
    }

    @Override
    public String toString() {
        return this.type + ":" + this.income;
    }
}
