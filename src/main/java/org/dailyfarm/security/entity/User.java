package org.dailyfarm.security.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.dailyfarm.products.entities.Product;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(updatable = false, unique = true)
	private String username;
	
	private String password;
	private String email;
	private Boolean client;
	private Boolean company;
	private LocalDate dateCreate;
	private LocalDate lastDateUpdate;
	private String address;
	private String phone;
	private boolean revoked;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		        name = "users_roles",
		        joinColumns = @JoinColumn(name = "user_id"),
		        inverseJoinColumns = @JoinColumn(name = "role_id")
		    )
	private Set<Role> roles = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	        name = "users_products",
	        joinColumns = @JoinColumn(name = "user_id"),
	        inverseJoinColumns = @JoinColumn(name = "product_id")
	    )
	private Set<Product> products = new HashSet<>();

	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }

	public LocalDate getDateCreate() { return dateCreate; }
	public void setDateCreate(LocalDate dateCreate) { this.dateCreate = dateCreate; }

}
