/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.command.ICommand;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

/**
 * @author Alain Ah Ming
 *
 */
public abstract class AbstractCommand implements ICommand {
    public static boolean unitTest = false;
    
	protected void logDebug(String methodName, String message)
	{
		Logger.debug(this.getClass().getSimpleName(), methodName, message);
	}
	protected void logError(String methodName, String message, Throwable t)
	{
		Logger.error(this.getClass().getSimpleName(), methodName, message, t);
	}
	
	protected void logInfo(String methodName, String testId, String testState, String message)
	{
		Logger.info(this.getClass().getSimpleName(), methodName, testId, testState, message);
	}

	protected void logTestState(String methodName, String testId, String state)
	{
		Logger.logTestState(this.getClass().getSimpleName(), methodName, testId, state);
	}
	
	protected void logWarn(String methodName, String message, Throwable t)
	{
		Logger.warn(this.getClass().getSimpleName(), methodName, message, t);
	}
	
	protected abstract void logTestState();
}
