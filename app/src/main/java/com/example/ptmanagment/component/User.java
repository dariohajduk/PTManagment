package com.example.ptmanagment.component;


public class User {
    //region Variables
    private String first;
    private String last;
    private String email;
    private String uid;
    private String department;
    private boolean isAdmin;
    private boolean isManager;
    private String shift;
    private String displayName;

    //endregion

    //region Constractors
    public User() {
    }

    public User(String first, String last, String email, String department, boolean isAdmin, boolean isManager, String shift) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.uid = uid;
        this.department = department;
        this.isAdmin = isAdmin;
        this.isManager = isManager;
        this.shift = shift;
    }

//endregion

    public String getDisplayName() {
        return first+" "+last;
    }

    //region Getters
    public String getUid() {
        return uid;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isManager() {
        return isManager;
    }

    public String getShift() {
        return shift;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }
//endregion

    //region Setters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setFirst(String first) {
        this.first = first;
    }


    public void setLast(String last) {
        this.last = last;
    }


    public void setEmail(String email) {
        this.email = email;
    }
//endregion
}
