package ru.home.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.home.mvc.dao.UsersDao;
import ru.home.mvc.forms.UserForm;
import ru.home.mvc.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    @Autowired
    private UsersDao usersDao;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ModelAndView getAllUsers(@RequestParam(value = "first_name", required = false) String firstName) {
        List<User> users;
        if (firstName != null && !firstName.equals(""))
            users = usersDao.findAllByFirstName(firstName);
        else
            users = usersDao.findAll();
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", users);
        return modelAndView;
    }

    @RequestMapping(path = "/users/{user-id}", method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable("user-id") Long userId) {
        Optional<User> userCandidate = Optional.empty();
        if (userId > 0)
            userCandidate = usersDao.find(userId);
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", userCandidate.isPresent() ? Collections.singletonList(userCandidate.get()) : new ArrayList<>());
        return modelAndView;
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public String addUser(UserForm userForm) {
        User newUser = User.fromForm(userForm);
        usersDao.save(newUser);
        return "redirect:/users";
    }
}
