package com.demo.springtask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	Long id;
	@Column(name = "EMAIL", nullable = false)
	String email;
	@JsonIgnore
	@Column(name = "PASS", nullable = false)
	String password;
	@Column(name = "NAME", nullable = false)
	String name;
	@Column(name = "DEV", nullable = false)
	String role;

}
