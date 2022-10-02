package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        User user1 = new User(1, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));
        User user2 = new User(2, "user2@example", "User2"
                , "user2", LocalDate.of(2000, 2, 1));
        userStorage.create(user1);
        userStorage.create(user2);
    }

    @Test
    public void shouldFindUserById() {
        Optional<User> userOptional = userStorage.getById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> users = userStorage.findAll();
        assertThat(users.size())
                .isEqualTo(2);
    }
}