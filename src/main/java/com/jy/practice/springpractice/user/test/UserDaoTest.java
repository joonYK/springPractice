package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class) //Junit 확장 기능. 해당 클래스를 지정해주면 테스트에 사용할 컨텍스트를 만들고 관리
@ContextConfiguration(locations = "/applicationContext.xml") //컨텍스트 설정파일 지정
/**
 * 테스트 메소드에서 컨텍스트의 구성이나 상태를 변경한다는것을 컨텍스트 프레임워크에 알려줌.
 * 이 테스트클래스는 테스트 실행시 매번 새 컨텍스트를 생성. 공유 X
 */
@DirtiesContext
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        //테스트에서 UserDao가 사용할 dataSource 오브젝트를 직접 생성.
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/springtest?characterEncoding=UTF-8&serverTimezone=UTC", "root", "1234", true);
        dao.setDataSource(dataSource);

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
