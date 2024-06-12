package com.apex.springbootdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apex.springbootdemo.bean.User;
import com.apex.springbootdemo.dao.UserDAO;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User addUser(User user) {
		return userDAO.addUser(user) >=0 ? user : null;
	}

	public List<User> getUsers() {
		return userDAO.getUsers();
	}

	public String deleteUser(int id) {
		return userDAO.deleteUser(id) > 0 ? "User Deleted" : "Error in Deleting the User";
	}

	public User getUser(int id) {
		return userDAO.getUser(id);
	}

	public User updateUser(User user) {
		return userDAO.updateUser(user);
	}
}