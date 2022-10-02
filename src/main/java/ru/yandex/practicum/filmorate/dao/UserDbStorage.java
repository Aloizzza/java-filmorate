package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private int userId = 0;
    private static final String SQL_INS_USERS =
            "INSERT INTO users(id_user,name,login,email,birthday) values(?,?,?,?,?)";
    private static final String SQL_UPD_USERS = "UPDATE users SET name=?,login=?,email=?,birthday=? WHERE id_user=?";
    private static final String SQL_DEL_USERS = "DELETE FROM users WHERE id_user=?";
    private static final String SQL_DEL_USERS_ALL = "DELETE FROM users";
    private static final String SQL_INS_FRIENDS = "INSERT INTO friends(id_user,id_friend,status) values(?,?,1)";
    private static final String SQL_DEL_FRIENDS = "DELETE FROM friends WHERE id_user=? and id_friend=?";

    private int getUserId() {
        return ++userId;
    }

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getById(Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("id должен быть >0");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            return Optional.of(user);
        } else {
            throw new NotFoundException("По указанному id не найден пользователь");
        }

    }

    @Override
    public Optional<User> create(User user) {

        if (user.getName().equals("") || user.getName() == null) {
            user.setName(user.getLogin());
        }

        int id = getUserId();
        jdbcTemplate.update(SQL_INS_USERS, id, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT U.ID_USER,F.ID_FRIEND FROM USERS U "
                        + "LEFT JOIN FRIENDS F on u.ID_USER = F.ID_FRIEND "
                        + "WHERE U.ID_USER=?"
                , id);
        while (userRow.next()) {
            user.setId(userRow.getInt("id_user"));
            user.addFriend(userRow.getInt("id_friend"));
        }

        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        if (user.getId() <= 0 || user.getId() == null || getById(user.getId()).isEmpty()) {
            throw new NotFoundException("id должен быть > 0");
        }
        jdbcTemplate.update(SQL_UPD_USERS, user.getName(), user.getLogin(), user.getEmail()
                , user.getBirthday(), user.getId());
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DEL_USERS, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(SQL_DEL_USERS_ALL);
    }

    @Override
    public void addFriend(int id, int idFriend) {
        jdbcTemplate.update(SQL_INS_FRIENDS, id, idFriend);
    }

    @Override
    public void deleteFriend(int id, int idFriend) {
        jdbcTemplate.update(SQL_DEL_FRIENDS, id, idFriend);
    }

    @Override
    public List<User> getFriends(int id) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT u.* FROM friends f  "
                + "JOIN users u on f.id_friend=u.id_user "
                + "WHERE f.status=1 and f.id_user=?", id);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getCommonFriends(int id, int id2) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user IN "
                + "(SELECT id_friend FROM friends WHERE id_user=? AND id_friend IN "
                + "(SELECT id_friend FROM friends WHERE id_user=?))", id, id2);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }
}