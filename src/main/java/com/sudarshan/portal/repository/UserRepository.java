package com.sudarshan.portal.repository;

import com.sudarshan.portal.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class UserRepository {

    private Set<User> users;


    public UserRepository() {
        this.users = new HashSet<>();
    }

    public User saveUser(User user) {
        if (users.add(user)) {
            return user;
        } else {
            return null;
        }
    }

    public User getUserByPhone(String phone) {
            for(User user: users) {
                if (user.phoneNumber().equals(phone)) {
                    return user;
                }
            }

        return null;
    }
}
