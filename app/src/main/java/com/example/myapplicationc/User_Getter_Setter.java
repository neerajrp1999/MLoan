package com.example.myapplicationc;

import java.time.LocalDateTime;

public class User_Getter_Setter {
    String name,mobileno,Attachment_link,History_Table_Attachment_link,TextData;
    int Id,dailyOrMonthly,IsActiveUser,Return_time,N_u_id,isPaid;
    Double Amount,UpdateAmount,rate,ExchangeAmount;
    LocalDateTime DueDate,Day_Before_DueDate,StartDate,PayDate,ExchangeDate;

    public int getN_u_id() {
        return N_u_id;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public void setN_u_id(int n_u_id) {
        N_u_id = n_u_id;
    }

    public String getHistory_Table_Attachment_link() {
        return History_Table_Attachment_link;
    }

    public void setHistory_Table_Attachment_link(String history_Table_Attachment_link) {
        History_Table_Attachment_link = history_Table_Attachment_link;
    }

    public String getTextData() {
        return TextData;
    }

    public void setTextData(String textData) {
        TextData = textData;
    }

    public Double getExchangeAmount() {
        return ExchangeAmount;
    }

    public void setExchangeAmount(Double exchangeAmount) {
        ExchangeAmount = exchangeAmount;
    }

    public LocalDateTime getExchangeDate() {
        return ExchangeDate;
    }

    public void setExchangeDate(LocalDateTime exchangeDate) {
        ExchangeDate = exchangeDate;
    }

    public LocalDateTime getPayDate() {
        return PayDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        PayDate = payDate;
    }

    public LocalDateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        StartDate = startDate;
    }

    public Double getUpdateAmount() {
        return UpdateAmount;
    }

    public void setUpdateAmount(Double updateAmount) {
        UpdateAmount = updateAmount;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        DueDate = dueDate;
    }

    public LocalDateTime getDay_Before_DueDate() {
        return Day_Before_DueDate;
    }

    public void setDay_Before_DueDate(LocalDateTime day_Before_DueDate) {
        Day_Before_DueDate = day_Before_DueDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public int getDailyOrMonthly() {
        return dailyOrMonthly;
    }

    public void setDailyOrMonthly(int dailyOrMonthly) {
        this.dailyOrMonthly = dailyOrMonthly;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getAttachment_link() {
        return Attachment_link;
    }

    public void setAttachment_link(String attachment_link) {
        Attachment_link = attachment_link;
    }

    public int getIsActiveUser() {
        return IsActiveUser;
    }

    public void setIsActiveUser(int isActiveUser) {
        IsActiveUser = isActiveUser;
    }

    public int getReturn_time() {
        return Return_time;
    }

    public void setReturn_time(int return_time) {
        Return_time = return_time;
    }

}