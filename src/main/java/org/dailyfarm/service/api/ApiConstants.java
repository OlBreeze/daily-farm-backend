package org.dailyfarm.service.api;

public interface ApiConstants {
	// 🔹 Users
	String REGISTER_USER = "/account/register";  // POST
	String GET_ALL_USERS = "/users";  // GET
	String GET_USER = "/users/{username}";
	String ACTIVATE_USER = "/users/activate/{username}";
	String REVOKE_USER = "/users/revoke/{username}";

	// 🔹 Roles
	String ADD_ROLE_TO_USER = "/user/{login}/role/{role}";
	String REMOVE_ROLE_FROM_USER = "/user/{login}/role/{role}";
	String GET_ROLES_FOR_USER = "/roles/{login}";

	// 🔹 Login
	String LOGIN = "/api/auth/login";
	String LOGOUT = "/api/auth/logout";
	String REFRESH = "/api/auth/refresh";
	String AUTH_CHECK = "/api/auth/check";
	
	// 🔹 Products
	String GET_ALL_PRODUCTS 	= "/products";
	String GET_PAGES_PRODUCTS 	= "/products/pages";
	String GET_PRODUCT_ID 		= "/products/id/{id}";
	String GET_PRODUCT_NAME 	= "/products/name/{name}";
	String REMOVE_PRODUCT 		= "/products/remove/{id}";
	String ADD_PRODUCT 			= "/products/add";
	String UPDATE_PRODUCT 		= "/products/update/{id}";
	
	// 🔹 Units
	String GET_ALL_UNITS 	= "/units";
	String GET_UNIT = "/units/{id}";
	String ADD_UNIT = "/units/add";
	String REMOVE_UNIT = "/units/remove/{id}";
	
	// 🔹 Categories
	String GET_ALL_CATEGORIES 	= "/categories";
	String ADD_CATEGORY = "/categories/add";
	String GET_CATEGORY = "/categories/{id}";
	String REMOVE_CATEGORY = "/categories/remove/{id}";

	// 🔹 Orders
	String GET_ALL_ORDERS 	= "/orders";
	String GET_ORDER_ID 	= "/orders/id/{id}";
	String GET_ORDERS_USER 	= "/orders/user/{userId}";
	String ADD_ORDER		= "/orders/add";
	String UPDATE_ORDER = "/orders/update/{id}";
	String REMOVE_ORDER 	= "/orders/remove/{id}";
	String REMOVE_PRODUCT_FROM_ORDER 		= "/orders/remove_product/order/{orderId}/product/{productId}";
	// **
	String ADD_PRODUCT_TO_ORDER = "/orders/add_product";



	String HOST = "localhost";
	int PORT = 8080;
}
