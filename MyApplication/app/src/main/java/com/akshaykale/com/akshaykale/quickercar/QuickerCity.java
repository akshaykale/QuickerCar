package com.akshaykale.com.akshaykale.quickercar;

/**
 * Created by Akshay on 8/22/2015.
 */
public class QuickerCity {

    private int users;
    private String city;

    public QuickerCity(){
        users = 0;
        city = "";
    }

    public QuickerCity(int users, String city) {
        this.users = users;
        this.city = city;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
