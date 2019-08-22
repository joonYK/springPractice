package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.dao.UserDao;

public class UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


}
