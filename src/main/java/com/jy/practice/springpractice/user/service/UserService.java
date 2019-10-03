package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
