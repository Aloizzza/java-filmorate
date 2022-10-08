package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private int userId = 0;

    private int getUserId() {
        return ++userId;
    }

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public User create(User user) {

        if (user.getName().equals("") || user.getName() == null) {
            user.setName(user.getLogin());
        }

        int id = getUserId();
        jdbcTemplate.update("INSERT INTO users(id_user, name, login, email, birthday) values(?,?,?,?,?)"
                , id, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT u.id_user,f.id_friend " +
                "FROM users u "
                + "LEFT JOIN friends f on u.id_user = F.id_friend "
                + "WHERE u.id_user=?", id);
        while (userRow.next()) {
            user.setId(userRow.getInt("id_user"));
            user.addFriend(userRow.getInt("id_friend"));
        }
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET name=?,login=?,email=?,birthday=? " +
                        "WHERE id_user=?", user.getName(), user.getLogin(), user.getEmail(), user.getBirthday()
                , user.getId());

        return getById(user.getId()).get();
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
        jdbcTemplate.update("DELETE FROM users WHERE id_user=?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM friends; DELETE FROM rating; DELETE FROM users");
        userId = 0;
    }

    @Override
    public void addFriend(int id, int idFriend) {
        jdbcTemplate.update("INSERT INTO friends(id_user,id_friend,status) values(?,?,1)", id, idFriend);
    }

    @Override
    public void deleteFriend(int id, int idFriend) {
        jdbcTemplate.update("DELETE FROM friends WHERE id_user=? and id_friend=?", id, idFriend);
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