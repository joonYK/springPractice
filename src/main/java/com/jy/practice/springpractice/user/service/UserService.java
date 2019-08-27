package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;

import java.util.List;

public class UserService {

    private UserDao userDao;

    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    /**
     * 회원 등급 업데이트
     */
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        users.forEach(user -> {
            if(canUpgradeLevel(user))
                upgradeLevel(user);
        });
    }

    /**
     * 회원 등급 업그레이드 가능한지 체크
     * @param user
     * @return
     */
    private boolean canUpgradeLevel(User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
    }

    /**
     * 회원 등급 업그레이드
     * @param user
     */
    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
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
