package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.domain.User;

public interface UserLevelUpgradePolicy {

    int getMinLogcountForSilver();

    int getMinRecommendForGold();

    boolean canUpgradeLevel(User user);
}
