/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaAppRunner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13 2003    Neo Sok Lay         Migrate from GT1.x
 * Oct 22 2003    Neo Sok Lay         Enhance to allow:
 *                                    - sub-class to override source of
 *                                      startup configuration
 *                                    - option to cache the System.out or
 *                                      System.err outputs by the run process.
 */
package com.gridnode.ext.util;

import java.text.MessageFormat;
import java.io.*;
import java.util.Properties;

/**
 * This class is a generic launcher for Java applications.
 *
 * @author Neo Sok Lay
 * @author Ang Meng Hua
 * @author Roger Ng
 *
 * @version 1.3
 * @since 1.0b
 */
public class JavaAppRunner
{
  public static final String JAVA_HOME = System.getProperty("java.home");

  protected MessageFormat _commandFormat =
    new MessageFormat("{0} {1} -Duser.dir=\"{2}\" -cp \"{3}\" {4} {5}");

  protected StringBuffer classPath = new StringBuffer();
  protected String workDir = "";
  protected String startupConfig = "config"+File.separator+"startup.config";
  protected String startupConfigBak = "config"+File.separator+"startup.bak";
  protected Properties startupProp = new Properties();
  protected String cmd = null;
  protected String[] cmdArray = null;
  protected String tmpCmdFile = null;
  protected StreamGobbler _outGobbler;
  protected StreamGobbler _errGobbler;
  protected int _returnCode;

  protected static String USER_DIR_KEY   = "user.dir";
  protected static String JVM_OPTION_KEY = "jvm.options";
  protected static String APP_PARAM_KEY  = "app.params";
  protected static String MAIN_CLASS_KEY = "main.class";
  protected static String LIB_NUM_KEY    = "lib.";
  protected static String NUM_LIB_KEY    = "lib.num";
  protected static String OUTPUT_CACHE_KEY = "out.cache";
  protected static String ERROR_CACHE_KEY  = "err.cache";

  /**
   * Constructs an instance of a JavaAppRunner. The runner reads from
   * a startup.config file under the working directory the necessary startup
   * options to run the java application.
   *
   * <P>The java application will not be run immediately.
   *
   * @since 1.0b
   */
  public JavaAppRunner()
  {
    this(false);
  }

  /**
   * Constructs an instance of a JavaAppRunner. The runner reads from
   * a startup.config file under the working directory the necessary startup
   * options to run the java application.
   *
   * @param runImmed <B>true</B> to run the java application immediately after
   * the startup options has been read, <B>false</B> otherwise. The java
   * application can then be launched using {@link #runApp runApp()}.
   *
   * @since 1.0b
   */
  public JavaAppRunner(boolean runImmed)
  {
    workDir = System.getProperty(USER_DIR_KEY);
    readStartupProp();
    if (runImmed)
      runApp();
  }

  /**
   * Constructs the classpath for the java application.
   *
   * @return <B>true</B> if the classpath is constructed successfully,
   * <B>false</B> otherwise.
   *
   * @since 1.0b
   */
  protected boolean constructClassPath()
  {
    int numLib = getNumOfLib();
    if (numLib < 1)
      return false;

    for (int i=0; i<numLib; i++)
    {
      String libName = startupProp.getProperty(LIB_NUM_KEY+i,"");
      if (!"".equals(libName))
        classPath.append(libName+File.pathSeparator);
    }
    return true;
  }

  /**
   * Constructs the command to run the java application.
   *
   * @return <B>true</B> if the command is constructed successfully,
   * <B>false</B> otherwise.
   *
   * @since 1.0b
   */
  protected boolean constructCommand()
  {
    String mainClass = getMainClass();
    if ("".equals(mainClass))
      return false;

    if (isWindowsOS())
    {
      //cmd =  "javaw" + " " + getJvmOptions();
      //cmd += " -Duser.dir=\""+workDir+"\"";
      //cmd += " -cp \"" + classPath.toString() +"\"";
      //cmd += " " + mainClass + " " + getAppParams();

      String[] params = new String[]{
        getJavaExeName(),
        getJvmOptions(),
        workDir,
        classPath.toString(),
        mainClass,
        getAppParams(),
      };

      cmd = _commandFormat.format(params);
    }
    else
    {
      String tmpCmdFile = createCommandFile();
      if (tmpCmdFile == null) return false;
      cmdArray = new String[]
                 { "/bin/sh",
                   tmpCmdFile,
                   getJvmOptions() + " -Duser.dir="+workDir,
                   classPath.toString(),
                   mainClass,
                   getAppParams()
                 };
    }
    return true;
  }

  protected String createCommandFile()
  {
    if (!isWindowsOS())
    {
      String command = getJavaExeName() +" $1 -classpath \"$2\" $3 $4\n";
      FileWriter fW = null;
      try
      {
        String tmpCmdFile = "run"+System.currentTimeMillis();
        fW = new FileWriter(tmpCmdFile);
        fW.write(command);
        fW.flush();
        return tmpCmdFile;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        if (fW != null)
        {
          try                  { fW.close();}
          catch (Exception ex) { }
        }
        fW = null;
      }
    }
    return null;
  }

  public static final String getJavaExeName()
  {
    File exeFile = null;
    String javaw = "bin/javaw";
    String java = "bin/java";
    if (isWindowsOS())
    {
      javaw += ".exe";
      java += ".exe";
    }
/* use java only */
//    exeFile = new File(JAVA_HOME, javaw);
//    if (!exeFile.exists())
      exeFile = new File(JAVA_HOME, java);

    return exeFile.getAbsolutePath();
  }

  protected static boolean isWindowsOS()
  {
    return File.separatorChar == '\\';
  }

  /**
   * Get the application parameters from the startup configurations.
   *
   * @return The application parameters specified in the startup configuration
   * file.
   *
   * @since 1.0b
   */
  protected String getAppParams()
  {
    return startupProp.getProperty(APP_PARAM_KEY, "");
  }

  /**
   * Get the JVM options from the startup configurations.
   *
   * @return The JVM options specified in the startup configuration
   * file.
   *
   * @since 1.0b
   */
  protected String getJvmOptions()
  {
    return startupProp.getProperty(JVM_OPTION_KEY, "");
  }

  /**
   * Get the main class from the startup configurations.
   *
   * @return The main class specified in the startup configuration
   * file.
   *
   * @since 1.0b
   */
  protected String getMainClass()
  {
    return startupProp.getProperty(MAIN_CLASS_KEY,"");
  }

  /**
   * Get the number of libraries from the startup configurations.
   *
   * @return The number of libraries specified in the startup configuration
   * file.
   *
   * @since 1.0b
   */
  protected int getNumOfLib()
  {
    String libNum = startupProp.getProperty(NUM_LIB_KEY, "1");
    try
    {
      int libNumInt = Integer.parseInt(libNum);
      return libNumInt;
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      return -1;
    }
  }

  /**
   * Get the option whether to cache the System.out output churn out by the
   * process being run. Default value is <b>false</b> if the "out.cache" option
   * is not specified.
   *
   * @return <b>true</b> if caching is turned on, <b>false</b> otherwise.
   */
  protected boolean isSystemOutCache()
  {
    String cacheStr = startupProp.getProperty(OUTPUT_CACHE_KEY, "false");

    return Boolean.valueOf(cacheStr).booleanValue();
  }

  /**
   * Get the option whether to cache the System.err output churn out by the
   * process being run. Default value is <b>false</b> if the "err.cache" option
   * is not specified.
   *
   * @return <b>true</b> if caching is turned on, <b>false</b> otherwise.
   */
  protected boolean isSystemErrCache()
  {
    String cacheStr = startupProp.getProperty(ERROR_CACHE_KEY, "false");

    return Boolean.valueOf(cacheStr).booleanValue();
  }

  /**
   * Read the startup configuration file.
   *
   * @return <B>true</B> if the configuration file is read successfully,
   * <B>false</B> otherwise.
   *
   * @since 1.0b
   */
  protected boolean readStartupProp()
  {
    String fileName = workDir + File.separator + startupConfig;

    try
    {
      startupProp.load(new FileInputStream(fileName));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      return false;
    }

  }

  /**
   * Write the startup configuration file with modified properties.
   * The original configuration file is backup to startup.bak.
   *
   * @since 1.0b
   */
  protected void writeStartupProp()
  {
    backupProps();
    String fileName = workDir + File.separator + startupConfig;

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(fileName);
      startupProp.store(fos, "Application Startup Configurations");
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
    }
    finally
    {
      if (fos != null)
      {
        try
        {
          fos.close();
        }
        catch (Exception ex) {}
      }
    }
  }

  /**
   * Backup the startup configuration file to startup.bak.
   *
   * @since 1.0b
   */
  protected void backupProps()
  {
    File fromFile = new File(workDir + File.separator + startupConfig);
    File toFile   = new File(workDir + File.separator + startupConfigBak);

    toFile.delete();
    fromFile.renameTo(toFile);
  }

  /**
   * Some setup that needs to be done pre-execution of the java application.
   * Sub-classes may override to perform necessary setup prior to running
   * the java application. This method returns <B>true</B> by default.
   *
   * @return <B>true</B> if the setup is successful, <B>false</B> otherwise.
   *
   * @since 1.0b
   */
  protected boolean setup()
  {
    return true;
  }

  private void cleanupCore()
  {
    try
    {
      if (cmdArray != null)
      {
        new File(cmdArray[1]).delete();
      }
    }
    catch (Exception ex)
    {

    }
  }

  /**
   * Some cleanup that needs to be done after the java application finishes
   * execution. Sub-classes may override to perform necessary cleanup after
   * the java application finishes execution.
   *
   * @since 1.0b
   */
  protected void cleanup()
  {
  }

  /**
   * Runs the Java application.
   * <P>The <CODE>setup()</CODE> method will be
   * called prior to launching the java application itself. If the setup is
   * successful, the java application will launch, otherwise this method returns
   * <B>false</B>.
   * <P>After the java application finishes execution, the <CODE>cleanup</CODE>
   * method will be called to perform any necessary cleanup.
   *
   * @return <B>true</B> if the Java application is launched successfully,
   * <B>false</B> otherwise. Either way, cleanup will be performed when this
   * method exits. Note that any errors encountered within the Java application
   * itself is not considered as failure in launching the program.
   *
   * @since 1.0b
   */
  public boolean runApp()
  {
    if (!setup() || !constructClassPath() || !constructCommand())
      return false;

    try
    {
      Process proc = null;
      if (isWindowsOS())
      {
        System.out.println("Launching Application with command: ");
        System.out.println(cmd);
        proc = Runtime.getRuntime().exec(cmd);
      }
      else
      {
        System.out.println("Launching Application on non-Windows OS: ");
        for (int i=0; i<cmdArray.length; i++)
        {
          if (i > 0) System.out.print(" ");
          System.out.print(cmdArray[i]);
        }
        proc = Runtime.getRuntime().exec(cmdArray);
      }

      _errGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", isSystemErrCache());
      _outGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", isSystemOutCache());
      _errGobbler.start();
      _outGobbler.start();

      _returnCode = proc.waitFor();
      System.out.println("Application ended with error code: "+_returnCode);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      return false;
    }
    finally
    {
      cleanupCore();
      cleanup();
    }
  }

  /**
   * Get the gobbled string data from the System.err of the run process.
   *
   * @return any gobbled data from the System.err output of the run process, or
   * <b>null</b> if no process has been run or cache is not turned on.
   */
  public String getSystemErrCache()
  {
    return (_errGobbler != null ? _errGobbler.getCachedGobbledData() : null);
  }

  /**
   * Get the gobbled string data from the System.out of the run process.
   *
   * @return any gobbled data from the System.out output of the run process, or
   * <b>null</b> if no process has been run or cache is not turned on.
   */
  public String getSystemOutCache()
  {
    return (_outGobbler != null ? _outGobbler.getCachedGobbledData() : null);
  }

  /**
   * Get the return code from the run process.
   *
   * @return The return code from the run process. By default if the process
   * has not been run, the return value is 0.
   */
  public int getReturnCode()
  {
    return _returnCode;
  }

  /**
   * Runs the Java application.
   * <P>The <CODE>setup()</CODE> method will be
   * called prior to launching the java application itself. If the setup is
   * successful, the java application will launch, otherwise this method returns
   * <B>false</B>.
   * <P>After the java application finishes execution, the <CODE>cleanup</CODE>
   * method will be called to perform any necessary cleanup.
   *
   * @return <B>true</B> if the Java application is launched successfully,
   * <B>false</B> otherwise. Either way, cleanup will be performed when this
   * method exits. Note that any errors encountered within the Java application
   * itself is not considered as failure in launching the program.
   *
   * @since 1.0b
   */
  public static boolean runApp(String cmd, boolean wait)
  {
    try
    {
      System.out.println("Launching Application with command: ");
      System.out.println(cmd);
      Process proc = Runtime.getRuntime().exec(cmd);

      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
      errorGobbler.start();
      outputGobbler.start();

      if (wait)
      {
        int code = proc.waitFor();
        System.out.println("Application ended with error code: "+code);
      }

      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      return false;
    }
  }

  /**
   * Runs an instance of JavaAppRunner.
   *
   * @param args Accepts as first parameter the working directory for the
   * JavaAppRunner as well as for the Java application to be executed.
   *
   * @since 1.0b
   */
  public static void main(String[] args)
  {
    if (args.length > 0)
    {
      System.setProperty(USER_DIR_KEY, args[0]);
    }

    //JavaAppRunner appRunner = 
    new JavaAppRunner(true);
  }
}
