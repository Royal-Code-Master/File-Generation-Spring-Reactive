package com.money.app.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "cards")
public class RandomNumber {
	
	@Id
	private long cardNumber;

	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public RandomNumber(long cardNumber) {
		super();
		this.cardNumber = cardNumber;
	}

	public RandomNumber() {
		super();
	}

}
