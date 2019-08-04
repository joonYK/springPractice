package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.junit.Assert.assertThat;

public class UserDaoTest {

    private UserDao dao;

    @Before
    public void setUp() {
        ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
        dao = context.getBean("userDao", UserDao.class);
    }

    @Test
    public void addAndGet() throws SQLException {

        User user1 = new User("gyumee", "박성철", "springno1");
        User user2 = new User("leegw700", "이길원", "springno2");

        dao.deleteAll();
        assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), CoreMatchers.is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), CoreMatchers.is(user1.getName()));
        assertThat(userget1.getPassword(), CoreMatchers.is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), CoreMatchers.is(user2.getName()));
        assertThat(userget2.getPassword(), CoreMatchers.is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        User user1 = new User("gyumee", "박성철", "springno1");
        User user2 = new User("leegw700", "이길원", "springno2");
        User user3 = new User("bumjin", "박범진", "springno3");

        dao.deleteAll();
        assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        assertThat(dao.getCount(), CoreMatchers.is(1));

        dao.add(user2);
        assertThat(dao.getCount(), CoreMatchers.is(2));

        dao.add(user3);
        assertThat(dao.getCount(), CoreMatchers.is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.get("unknown_id");
    }
}
