package com.jy.practice.springpractice.user.service;

import com.jy.practice.springpractice.user.dao.UserDao;
import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserService {

    private UserDao userDao;

    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    private DataSource dataSource;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 회원 등급 업데이트
     */
    public void upgradeLevels() throws Exception {
        //트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화.
        TransactionSynchronizationManager.initSynchronization();
        //DB커넥션 생성, 트랜잭션 시작 (커넥션 생성과 동기화를 함께 해주는 유틸리티 메소드)
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            users.forEach(user -> {
                if (canUpgradeLevel(user))
                    upgradeLevel(user);
            });
            c.commit();
        } catch (Exception e) {
            //예외 발생시 롤백
            c.rollback();
            throw e;
        } finally {
            //DB 커넥션 닫음
            DataSourceUtils.releaseConnection(c, dataSource);
            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    /**
     * 회원 등급 업그레이드 가능한지 체크
     * @param user
     * @return
     */
    private boolean canUpgradeLevel(User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
    }

    /**
     * 회원 등급 업그레이드
     * @param user
     */
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    /**
     * 회원 등록
     * @param user
     */
    public void add(User user) {
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);

        userDao.add(user);
    }
}
