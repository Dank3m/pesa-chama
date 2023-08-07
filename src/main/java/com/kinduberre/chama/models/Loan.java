package com.kinduberre.chama.models;

import java.sql.Date;

import com.kinduberre.chama.models.auth.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "loan")
public class Loan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date startDate;
	private Date repayDate;
	private Double amount;
	private Date maturityDate;
	private String status;
	private Double balance;
	private String purpose;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="loan_type_id")
	private LoanType loanType;
	
	@ManyToOne
	@JoinColumn(name="guarantee_id")
	private Guarantee guarantee;
	
	private int duration;
	

	
	
}
