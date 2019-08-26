package com.jy.practice.springpractice.user.test;

import com.jy.practice.springpractice.user.domain.Level;
import com.jy.practice.springpractice.user.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), CoreMatchers.is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if (level.nextLevel() != null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }

}
