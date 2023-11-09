package com.money.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.app.pojo.RandomNumber;
import com.money.app.service.RandomNumberService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cards")
public class RandomNumberController {
	
	@Autowired
	private RandomNumberService randomNumberService;
	
	
	@GetMapping("/carddata")
    public Mono<String> exportDocumentsToCSV() {
        return randomNumberService.exportDocumentsToCSV();
        
    }
	
	@GetMapping("/create")
	public Flux<RandomNumber> createNewNumber(RandomNumber randomNumber){
		return randomNumberService.createNumber(randomNumber);
	}
	
}
