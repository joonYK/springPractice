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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User> users;

    @Autowired
    private MailSender mailSender;

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equalsIgnoreCase(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    // 메일 전송 확인용 목 오브젝트 (메일전송 파라미터에 대한 확인을 할 수 있다.)
    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        private List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }


    @Before
    public void serUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, userLevelUpgradePolicy.getMinLogcountForSilver()-1, 0, "aaa@aaa.com"),
                new User("joytouch", "강명성", "p2", Level.BASIC, userLevelUpgradePolicy.getMinLogcountForSilver(), 0, "bbb@aaa.com"),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, userLevelUpgradePolicy.getMinRecommendForGold()-1, "ccc@aaa.com"),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, userLevelUpgradePolicy.getMinRecommendForGold(), "ddd@aaa.com"),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "eee@aaa.com"));
    }

    @Test
    public void bean() {
        Assert.assertThat(this.userService, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() {
        userDao.deleteAll();
        users.forEach(u -> userDao.add(u));

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        try {
            userService.upgradeLevels();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();

        Assert.assertThat(requests.size(),CoreMatchers.is(2));
        Assert.assertThat(requests.get(0),CoreMatchers.is(users.get(1).getEmail()));
        Assert.assertThat(requests.get(1),CoreMatchers.is(users.get(3).getEmail()));

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

    @Test
    public void upgradeAllOrNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
        testUserService.setDataSource(this.dataSource);
        testUserService.setTransactionManager(transactionManager);

        userDao.deleteAll();
        for(User user : users)
            userDao.add(user);

        try {
            userServiceTx.upgradeLevels();
            Assert.fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLevelUpgraded(users.get(1), false);
    }
}
