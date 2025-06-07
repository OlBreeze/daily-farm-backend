package org.dailyfarm.service.exceptions;

@SuppressWarnings("serial")
public class EntityExistsException extends RuntimeException{
	public EntityExistsException(){}
	public EntityExistsException(String message)
	{
		super(message);
	}
}
