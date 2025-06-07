package org.dailyfarm.products.entities;


import java.util.UUID;

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
@Table(name = "categories")
public class Category {
	// при удалении родителя проверять - есть ли дети!! если есть - отказ
	
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY) //LAZY — чтобы не грузить родителя сразу.
    @JoinColumn(name = "id_parentCategory")
    private Category parentCategory; //В JPA правильнее ссылаться на объект (а не на UUID напрямую). Hibernate сам свяжет по id.

//    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
//    private Set<Category> subCategories = new HashSet<>();  // подкатегории

}
