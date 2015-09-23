/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityReferenceHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-15     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.rpf.model.*;

/**
 * Some utiltity methods to assist with converting between bTier and pTier multiple entity references. The
 * pTier makes use of the more abstract IGTMheReference interface, while the bTier now uses the
 * IEntityDescriptorListSet (introduced more recently than the IGTMheReference).
 * This class has been implemented as a singleton rather tham using static methods. I am certain it will not
 * be long before I need to subclass it for some specific situation and alter slightly its behaviour or to 
 * supply additional parameters without breaking the interface, and this is hard to do with statics.
 */
class EntityReferenceHelper
{  
  private static EntityReferenceHelper _instance;
  
  protected EntityReferenceHelper()
  {
  }
  
  public synchronized static EntityReferenceHelper getInstance()
  {
    if(_instance == null) _instance = new EntityReferenceHelper();
    return _instance;
  }
  
  /**
   * Convert an IEntityDescriptorListSet into an IGTMheReference
   * @param edls
   * @return mheRef
   * @throws GTClientException
   */
  public IGTMheReference getMheReference(IEntityDescriptorListSet edls)
    throws GTClientException
  {
    try
    {
      if (edls == null)
        throw new NullPointerException("edls is null");
      SimpleMheReference mheRef = new SimpleMheReference();
      Collection lists = edls.getEntityDescriptorLists();
      if (lists == null)
        throw new NullPointerException("entityDescriptorLists is null in EntityDescriptorListSet");
      Iterator listsIterator = lists.iterator();
      int listIndex = 0;
      int size = lists.size();
      while(listsIterator.hasNext())
      {
        Object listOb = listsIterator.next();
        if(! (listOb instanceof IEntityDescriptorList) )
        {
          throw new ClassCastException("Expecting an instance of IEntityDescriptorList but found "
                    + StaticCtrlUtils.objectClassName(listOb)
                    + " at index "
                    + listIndex
                    + " (size="
                    + size
                    + ") in entityDescriptorLists of IEntityDescriptorListSet");
        }
        IEntityDescriptorList list = (IEntityDescriptorList)listOb;
        if (list == null)
          throw new NullPointerException("list is null");
        try
        {
          IEntityDescriptor[] descriptors = list.getEntityDescriptors();
          if(descriptors != null)
          {
            for(int i=0; i < descriptors.length; i++)
            {
              IEntityDescriptor descriptor = descriptors[i];
              if (descriptor == null)
              {
                throw new NullPointerException("descriptor["
                                                + i + "] is null in entityDescriptors array (size="
                                                + descriptors.length
                                                + ") of IEntityDescriptorListSet");
              }
              SimpleEntityReference ref = new SimpleEntityReference();
              ref.setKeyValue(descriptor.getKey());
              ref.setDisplay(descriptor.getDescription());
              ref.setKeyFieldId(list.getKeyId());
              ref.setType( DefaultGTEntities.getClientMappedName(list.getEntityType()) );
              mheRef.addReference(ref);
            }
          }
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error converting IEntityDescriptorList at index "
                                       + listIndex
                                       + " in entityDescriptorLists of IEntityDescriptorListSet");
        }
        listIndex++;
      }
      return mheRef;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to convert IEntityDescriptorListSet"
                                  + " into an IGTMheReference",t);
    }   
  }
  
  /**
   * Convert an IGTMheReference into an IEntityDescriptorListSet
   * (UNTESTED METHOD)
   * @param mheRef
   * @return edls
   * @throws GTClientException
   */
  public IEntityDescriptorListSet getEntityDescriptorListSet(IGTMheReference mheRef)
    throws GTClientException
  { //NOTE: WE HAVENT NEEDED THIS METHOD YET AND IT WILL PROBABLY NEED SOME DEBUGGING AS I HAVENT INCLUDED IT IN TESTING
    try
    {
      if (mheRef == null)
        throw new NullPointerException("mheRef is null");
      EntityDescriptorListSet edls = new EntityDescriptorListSet();
      String[] entityTypes = mheRef.getReferencedTypes();
      for(int i=0; i < entityTypes.length; i++)
      {
        String type = DefaultGTEntities.getServerMappedName(entityTypes[i]);
        List references = mheRef.get(entityTypes[i]);
        if(!references.isEmpty())
        {
          //To obtain the keyFieldId for use in the EntityDescriptorList we shall merely grab the key
          //from the first IGTEntityReference - though we will check as we go through that all the other
          //references are made with this key and throw an exception if not - as in that situation the
          //MheReference is not compatible with the EntiyDescriptorListSet.
          Number keyId = ((IGTEntityReference)references.get(0)).getKeyFieldId();
          if (keyId == null)
            throw new NullPointerException("keyId is null");
          EntityDescriptorList list = new EntityDescriptorList(type, keyId);
          Iterator refIterator = references.iterator();
          while(refIterator.hasNext())
          {
            IGTEntityReference ref = (IGTEntityReference)refIterator.next();
            if( !keyId.equals(ref.getKeyFieldId()) )
            {
              throw new UnsupportedOperationException("EntityDescriptorListSet does not"
                                + " support hetrogenous key fields within an entity type");
            }
            EntityDescriptor descriptor = new EntityDescriptor(ref.getKeyValue(), ref.toString() );
            list.addEntityDescriptor(descriptor);
          }
          edls.addEntityDescriptorList(list);
        }      
      }    
      return edls;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to convert multiple hetrogenous entity reference"
                                    + " into an entity descriptor list set",t);
    }
  }
  
  /**
   * A debuging method that will write out the contents of an IEntityDescriptorListSet
   * instance in a human readable form to allow for analysis of the contents
   * @param edls
   * @return edlsString
   */
  public static String getLogableDetails(IEntityDescriptorListSet edls)
  { //20030718AH
    StringBuffer buffer = new StringBuffer();
    buffer.append("IEntityDescriptorListSet:\n");
    buffer.append("{\n");
    if(edls == null)
    {
      buffer.append("  null\n");
    }
    else
    {
      buffer.append("  empty=");
      buffer.append(edls.isEmpty());
      buffer.append("\n");
      Collection lists = edls.getEntityDescriptorLists();
      
      if(lists == null)
      {
        buffer.append("  entityDescriptorLists is null\n");
      }
      else
      {
        int size = lists.size();
        buffer.append("  entityDescriptorLists.size=");
        buffer.append(size);
        buffer.append("\n");
        Iterator iterator = lists.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
          IEntityDescriptorList list = (IEntityDescriptorList)iterator.next();
          buffer.append("  [");
          buffer.append(i);
          buffer.append("]=");
          buffer.append( getLogableDetails(list) );
          i++;
        }
      }
    }
    buffer.append("}\n");
    return buffer.toString();
  }

  public static String getLogableDetails(IEntityDescriptorList edl)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("IEntityDescriptorList\n");
    buffer.append("    {");
    buffer.append("      entityType=");
    buffer.append(edl.getEntityType());
    buffer.append("\n");
    buffer.append("      keyId=");
    buffer.append(edl.getKeyId());
    buffer.append("\n");
    IEntityDescriptor[] descriptors = edl.getEntityDescriptors();
    if(descriptors == null)
    {
      buffer.append("      entityDescriptors is null\n");
    }
    else
    {
      buffer.append("      entityDescriptors.size=");
      buffer.append(descriptors.length);
      buffer.append("\n");
      for(int i=0; i < descriptors.length; i++)
      {
        IEntityDescriptor descriptor = descriptors[i];
        buffer.append("      [");
        buffer.append(i);
        buffer.append("]=");
        if(descriptor == null)
        {
          buffer.append("      null");
        }
        else
        {
          buffer.append( getLogableDetails(descriptor) );
        }
      }
    }
    buffer.append("    }\n");
    return buffer.toString();
  }

  public static String getLogableDetails(IEntityDescriptor descriptor)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("      IEntityDescriptor\n");
    buffer.append("        {");
    buffer.append("          key=");
    buffer.append(descriptor.getKey());
    buffer.append("\n");
    buffer.append("          description=");
    buffer.append(descriptor.getDescription());
    buffer.append("\n"); 
    buffer.append("        }\n");   
    return buffer.toString();
  }

}
