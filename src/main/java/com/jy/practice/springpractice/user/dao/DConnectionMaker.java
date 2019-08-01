package com.jy.practice.springpractice.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/springtest?characterEncoding=UTF-8&serverTimezone=UTC", "root", "1234");
        return c;
    }
}
