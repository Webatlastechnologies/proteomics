package com.vkhatri.modal;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String benificary;
	private Double amount;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;
	
	public Transaction() {}

    public Transaction(String benificary, double amount) {
        this.benificary = benificary;
        this.amount = amount;
    }
	
	@Override
    public String toString() {
        return String.format(
                "Transaction[id=%d, benificary='%s', amount='%d']",
                id, benificary, amount);
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBenificary() {
		return benificary;
	}

	public void setBenificary(String benificary) {
		this.benificary = benificary;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
