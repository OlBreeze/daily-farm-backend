package org.dailyfarm.products.repo;

import java.util.UUID;

import org.dailyfarm.products.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRepository extends JpaRepository<Product, UUID>{

	Product findByName(String name);

	@Query("""
		    SELECT p FROM Product p
		    WHERE (:search IS NULL OR :search = ''
		        OR LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
		        OR LOWER(comment) LIKE LOWER(CONCAT('%', :search, '%'))
		        )
		""")
		Page<Product> findProducts(@Param("search") String search, Pageable pageRequest);

}

//RESULT
//
//"content": [
//            {
//                "id": "0ece1e4d-425a-4847-8bd4-8bbe04d8632c",
//                "userId": null,
//                "username": null,
//                "categoryId": "3f24e2a3-a223-48f7-a776-278550e1edf0",
//                "categoryName": "Meat",
//                "unitId": null,
//                "unitName": null,
//                "name": "adawf",
//                "comment": "",
//                "price": 14.0,
//                "quantity": 5.0
//            },
//            {
//                "id": "8c55a084-9c62-4caf-8354-b26c62e05c4b",
//                "userId": null,
//                "username": null,
//                "categoryId": "3f24e2a3-a223-48f7-a776-278550e1edf0",
//                "categoryName": "Meat",
//                "unitId": null,
//                "unitName": null,
//                "name": "dbhdbh",
//                "comment": "",
//                "price": 15.0,
//                "quantity": 30.0
//            }
//        ],
//        "page": {
//            "size": 3,
//            "number": 1,
//            "totalElements": 5,
//            "totalPages": 2
//        }
//    }


//
//
//{
//	  "content": [...],      // Текущая страница товаров
//	  "page": {
//	    "size": 3,           // Размер страницы
//	    "number": 0,         // Текущая страница (0 — первая)
//	    "totalElements": 5,  // Всего элементов
//	    "totalPages": 2      // Всего страниц
//	  }
//	}