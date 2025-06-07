package org.dailyfarm.service.api;
public interface ExceptionMessageConstants
{
	String WRONG_ARGUMENT = "Invalid method parameter/parameters";
	String WRONG_PHONE_NUMBER = "Registration phone number does not match the pattern";
	
	String UNIT_EXISTS = "Unit already exists";
	String CATEGORY_EXISTS = "Category already exists";
	String PRODUCT_EXISTS = "Product already exists";
	
	String UNIT_NOT_EXISTS = "Unit does not exist";
	String CATEGORY_NOT_EXISTS = "Category does not exist";
	String PRODUCT_NOT_EXISTS = "Product does not exist";
	String ORDER_NOT_EXISTS = "Such order does not exist";
	
	String RESOURCE_EXISTS = "Resource already exists";
}
