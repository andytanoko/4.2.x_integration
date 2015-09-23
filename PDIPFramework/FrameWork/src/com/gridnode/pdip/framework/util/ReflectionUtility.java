/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReflectionUtility.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Jun 10 2002    Ang Meng Hua        Add getSignature() method
 */
package com.gridnode.pdip.framework.util;

import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;
import java.util.Vector;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

/**
 * Utility class to make use of reflection api
 */

/**
 * This class makes use of the reflection api to provide custom functionality.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public class ReflectionUtility
{
  /**
   * Get a declared field in a class.
   * The accessibility of the field will be set to true.
   *
   * @param objClass The Class to find the field.
   * If not found in <I>objClass</I>, the ancestors of <I>objClass</I> will
   * be looked into.
   * @param fieldName Name of the field as declared in the class
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Field getAccessibleField(Class objClass, String fieldName)
  {
    Field field = null;
    do
    {
      try
      {
        field = objClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
      }
      catch (NoSuchFieldException ex)
      {
        //look into objClass' interfaces
        Class[] interfaceClass = objClass.getInterfaces();
        for (int i=0; i<interfaceClass.length; i++)
        {
          field = getAccessibleField(interfaceClass[i], fieldName);
          if (field != null)
            return field; //found the field in one of the interfaces
        }
        //look into the super class
        objClass = objClass.getSuperclass();
      }
    }
    while (objClass != null);

    return field;
  }

  /**
   * Get a declared field in a class.
   * The accessibility of the field will be set to true.
   *
   * @param objClass The Class to find the field.
   * If not found in <I>objClass</I>, the ancestors of <I>objClass</I> will
   * be looked into.
   * @param fieldName Name of the field as declared in the class
   * @param modifiers The modifiers that the field must have.
   *
   * @since 1.0a build 0.9.9.6
   */
   public static Field getAccessibleField(
     Class objClass, String fieldName, int modifiers)
  {
    Field field = null;
    do
    {
      try
      {
        field = objClass.getDeclaredField(fieldName);
        //the field must have the specified modifiers
        if ((field.getModifiers() & modifiers) != 0)
        {
          field.setAccessible(true);
          return field;
        }
      }
      catch (Exception ex)
      {
        //look into the interfaces
        Class[] interfaceClass = objClass.getInterfaces();
        for (int i=0; i<interfaceClass.length; i++)
        {
          field = getAccessibleField(interfaceClass[i], fieldName);
          if (field != null)
            return field;
        }
        //try the super class
        objClass = objClass.getSuperclass();
      }
    }
    while (objClass != null);

    return field;
  }

  /**
   * Get all declared fields in a class and all its interfaces and super
   * classes.
   * The accessibility of all fields are set to true.
   *
   * @param objClass The class to look into
   * @return All fields in the class with modified accessibility.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Field[] getAccessibleFields(Class objClass)
  {
    Field[] fields;
    Vector fieldV = new Vector();
    do
    {
      fields = objClass.getDeclaredFields();
      for (int i=0; i<fields.length; i++)
      {
        fields[i].setAccessible(true);
        fieldV.add(fields[i]);
      }
      //look into the interfaces
      Class[] interfaceClass = objClass.getInterfaces();
      for (int i=0; i<interfaceClass.length; i++)
      {
        fields = getAccessibleFields(interfaceClass[i]);
        for (int j=0; j<fields.length; j++)
        {
          fields[j].setAccessible(true);
          fieldV.add(fields[j]);
        }
      }
      //look into the super class
      objClass = objClass.getSuperclass();
    }
    while (objClass != null);

    return (Field[])fieldV.toArray(new Field[0]);
  }

  /**
   * Get all declared fields in a class and all its interfaces and super
   * classes.
   * The accessibility of all fields are set to true.
   *
   * @param objClass The class to look into
   * @param modifiers The modifiers that the fields must have
   * @return All fields in the class having the specified modifiers and with
   * modified accessibility.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Field[] getAccessibleFields(Class objClass, int modifiers)
  {
    Field[] fields;
    Vector fieldV = new Vector();
    do
    {
      fields = objClass.getDeclaredFields();
      for (int i=0; i<fields.length; i++)
      {
        fields[i].setAccessible(true);
        if ((fields[i].getModifiers() & modifiers) != 0)
          fieldV.add(fields[i]);
      }
      //look into the interfaces
      Class[] interfaceClass = objClass.getInterfaces();
      for (int i=0; i<interfaceClass.length; i++)
      {
        fields = getAccessibleFields(interfaceClass[i], modifiers);
        for (int j=0; j<fields.length; j++)
        {
          fields[j].setAccessible(true);
          fieldV.add(fields[j]);
        }
      }
      //look into the super class
      objClass = objClass.getSuperclass();
    }
    while (objClass != null);

    return (Field[])fieldV.toArray(new Field[0]);
  }
  /**
   * Creates a new object instance of a class type.
   *
   * @param className Name of the class
   * @param paramTypes The types of the parameters to the constructor of
   * <I>className</I> to invoke.
   * @param params The parameters to the constructor of <I>className</I> to
   * invoke
   * @return The new instance created, or <B>null</B> if error in creating
   * instance.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Object newInstance(
    String className, Class[] paramTypes, Object[] params)
  {
    try
    {
      Constructor construct =
        Class.forName(className).getConstructor(paramTypes);
      return construct.newInstance(params);
    }
    catch (Throwable ex)
    {
      Log.error(ILogErrorCodes.REFLECTION_UTIL_INIT, Log.DB, "[ReflectionUtility.newInstance] Failed to initialize. Error: "+ex.getMessage(), ex);
      return null;
    }
  }

   /** Construct an array of Class objects representing a method signature
   *
   * @param parameters  A vector whose elements will be
   * reflected or their specified Classes.
   * @return A Class array
   *
   */
   public static Class[] getSignature(Object [] parameters)
   {
      Class sig[] = new Class[parameters.length];

      for (int i =0; i<sig.length; i++)
      {
        sig[i] = parameters[i].getClass();
      }
      return sig;
   }

}