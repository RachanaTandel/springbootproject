package com.apex.springbootdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apex.springbootdemo.bean.User;
import com.apex.springbootdemo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/message")
	public String getDemo() {
		return "Welcome to Spring Boot Demo";
	}

	@GetMapping("/user")
	public User getUser(@RequestParam("id") int id) {
		return userService.getUser(id);
	}
	
	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}
	
	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@PutMapping
	public User updateUser(@RequestBody User user) {
		// Updating
		return userService.updateUser(user);
	}
	
	@DeleteMapping
	public String deleteUser(@RequestParam("id") int id) {
		return userService.deleteUser(id);
	}
}