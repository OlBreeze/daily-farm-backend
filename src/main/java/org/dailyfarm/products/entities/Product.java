package org.dailyfarm.products.entities;

import java.util.UUID;

import org.dailyfarm.security.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // генерим автоматически (UUID, IDENTITY чаще всего)
	private UUID id;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "id_user")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER) // LAZY  не будет подгружаться сразу (оптимизация)??
	@JoinColumn(name = "id_category")
	private Category category;
	
	@ManyToOne(fetch = FetchType.EAGER) // EAGER-сразу тянет
	@JoinColumn(name = "id_unit")
	private Unit unit;
	
	@Column(name = "name", columnDefinition = "varchar(255)")
	private String name;
	
	private Double price;
	private Double quantity;
	
	@Column(name = "comment", columnDefinition = "varchar(255)")
	private String comment;
}
