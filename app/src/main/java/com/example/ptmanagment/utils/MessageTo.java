package com.example.ptmanagment.utils;

public class MessageTo {

    private String sender;
    private String to;
    private String msg;
    private boolean newMsg;

    //region Constructors
    public MessageTo(String sender, String to, String msg, boolean newMsg) {
        this.sender = sender;
        this.to = to;
        this.msg = msg;
        this.newMsg = newMsg;
    }

    public MessageTo() {
    }

    //endregion

    //region Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        this.newMsg = newMsg;
    }
}
