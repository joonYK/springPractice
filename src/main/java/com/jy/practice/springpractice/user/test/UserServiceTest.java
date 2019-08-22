package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;
import com.jy.practice.springpractice.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @Before
    public void serUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100));
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

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);

    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(expectedLevel));
    }
}
