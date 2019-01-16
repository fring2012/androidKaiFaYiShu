package com.study.czq.androidKaiFaYiShu.entity;

public class User {
    public int userId;
    public String userName;
    public boolean isMale;

    @Override
    public String toString() {
        return "{" +
                "userId:" + userId + "," +
                "userName:" + userName + "," +
                "isMale:" + isMale +
                "}";
    }
}
