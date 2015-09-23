package com.gridnode.gtas.client;

public class NotApplicableException extends RuntimeException
{
  public NotApplicableException(String message)
  {
    super(message);
  }

  public NotApplicableException()
  {
    super();
  }
}