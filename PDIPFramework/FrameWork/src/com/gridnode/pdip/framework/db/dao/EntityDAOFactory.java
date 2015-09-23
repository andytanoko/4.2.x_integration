package com.gridnode.pdip.framework.db.dao;

/**
 * <p>Title: PDIP</p>
 * <p>Description:
 *     This is the singleton class,which will hold the instances of EntityDAOImpl
 *     objects.This class will create only one instance for each Entity,and same
 *     instance is used to serve all the requests for that entity </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Mahesh
 * @version 1.0
 */

import java.util.*;

public class EntityDAOFactory {
    Hashtable entityDAOHt;

    private static EntityDAOFactory  entityDAOFactory;
    private EntityDAOFactory(){
        entityDAOHt=new Hashtable();
    }

    /**
     * This method returns EntityDAOFactory object.it will create an instance
     * if it does not exist.
     *
     * @return EntityDAOFactory
     */
    public static EntityDAOFactory getInstance(){
        if(entityDAOFactory==null){
            synchronized(EntityDAOFactory.class){
                if(entityDAOFactory==null)
                    entityDAOFactory=new EntityDAOFactory();
            }
        }
        return entityDAOFactory;
    }

    /**
     * This method returns the EntityDAOImpl object for the passed entityName.
     * This will create a new instance if one does not exist for that entityName
     * in the HashTable
     *
     * @param entityName
     * @return IEntityDAO
     */
    public IEntityDAO getDAOFor(String entityName){
        IEntityDAO entityDAO=(IEntityDAO)entityDAOHt.get(entityName);

        if(entityDAO==null){
            synchronized(entityDAOHt){
                if(entityDAO==null){
                    entityDAO=new EntityDAOImpl(entityName);
                    entityDAOHt.put(entityName,entityDAO);
                }
            }
        }
        return entityDAO;
    }

}