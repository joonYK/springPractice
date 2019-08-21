package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDaoJdbc;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class) //Junit 확장 기능. 해당 클래스를 지정해주면 테스트에 사용할 컨텍스트를 만들고 관리
@ContextConfiguration(locations = "/applicationContext.xml") //컨텍스트 설정파일 지정
public class UserDaoTest {

    @Autowired
    private UserDaoJdbc dao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 5, 10);
        user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 100);
    }

    @Test
    public void addAndGet() {
        dao.deleteAll();
        assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), CoreMatchers.is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test
    public void count() {
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
    public void getUserFailure() {
        dao.deleteAll();
        assertThat(dao.getCount(), CoreMatchers.is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), CoreMatchers.is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), CoreMatchers.is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), CoreMatchers.is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), CoreMatchers.is(3));
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), CoreMatchers.is(user2.getId()));
        assertThat(user1.getName(), CoreMatchers.is(user2.getName()));
        assertThat(user1.getPassword(), CoreMatchers.is(user2.getPassword()));
        assertThat(user1.getLevel(), CoreMatchers.is(user2.getLevel()));
        assertThat(user1.getLogin(), CoreMatchers.is(user2.getLogin()));
        assertThat(user1.getRecommend(), CoreMatchers.is(user2.getRecommend()));
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException)e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx), CoreMatchers.instanceOf(DuplicateKeyException.class));

        }
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1update, user1);
    }
}
