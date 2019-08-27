package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

    private int minLogcountForSilver = 50;
    private int minRecommendForGold = 30;

    @Override
    public int getMinLogcountForSilver() {
        return minLogcountForSilver;
    }

    @Override
    public int getMinRecommendForGold() {
        return minRecommendForGold;
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC : return (user.getLogin() >= minLogcountForSilver);
            case SILVER : return (user.getRecommend() >= minRecommendForGold);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

}
