package com.money.app.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.money.app.pojo.User;




@Repository
public interface UserRepo extends ReactiveMongoRepository<User, Long>{
	
}
