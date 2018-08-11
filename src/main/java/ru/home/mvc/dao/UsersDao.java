package ru.home.mvc.dao;

import ru.home.mvc.models.User;

import java.util.List;

public interface UsersDao extends CrudDao<User> {
    List<User> findAllByFirstName(String firstName);
}
