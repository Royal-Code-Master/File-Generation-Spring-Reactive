package com.money.app.service;

import org.springframework.stereotype.Service;

import com.money.app.pojo.Documents;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Service
public class DocumentService {

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	public Flux<Documents> getAllDocuments() {
		return reactiveMongoTemplate.findAll(Documents.class);
	}
	
	public Mono<String> exportDocumentsToCSV() {
		Flux<Documents> documentFlux = getAllDocuments();

		return documentFlux.collectList().flatMap(documents -> {
			try {
				String filePath = "documents.csv";
				File file = new File(filePath);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				FileChannel channel = fileOutputStream.getChannel();

				// Writing the header line
				String header = "UID, Account_Number, Amount, Full_Name, Email_Address, Phone_Number,Age\n";
				ByteBuffer headerBuffer = ByteBuffer.wrap(header.getBytes());
				channel.write(headerBuffer);

				// Writing document data
				for (Documents document : documents) {
					String line = String.format("%d,%s,%f,%s,%s,%s,%d\n", document.getUid(), document.getAccountNumber(),
							document.getAmount(), document.getFullName(), document.getEmailAddress(),
							document.getPhoneNumber(), document.getAge());
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

}
