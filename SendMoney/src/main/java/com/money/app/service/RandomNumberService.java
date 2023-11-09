package com.money.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.money.app.pojo.RandomNumber;
import com.money.app.repo.RandomNumberRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RandomNumberService {

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	private RandomNumberRepo randomNumberRepo;

	public Flux<RandomNumber> getAllDocuments() {
		return reactiveMongoTemplate.findAll(RandomNumber.class);
	}

	public Mono<String> exportDocumentsToCSV() {
		Flux<RandomNumber> documentFlux = getAllDocuments();

		return documentFlux.collectList().flatMap(cards -> {
			try {
				String filePath = "cards.csv";
				File file = new File(filePath);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				FileChannel channel = fileOutputStream.getChannel();

				// Writing the header line
				String header = "Card_Numbers\n";
				ByteBuffer headerBuffer = ByteBuffer.wrap(header.getBytes());
				channel.write(headerBuffer);

				// Writing document data
				for (RandomNumber randomNumber : cards) {
					String line = String.format("%d\n", randomNumber.getCardNumber());
					ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());
					channel.write(buffer);
				}
				channel.close();
				fileOutputStream.close();
				return Mono.just("CSV file generated successfully");
			} catch (IOException e) {
				return Mono.error(new RuntimeException("Error while generating CSV file", e));
			}
		});
	}

	// creating bulk of random card numbers
	public Flux<RandomNumber> createNumber(RandomNumber randomNumber) {
		long minNumber = 1000000000L;
		long maxNumber = 9999999999L;
		int number_of_cards = 1000;

		return Flux.range(0, number_of_cards).map(i -> {
			long random_Number = (long) (Math.random() * (maxNumber - minNumber)) + minNumber;
			return new RandomNumber(random_Number);
		}).flatMap(randomNumberRepo::insert);
	}

}
