package com.vlutskevych.learningspringboot.service;

import com.vlutskevych.learningspringboot.dao.FakeDataDao;
import com.vlutskevych.learningspringboot.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


public class UserServiceTest {

    @Mock
    private FakeDataDao fakeDataDao;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    public void shouldGetAllUsers() {
        User anna = new User(UUID.randomUUID(), "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        List<User> users = new ArrayList<User>(){{add(anna);}};
        List<User> immutableUsers = Collections.unmodifiableList(users);

        given(fakeDataDao.selectAllUsers()).willReturn(immutableUsers);
        List<User> allUsers = userService.getAllUsers(Optional.empty());
        assertThat(allUsers).hasSize(1);

        User user = users.get(0);
        assertAnnaFields(user);
    }

    @Test
    public void shouldGetAllUsersByGender() {
        User anna = new User(UUID.randomUUID(), "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        User mike = new User(UUID.randomUUID(), "mike", "mitchel",
                User.Gender.MALE, 30, "mmike@gmail.com");
        List<User> users = new ArrayList<User>(){{add(anna); add(mike);}};
        List<User> immutableUsers = Collections.unmodifiableList(users);

        given(fakeDataDao.selectAllUsers()).willReturn(immutableUsers);
        List<User> filteredUsers = userService.getAllUsers(Optional.of("female"));
        assertThat(filteredUsers).hasSize(1);
        assertAnnaFields(filteredUsers.get(0));
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsInvalid() {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("aaaa")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid gender");
    }

    @Test
    public void shouldGetUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));

        Optional<User> optionalUser = userService.getUser(annaUid);
        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        assertAnnaFields(user);
    }

    @Test
    public void shouldUpdateUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.updateUser(anna)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(anna);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).updateUser(captor.capture());

        User user = captor.getValue();
        assertAnnaFields(user);
        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    public void shouldRemoveUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.deleteUserByUserUid(annaUid)).willReturn(1);

        int deleteResult = userService.removeUser(annaUid);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).deleteUserByUserUid(annaUid);

        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    public void shouldInsertUser() {
        UUID userUid = UUID.randomUUID();
        User anna = new User(userUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        int insertResult = userService.insertUser(anna);
        verify(fakeDataDao).insertUser(eq(userUid), captor.capture());
        User user = captor.getValue();
        assertAnnaFields(user);
        assertThat(insertResult).isEqualTo(1);
    }

    private void assertAnnaFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getFirstName()).isEqualTo("anna");
        assertThat(user.getLastName()).isEqualTo("montana");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("anna@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
        assertThat(user.getUserUid()).isInstanceOf(UUID.class);
    }
}