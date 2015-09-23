/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.exception;

/**
 * @author i00057
 *
 */
public class TestNotFoundException extends GlbtException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2083682591982784027L;

	private String _testId = null;
	
	public TestNotFoundException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TestNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TestNotFoundException(String testId)
	{
		super("Test Not Found: "+testId);
		_testId = testId;
		// TODO Auto-generated constructor stub
	}

	public TestNotFoundException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getTestId()
	{
		return _testId;
	}
}
