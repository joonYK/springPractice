package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;
import com.jy.practice.springpractice.user.service.UserLevelUpgradePolicy;
import com.jy.practice.springpractice.user.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    private List<User> users;

    @Before
    public void serUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, userLevelUpgradePolicy.getMinLogcountForSilver()-1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, userLevelUpgradePolicy.getMinLogcountForSilver(), 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, userLevelUpgradePolicy.getMinRecommendForGold()-1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, userLevelUpgradePolicy.getMinRecommendForGold()),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE));
    }

    @Test
    public void bean() {
        Assert.assertThat(this.userService, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        users.forEach(u -> userDao.add(u));

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded)
            Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(user.getLevel().nextLevel()));
        else
            Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(user.getLevel()));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        Assert.assertThat(userWithLevelRead.getLevel(), CoreMatchers.is(userWithLevel.getLevel()));
        Assert.assertThat(userWithoutLevelRead.getLevel(), CoreMatchers.is(userWithoutLevel.getLevel()));
    }
}
