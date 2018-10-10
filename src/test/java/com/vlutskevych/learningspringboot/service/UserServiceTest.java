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
        List<User> allUsers = userService.getAllUsers();
        assertThat(allUsers).hasSize(1);

        User user = users.get(0);
        assertUserFields(user);
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
        assertUserFields(user);
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
        assertUserFields(user);
        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void insertUser() {
    }

    private void assertUserFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getFirstName()).isEqualTo("anna");
        assertThat(user.getLastName()).isEqualTo("montana");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("anna@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }
}