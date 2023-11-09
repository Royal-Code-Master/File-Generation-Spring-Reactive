package com.money.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.app.pojo.User;
import com.money.app.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bank")
public class UserController {

	@Autowired
	private UserService userService;

	// account creation end point
	@PostMapping("/newAccount")
	public Mono<User> newAccountEndPoint(@RequestBody User user) {
		return userService.createAccount(user).doOnSuccess(this::sendAlertMessage);
	}

	// delete account by id
	@DeleteMapping("/deleteUser/{uid}")
	public Mono<Void> deleteAccountEndPoint(@PathVariable long uid) {
		return userService.deleteAccount(uid).doOnSuccess(v -> System.out.println("Your Account Successfully Deleted"));
	}

	// update account by id
	@PutMapping("/updateUser")
	public Mono<User> updateAccountEndPoint(@RequestBody User user) {
		return userService.updateAccount(user).doOnSuccess(this::updateAlertMessage);
	}

	// get all users
	@GetMapping("/allUsers")
	public Flux<User> getAllUserDetailsEndPoint() {
		return userService.allUsers();
	}

	// success message
	private void sendAlertMessage(User user) {
		System.out.println(user.getFullName() + " Your Account Successfully Created ");
	}

	// update success message
	private void updateAlertMessage(User user) {
		System.out.println(user.getFullName() + " Your Account Successfully Updated ");
	}

}
