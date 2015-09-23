package com.gridnode.gtas.client.web.xml;

import com.gridnode.gtas.client.GTClientException;

public class BadDocumentException extends GTClientException
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7980081772350401189L;

	public BadDocumentException()
  {
    super();
  }

  /**
  * Constructs an Exception with the specified detail message.
  * @param message the detail message.
  */
  public BadDocumentException(String message)
  {
    super(message);
  }

  /**
  * Constructs an Exception
  * @param message the detail message.
  */
  public BadDocumentException(Throwable nestedException)
  {
    super(nestedException);
  }

  /**
  * Constructs an Exception
  * @param message the detail message.
  * @param nestedException the root cause exception
  */
  public BadDocumentException(String message, Throwable nestedException)
  {
    super(message, nestedException);
  }
}