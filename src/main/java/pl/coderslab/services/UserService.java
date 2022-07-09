package pl.coderslab.services;

import pl.coderslab.User;

public interface UserService {

    User findByUserName(String name);

    void saveUser(User user);


}