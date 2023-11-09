package com.money.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.app.pojo.User;
import com.money.app.repo.UserRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	// create new account
	public Mono<User> createAccount(User user) {
		long userId = user.getUid();
		return userRepo.findById(userId).flatMap(existingUser -> {
			if (existingUser.getAccountNumber().equals(user.getAccountNumber())
					|| existingUser.getEmailAddress().equals(user.getEmailAddress())
					|| existingUser.getPhoneNumber().equals(user.getPhoneNumber())) {
				String errorMessage = "User with the same ID or account number, or email or phone number already exists.";
				System.err.println(errorMessage);
				return Mono.error(new RuntimeException(errorMessage));
			} else {
				return userRepo.insert(user);
			}
		}).switchIfEmpty(userRepo.insert(user)).onErrorResume(throwable -> {
			if (throwable instanceof RuntimeException) {
				String errorMessage = "User with the same ID, account number, or email already exists.";
				System.err.println(errorMessage);
			}

			return Mono.error(throwable);
		});
	}

	// delete user account by id

	public Mono<Void> deleteAccount(long uid) {
		return userRepo.findById(uid)
				.switchIfEmpty(Mono.error(new RuntimeException("User with ID " + uid + " does not exist to delete.")))
				.flatMap(existingUser -> {
					return userRepo.deleteById(uid);
				});
	}

	// update user account data by id

	public Mono<User> updateAccount(User user) {
		long userId = user.getUid();
		return userRepo.findById(userId)
				.switchIfEmpty(Mono.error(new RuntimeException("User with ID " + userId + " does not exist.")))
				.flatMap(existingUser -> {
					existingUser.setAccountNumber(user.getAccountNumber());
					existingUser.setFullName(user.getFullName());
					existingUser.setEmailAddress(user.getEmailAddress());
					existingUser.setPhoneNumber(user.getPhoneNumber());
					existingUser.setAge(user.getAge());
					return userRepo.save(existingUser);
				});
	}

	// get All user details
	public Flux<User> allUsers() {
		return userRepo.findAll().flatMap(user -> Flux.just(user));
	}

}
