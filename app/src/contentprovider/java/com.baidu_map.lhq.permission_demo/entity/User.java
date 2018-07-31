package com.baidu_map.lhq.permission_demo.entity;

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
