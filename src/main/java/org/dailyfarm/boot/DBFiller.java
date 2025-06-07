package org.dailyfarm.boot;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.dailyfarm.products.entities.Category;
import org.dailyfarm.products.entities.Unit;
import org.dailyfarm.products.repo.CategoriesRepository;
import org.dailyfarm.products.repo.UnitsRepository;
import org.dailyfarm.security.entity.Role;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.security.repository.RoleRepository;
import org.dailyfarm.security.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DBFiller implements ApplicationRunner {

	private final RoleRepository roleRepository;
	private final CategoriesRepository categoriesRepository;
	private final UnitsRepository unitRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// Necessary part - filling
		if (categoriesRepository.count() == 0) {
			Category root = Category.builder().name("Root").build();
			Category meat = Category.builder().name("Meat").parentCategory(root).build();
			Category dairy = Category.builder().name("Dairy").parentCategory(root).build();
			categoriesRepository.save(root);
			categoriesRepository.save(meat);
			categoriesRepository.save(dairy);
		}

		if (unitRepository.count() == 0) {
			Unit unit1 = Unit.builder().name("kg").build();
			Unit unit2 = Unit.builder().name("unit").build();

			unitRepository.save(unit1);
			unitRepository.save(unit2);
		}
		// ----
//		Product product = new Product(1, 2, 3, "Kg", 10.);
//		availiableProductsRepo.save(product);
		//System.out.println(productRepository.findAll());

		
		if (roleRepository.count() == 0) {
//			Role user = new Role(UUID.randomUUID(), "USER");
//		    Role admin = new Role(UUID.randomUUID(), "ADMIN");
//		    Role manager = new Role(UUID.randomUUID(), "MANAGER");

			Role user = new Role(); 		user.setName("USER");
		    Role admin = new Role(); 	admin.setName("ADMIN");
		    Role manager = new Role(); 	manager.setName("MANAGER");
		    
		    roleRepository.saveAll(List.of(user, admin, manager));
		}

		if (userRepository.count() == 0) {
			User admin = new User();
		    User client1 = new User();
		    User client2 = new User();
		    User company1 = new User();
		    User company2 = new User();
		    User company3 = new User();

		    // 1. Admin (роль ADMIN)
		    userRepository.save(initUser(admin, "admin", false, "ADMIN"));

		    // 2. Клиенты (роль USER)
		    userRepository.save(initUser(client1, "client1", false, "USER"));
		    userRepository.save(initUser(client2, "client2", false, "USER"));

		    // 3. Компании (роли USER + MANAGER)
		    userRepository.save(initUser(company1, "company1", true, "USER", "MANAGER"));
		    userRepository.save(initUser(company2, "company2", true, "USER", "MANAGER"));
		    userRepository.save(initUser(company3, "company3", true, "USER", "MANAGER"));
		}
	}

	private User initUser(User user, String username, Boolean company, String... roles) {
	   // user.setId(UUID.randomUUID());
	    user.setUsername(username);
	    
	    // Получаем все роли из БД
	    Set<Role> userRoles = new HashSet<>();
	    for (String roleName : roles) {
	        userRoles.add(roleRepository.findByName(roleName));
	    }
	    user.setRoles(userRoles);
	    
	    user.setClient(!company);
	    user.setCompany(company);
	    user.setAddress("Address unknown");
	    user.setDateCreate(LocalDate.now());
	    user.setPhone("0542223355");
	    user.setPassword(passwordEncoder.encode("qwerty"));
	    return user;
	}

}
