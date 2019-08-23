package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;

import java.util.List;

public class UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 회원 등급 업데이트
     */
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        users.forEach(user -> {
            Boolean changed = null;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else {
                changed = false;
            }

            if(changed) {
                userDao.update(user);
            }
        });
    }

    /**
     * 회원 등록
     * @param user
     */
    public void add(User user) {
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);

        userDao.add(user);
    }
}
