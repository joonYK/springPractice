package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class) //Junit 확장 기능. 해당 클래스를 지정해주면 테스트에 사용할 컨텍스트를 만들고 관리
@ContextConfiguration(locations = "/test-applicationContext.xml") //컨텍스트 설정파일 지정
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("gyumee", "박성철", "springno1");
        user2 = new User("leegw700", "이길원", "springno2");
        user3 = new User("bumjin", "박범진", "springno3");
    }

    @Test
    public void addAndGet() throws SQLException {
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
