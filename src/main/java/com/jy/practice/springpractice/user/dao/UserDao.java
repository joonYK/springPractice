package com.jy.practice.springpractice.user.dao;

import com.jy.practice.springpractice.user.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();

    void update(User user1);
}
