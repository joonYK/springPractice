package com.jy.practice.springpractice.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
//        userDao.setDataSource(connectionMaker());
        return userDao;
    }


    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMarker(realConnectionMarker());
    }

    @Bean
    public ConnectionMaker realConnectionMarker() {
        return new DConnectionMaker();
    }
}
