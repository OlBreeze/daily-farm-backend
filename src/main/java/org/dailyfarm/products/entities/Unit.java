package org.dailyfarm.products.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Table(name = "units")
public class Unit {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // генерим автоматически (UUID, IDENTITY чаще всего)
	private UUID id;
	private String name;
}
