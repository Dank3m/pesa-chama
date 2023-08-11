package com.kinduberre.chama.models.auth;

import java.util.Date;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String idNo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	@NotNull(message="Password is compulsory")
	@Length(min=5, message="Password should be at least 5 characters")
	@Column(name = "user_password")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@Column(name = "user_status")
	private String status;
	
	@Column(name = "reset_password")
	private String resetPassword;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
}
