package com.vlutskevych.learningspringboot.dao;

import com.vlutskevych.learningspringboot.model.User;
import com.vlutskevych.learningspringboot.model.User.Gender;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @Before
    public void setUp() throws Exception {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    public void shouldSelectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("joe.jones@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    public void shouldSelectUserByUserUid() {
        UUID userId = UUID.randomUUID();
        User anna = new User(userId, "anna", "montana",
                Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.insertUser(anna);
        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> annaOptional = fakeDataDao.selectUserByUserUid(userId);
        assertThat(annaOptional.isPresent()).isTrue();
        assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);

    }

    @Test
    public void shouldUpdateUser() {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        User newJoe = new User(joeUserUid, "newJoe", "montana",
                Gender.MALE, 25, "newJoe@gmail.com");
        fakeDataDao.updateUser(newJoe);

        Optional<User> user = fakeDataDao.selectUserByUserUid(joeUserUid);
        assertThat(user.isPresent()).isTrue();

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(user.get()).isEqualToComparingFieldByField(newJoe);
    }

    @Test
    public void shouldDeleteUserByUserUid() {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        fakeDataDao.deleteUserByUserUid(joeUserUid);

        assertThat(fakeDataDao.selectUserByUserUid(joeUserUid).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }

    @Test
    public void shouldInsertUser() {
        UUID userId = UUID.randomUUID();
        User alex = new User(userId, "alex", "montana",
                Gender.MALE, 35, "alex@gmail.com");
        fakeDataDao.insertUser(alex);

        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
        assertThat(fakeDataDao.selectUserByUserUid(userId).get()).isEqualToComparingFieldByField(alex);
    }
}