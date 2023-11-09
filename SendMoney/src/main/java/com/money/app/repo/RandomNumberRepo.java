package com.money.app.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.money.app.pojo.RandomNumber;

@Repository
public interface RandomNumberRepo extends ReactiveMongoRepository<RandomNumber, Long>{

}
