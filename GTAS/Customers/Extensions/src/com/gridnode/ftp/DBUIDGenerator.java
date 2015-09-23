package com.gridnode.ftp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gridnode.ftp.exception.NestingException;



public class DBUIDGenerator
{

  private static final long INITIAL_COUNTER = 1;
  private static final String PAD_FORMAT = "0";
  private static DecimalFormat format = null;
  private static Properties dbConfig = new Properties();
  //private static DataSource _dataSource = null;

  static
  {
    try
   {
     InputStream inStream = new FileInputStream(new File(IDBConfig.CONFIG_LOC+IDBConfig.CONFIG_NAME));
     dbConfig.load(inStream);

    /*	Thread.currentThread().getContextClassLoader().getResourceAsStream(
    	  IDBConfig.CONFIG_LOC+IDBConfig.CONFIG_NAME));
    */
      System.getProperties().put(IDBConfig.JDBC_DRIVERS,
            dbConfig.getProperty(IDBConfig.JDBC_DRIVERS));
      //System.out.println("Properties are ........"+dbConfig);
      System.getProperties().put(IDBConfig.SAX_DRIVER,dbConfig.getProperty(IDBConfig.SAX_DRIVER));
      Class.forName(dbConfig.getProperty(IDBConfig.JDBC_DRIVER_CLASS));
      int padLength = Integer.parseInt(dbConfig.getProperty(IDBConfig.PAD_LENGTH));
      //_dataSource = getDataSource();
      StringBuffer sb = new StringBuffer();
      for (int i=0;i<padLength;i++)
      {
        sb.append(PAD_FORMAT);
      }
      format = new DecimalFormat(sb.toString());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public DBUIDGenerator()
  {
  }



  public static Connection getConnection()
    throws SQLException
  {
    Connection con = DriverManager.getConnection(
    	dbConfig.getProperty(IDBConfig.DB_URL));

    //Connection con = _dataSource.getConnection();
    return con;
  }

  /*public static DataSource getDataSource() throws SQLException
  {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(dbConfig.getProperty(IDBConfig.JDBC_DRIVER_CLASS));
    ds.setUrl(dbConfig.getProperty(IDBConfig.DB_URL));
    //ds.setUrl("jdbc:mysql://localhost:3306/appdb?user=root");
    return ds;
  }
*/
  public static  String getNextRefNum(Site siteLoc)
  	throws NestingException
  {
    long maxNo=0,nextNum=0;
    try
    {
      System.out.println("DBUIDGenerator getting Next num..");
      List values = generateUniqueNoFromDB(siteLoc);
      if (values == null)
      {
      	doInsertSequence(siteLoc);
	values = generateUniqueNoFromDB(siteLoc);
      }
      nextNum =   ((Long)values.get(0)).longValue();
      maxNo  = ((Long)values.get(1)).longValue();
      long id = getUniqueId(nextNum, maxNo);
      DecimalFormat format = padFormat(siteLoc);
      return format.format(id);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw new NestingException(ex.getMessage(),ex);
    }
  }

  public static synchronized void updateRefNum(long refNum,Site siteLoc)
  	throws NestingException
  {
    Connection dbConnection = null;
    PreparedStatement statement = null;
    try
    {
      String updateQuery =
          siteLoc.getProperty(ISiteConfig.DATABASE_UPDATE_QUERY);
        //dbConfig.getProperty(IDBConfig.DB_UPDATE_QUERY);
      dbConnection = getConnection();
      if (dbConnection == null)
      	return;
      statement = dbConnection.prepareStatement(updateQuery);
      statement.setLong(1, refNum);
      statement.setString(2, siteLoc.getProperty(ISiteConfig.DATABASE_SEQUENCE_NAME));
      int i = statement.executeUpdate();
      System.out.println("Updated..." + i);
    }
    catch(SQLException ex)
    {
      throw new NestingException(ex.getMessage(),ex);
    }
    finally
    {
      try
      {
        if (statement != null)
          statement.close();
        if (dbConnection != null)
          dbConnection.close();
      }
      catch(SQLException ex)
      {
        ex.printStackTrace();
      }
    }
  }


  private static synchronized void doInsertSequence(Site siteLoc)
    throws NestingException
  {
    Connection dbConnection = null;
    Statement statement = null;
    try
    {
      String insertQuery =
          siteLoc.getProperty(ISiteConfig.DATABASE_INSERT_QUERY);
      dbConnection = getConnection();
      if (dbConnection == null)
      	return;
      statement = dbConnection.createStatement();
      boolean b  = statement.execute(insertQuery);
      System.out.println("Record Inserted ..." + b);
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
      throw new NestingException(ex.getMessage(),ex);
    }
    finally
    {
      try
      {
        if (statement != null)
          statement.close();
        if (dbConnection != null)
          dbConnection.close();
      }
      catch(SQLException ex)
      {
        ex.printStackTrace();
      }
    }

  }

  private static List generateUniqueNoFromDB(Site siteLoc)
    throws NestingException
  {
    Connection dbConnection;
    PreparedStatement statement ;
    ResultSet rs = null;
    List values = null;

    try
    {
      dbConnection = getConnection();
      //boolean isRetrieved = false;
      if (dbConnection == null)
              return null;
      String selectQuery = siteLoc.getProperty(ISiteConfig.DATABASE_SELECT_QUERY);
      statement = dbConnection.prepareStatement(selectQuery);
      statement.setString(1, siteLoc.getProperty(ISiteConfig.DATABASE_SEQUENCE_NAME));
      rs = statement.executeQuery();
      //System.out.println("Fetch Size --- "+rs.h);
      while (rs.next()) {
    	System.out.println("[Values retrieved from DB..]");
        values = new ArrayList();
        values.add(new Long(rs.getLong(1)) );
        values.add(new Long(rs.getLong(2)));
      }
    }
    catch(Exception ex)
    {
      throw new NestingException(ex.getMessage(),ex);
    }
    return values;
  }


  private static synchronized long getUniqueId(long nextNum,long maxNo)
  {
    nextNum+=1;
    if (nextNum>maxNo || nextNum<INITIAL_COUNTER)
     nextNum = INITIAL_COUNTER;
    return nextNum;
  }

  private static DecimalFormat padFormat(Site siteLoc)
  {
    DecimalFormat format = null;
    String pad = siteLoc.getProperty(ISiteConfig.DATABASE_PAD_LENGTH);
    int padLength = Integer.parseInt(pad);
    StringBuffer sb = new StringBuffer();
    for (int i=0;i<padLength;i++)
    {
      sb.append(PAD_FORMAT);
    }
    format = new DecimalFormat(sb.toString());
    return format;
  }


  public static void main(String[] args) throws Exception
  {
    //DBUIDGenerator generator = new DBUIDGenerator();
    //  String nextSeqNo = DBUIDGenerator.getNextRefNum();
     // System.out.println(nextSeqNo);
      //DBUIDGenerator.updateRefNum(Long.parseLong(nextSeqNo));

  }




}