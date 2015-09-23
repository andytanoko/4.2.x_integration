/**
 * 
 */
package com.etrade.sftp.exception;

/**
 * @author EricLoh
 *
 */
public class SFTPClientException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -477884965774791321L;

    public SFTPClientException()
    {
        super();
    }

    public SFTPClientException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SFTPClientException(String message)
    {
        super(message);
    }

    public SFTPClientException(Throwable cause)
    {
        super(cause);
    }
}
