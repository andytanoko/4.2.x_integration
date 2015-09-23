/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyCheckEngine.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Set;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptorListSet;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet;

/**
 * This is the engine for entity dependency checking.
 * This engine makes use of DependencyDescriptors specified for each entity
 * to obtain the dependent entities for the entity. The actual manner of obtaining
 * dependent entities are delegated to the dependency checker classes specified
 * in the DependencyDescriptors.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DependencyCheckEngine
{
  private static final Hashtable _objectInstanceTable = new Hashtable();
  private static final DependencyCheckEngine _self = new DependencyCheckEngine();
  
  private static final boolean IS_DEBUG = false;
  
  /**
   * Constructor for DependencyChecker.
   */
  private DependencyCheckEngine()
  {
  }

  /**
   * Get an instance of this class.
   * 
   * @return An instance of DependencyCheckEngine
   */
  public static DependencyCheckEngine getInstance()
  {
    return _self;
  }
  
  /**
   * Get the dependencies for the specified entity.
   * 
   * @param entity The entity to get dependencies for.
   * @return A IEntityDescriptorListSet containing a number of 
   * IEntityDescriptorList of EntityDescriptor(s) for each dependent
   * entity on the specified entity instance.
   * @throws DependencyCheckException Error while checking dependencies or
   * creating the entity descriptors for the dependent entities.
   */
  public IEntityDescriptorListSet getDependencies(IEntity entity) 
    throws DependencyCheckException
  {
    // Get the dependency descriptor for the entity
    DependencyDescriptor dependencyDescr = 
      DependencyDescriptorFactory.getInstance().getDependencyDescriptor(
        entity.getEntityName());

    EntityDescriptorListSet listSet = new EntityDescriptorListSet();

    // Iterate through the list of checkers specified
    CheckerDescriptor[] checkDescrs = dependencyDescr.getCheckers();
    if (checkDescrs != null)
    {
      IEntityDescriptorList depList;
      for (int i=0; i<checkDescrs.length; i++)
      {
        // Get the dependency list for the entity using the checker
        depList = getDependencies(entity, checkDescrs[i]);
        // only add to the set if there are dependencies
if (IS_DEBUG) System.out.println("***** getDependencies(checker) returns "+depList);
 
        if (depList != null && depList.getEntityDescriptors().length > 0)
        {
if (IS_DEBUG) System.out.println("***** getDependencies() adding non-empty dependent list");
          listSet.addEntityDescriptorList(depList);
        }
      }
    }
    
    return listSet;
  }
  
  /**
   * Get the dependencies for the specified entity using the specified CheckerDescriptor.
   * 
   * @param entity The entity instance to get dependencies for.
   * @param checkerDescr The CheckerDescriptor that specifies the dependency checker class
   * and method to obtain the dependencies.
   * @return IEntityDescriptorList containing the EntityDescriptor(s) for each
   * entity that is dependent on the specified entity instance.
   * @throws DependencyCheckException Error while Error while checking dependencies using
   * the specified CheckerDescriptor or creating the entity descriptors for the dependent entities 
   * using any DescriptorCreatorDescriptor specified in the CheckerDescriptor.
   */
  private IEntityDescriptorList getDependencies(IEntity entity, CheckerDescriptor checkerDescr)
    throws DependencyCheckException
  {
    Set dependentSet;
    try
    {    
      // Get the checker class instance
      Class checkerClass = getClassFor(checkerDescr.getClassName());
      
      ParamDescriptor[] paramDescrs = checkerDescr.getParams();

      // Get the class types for the parameters to the checker method
      Class[] paramClassTypes = getParamClassTypes(paramDescrs);
  
      // Get the checker method
      Method checkerMethod = getMethodFor(checkerClass, checkerDescr.getMethodName(), paramClassTypes);
      
      // Get the parameter values from the entity
      Object[] paramValues = getParamValues(paramDescrs, entity);

      // invoke the checker method to obtain the set of dependent entities
      dependentSet = invokeMethod(checkerClass, checkerMethod, paramValues);
    }
    catch (InvocationTargetException ex)
    {
      throw DependencyCheckException.dependencyCheckerEx(ex.getTargetException());
    }
    catch (Throwable t)
    {
      throw DependencyCheckException.dependencyCheckerEx(t);
    }   

    IEntityDescriptorList descrList = null;
    
    // populate the EntityDescriptorList with the EntityDescriptor(s) of the
    // dependent entities
    if (dependentSet != null && !dependentSet.isEmpty())
    {
      // Create the EntityDescriptorList for specific type of dependent entity list
      descrList = createDependentList(dependentSet, checkerDescr.getDescriptorCreator());
    }
    return descrList;
  }
  
  /**
   * Get the Class instance using the specified class name
   * 
   * @param className Full qualified name of the class
   * @return Class instance obtained.
   * @throws ClassNotFoundException Invalid class name specified.
   */
  private Class getClassFor(String className) throws ClassNotFoundException
  {
    return Class.forName(className);   
  }

  /**
   * Get the Method instance in a Class instance using the specified method name and
   * parameter types.
   * 
   * @param classInstance The Class instance to obtain the Method instance
   * @param methodName Name of the method
   * @param paramClassTypes Parameter types of the method signature.
   * @return Method instance obtained.
   * @throws NoSuchMethodException Method not found in the class instance with the
   * specified method name and parameter types.
   * @throws SecurityException Unable to obtain the access information
   */  
  private Method getMethodFor(Class classInstance, String methodName, Class[] paramClassTypes) 
    throws NoSuchMethodException, SecurityException
  {
    return classInstance.getMethod(methodName, paramClassTypes);
  }

  /**
   * Invoke the specified method on the specified object instance, passing in
   * the specified parameter values.
   * 
   * @param instance The object instance to invoke the method on.
   * @param method The Method instance to invoke.
   * @param params The parameter values to be passed into the method.
   * @throws IllegalAccessException The specified method is inaccessible.
   * @throws IllegalArgumentException Invalid parameter values.
   * @throws InvocationTargetException The invoked method throws exception.
   */
  private Object invoke(Object instance, Method method, Object[] params) 
    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    return method.invoke(instance, params);
  }
  
  /**
   * Get the class types for the specified parameter descriptors     
   * 
   * @param paramDescrs The ParamDescriptors
   * @return Array of Class types for each ParamDescriptor specified.
   * @throws ClassNotFoundException Invalid class name specified in one of the
   * ParamDescriptor.
   */
  private Class[] getParamClassTypes(ParamDescriptor[] paramDescrs) 
    throws ClassNotFoundException
  {
    Class[] classes = new Class[paramDescrs.length];
    for (int i=0; i<classes.length; i++)
    {
      classes[i] = getClassFor(paramDescrs[i].getType());
    }
    
    return classes;
  }
 
  /**
   * Get the parameter values for the specified ParamDescriptor(s) from the
   * specified entity instance.
   * 
   * @param paramDescrs The ParamDescriptor(s) for each parameter
   * @param entity The entity instance to obtain parameter values from.
   * @see #getMethodFor
   * @see #invoke
   */
  private Object[] getParamValues(ParamDescriptor[] paramDescrs, IEntity entity)
    throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException
  {
    Object[] values = new Object[paramDescrs.length];
    Method method;
    for (int i=0; i<values.length; i++)
    {
      method = getMethodFor(entity.getClass(), paramDescrs[i].getGetterMethod(), null); 
      values[i] = invoke(entity, method, null);
    }
    
    return values;
  }
  
  /**
   * Invoke the specified Method of the specified Class instance, passing the specified
   * parameter values.
   * 
   * @param classInstance The Class instance. An object instance of this Class instance
   * will be created using the default constructor for invoking the method.
   * @param method The Method instance to invoke
   * @param paramValues The parameter values to pass to the method.
   * @see #getObjectInstance
   * @see #invoke
   */
  private Set invokeMethod(Class classInstance, Method method, Object[] paramValues)
    throws InstantiationException, IllegalAccessException,
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException
  {
    Object instance = getObjectInstance(classInstance);
    
    return (Set)invoke(instance, method, paramValues);
  }
  
  /**
   * Get an object instance of the specified class type.
   * 
   * @param classInstance The class type.
   * @return An object instance of the specified class type.
   * @throws InstantiationException Unable to instantiate the object instance.
   * @throws IllegalAccessException The specified class instance is not accessible.
   */
  private Object getObjectInstance(Class classInstance) 
    throws InstantiationException, IllegalAccessException
  {
    // Use the cached object instance if one has already been instantiated
    Object objectInstance = _objectInstanceTable.get(classInstance.getName());
    if (objectInstance == null)
    {
      objectInstance = classInstance.newInstance();
      
      // cache the object instance for re-use
      _objectInstanceTable.put(classInstance.getName(), objectInstance);
    }
    return objectInstance;
  }
  
  /**
   * Populate the dependent EntityDescriptorList with EntityDescriptor(s) for
   * each dependent entity.
   * 
   * @param descrList The EntityDescriptorList to be populated.
   * @param dependentSet Set of dependent entities
   * @param descrCreatorDescr DescriptorCreatorDescriptor for the descriptor creator
   * class and method to create the EntityCreators. <b>null</b> if default is to be 
   * used.
   * @throws DependencyCheckException If DescriptorCreatorDescriptor is specified
   * and there is error in obtaining the class or method instance, or invoking the
   * creation method.
   */
  private IEntityDescriptorList createDependentList(
    Set dependentSet, 
    DescriptorCreatorDescriptor descrCreatorDescr) 
    throws DependencyCheckException
  {
    IEntityDescriptorList descrList = null;
    
    //Set descrSet;
    if (descrCreatorDescr != null)
    {
if (IS_DEBUG) System.out.println("**** createDependentList using custom creator");  
    
      // invoke the class & method to get the entity descriptors for each object
      try
      {
        Class creatorClass = getClassFor(descrCreatorDescr.getClassName());
        Method method = getMethodFor(creatorClass, descrCreatorDescr.getMethodName(), new Class[]{Set.class});
        Object instance = getObjectInstance(creatorClass);
   
        descrList = (IEntityDescriptorList)invoke(instance, method, new Object[]{dependentSet});
      }
      catch (InvocationTargetException ex)
      {
        throw DependencyCheckException.createDescriptorEx(ex.getTargetException());
      }
      catch (Throwable t)
      {
        throw DependencyCheckException.createDescriptorEx(t);      
      }
    }
    else
    {
if (IS_DEBUG) System.out.println("***** createDependentList using default creator");
      
      //invoke default descriptor creator 
      descrList = DefaultEntityDescriptorCreator.getInstance().createDescriptorList(dependentSet);
    }
    
    return descrList;
  }
}
