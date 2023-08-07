package com.kinduberre.chama.models;

import java.sql.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {
	
	@Id
	private Long id;
	private Date tranDate;
	private String reference;
	private Double amount;
	private Double charge;
	private Double tax;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tran_type_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TransactionType tranType;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="loan_id", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Loan loan;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="contribution_id", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Contribution contribution;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="account_id", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Account account;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "channel_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Channel channel;
	
	
	
	
}
