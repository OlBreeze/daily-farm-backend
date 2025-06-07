package org.dailyfarm.service.exceptions;

@SuppressWarnings("serial")
public class EntityNotExistsException extends RuntimeException
{
	public EntityNotExistsException(){}
	public EntityNotExistsException(String message)
	{
		super(message);
	}
}
