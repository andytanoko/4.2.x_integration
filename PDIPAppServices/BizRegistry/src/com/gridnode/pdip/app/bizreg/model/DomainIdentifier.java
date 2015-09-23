/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomainIdentifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22, 2003   Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.text.MessageFormat;

/**
 * This entity represents an identity of the BusinessEntity
 * under a certain business domain.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class DomainIdentifier extends AbstractEntity implements IDomainIdentifier, Comparable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -209375855236273029L;
	private static final MessageFormat _format = new MessageFormat("{0}/BE:{1}/Domain:{2}/Type:{3}/Value:{4}");
  private static final long _DUMMY = Long.MIN_VALUE;
  private String _domainName;
  private String _type;
  private String _value;
  private Long _beUid;
    
  /**
   * Creates a DomainIdentifier entity with default values.
   */
  public DomainIdentifier()
  {
    setUId(_DUMMY);
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getEntityName()
   */
  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getEntityDescr()
   */
  public String getEntityDescr()
  {
    Object params = new Object[] {
      getEntityName(),
      getBeUid(),
      getDomainName(),
      getType(),
      getValue(),
    };
    return _format.format(params);
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getKeyId()
   */
  public Number getKeyId()
  {
    return UID;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object other)
  {
    boolean equals = false;
    
    if (other != null && getClass().isInstance(other))
    {
      DomainIdentifier oIdentifier = (DomainIdentifier)other;
      equals = (getUId() == oIdentifier.getUId()) && 
               ((getUId() != _DUMMY) ||
                (other.hashCode() == hashCode()));
    }
    
    return equals;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return getEntityDescr().hashCode();
  }
  
  /**
   * Get the UID of the BusinessEntity that this DomainIdentifier identifies.
   * 
   * @return
   */
  public Long getBeUid()
  {
    return _beUid;
  }

  /**
   * Get the Name of the Domain of this Identifier.
   * 
   * @return
   */
  public String getDomainName()
  {
    return _domainName;
  }

  /**
   * Get the Type of this Identifier.
   * 
   * @return
   */
  public String getType()
  {
    return _type;
  }

  /**
   * Gte the Value of this Identifier.
   * 
   * @return
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Sets the UID of the BusinessEntity that this DomainIdentifier belongs to.
   * 
   * @param beUid The UID to set.
   */
  public void setBeUid(Long beUid)
  {
    _beUid = beUid;
  }

  /**
   * Sets the name of the Domain of this Identifier.
   * 
   * @param domainName The DomainName to set.
   * @see IDomainIdentifier#DOMAIN_DUNS
   * @see IDomainIdentifier#DOMAIN_AS2
   * @see IDomainIdentifier#DOMAIN_UDDI
   * @see IDomainIdentifier#DOMAIN_UCCNET
   */
  public void setDomainName(String domainName)
  {
    _domainName = domainName;
  }

  /**
   * Sets the type of this Identifier.
   * 
   * @param type The Type to set.
   * @see IDomainIdentifier#TYPE_DUNS_NUMBER
   * @see IDomainIdentifier#TYPE_AS2_IDENTIFIER
   * @see IDomainIdentifier#TYPE_DISCOVERY_URL
   * @see IDomainIdentifier#TYPE_GLOBAL_LOCATION_ID
   */
  public void setType(String type)
  {
    _type = type;
  }

  /**
   * Sets the Value of this Identifier.
   * 
   * @param value The Value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
    int result = -1;
    // will throw ClassCastException if incompatible type
    if (getClass().isInstance(o))
    {
      result = equals(o) ? 0 : hashCode() - o.hashCode();
    }    
    else
      throw new ClassCastException("Unable to compare "+getClass().getName() + " to "+o.getClass().getName());

    return result;
  }

/*
  public static void main(String[] args)
  {
    DomainIdentifier id1 = new DomainIdentifier();
    DomainIdentifier id2 = new DomainIdentifier();
    DomainIdentifier id3 = new DomainIdentifier();
    DomainIdentifier id4 = new DomainIdentifier();
    id1.setType("aaaa"); id1.setDomainName("bbbb"); id1.setValue("11111"); id1.setUId(11);
    id2.setType("cccc"); id2.setDomainName("dddd"); id2.setValue("22222"); id2.setUId(22);
    
    id3.setType("aaaa"); id3.setDomainName("bbbb"); id3.setValue("11111");
    id4.setType("eeee"); id4.setDomainName("dddd"); id4.setValue("22222"); id4.setUId(22);

    System.out.println("id1 equals id3? "+id1.equals(id3));
    System.out.println("id2 equals id4? "+id2.equals(id4));    
    java.util.ArrayList oldSet = new java.util.ArrayList();
    java.util.ArrayList newSet = new java.util.ArrayList();
    oldSet.add(id1); oldSet.add(id2);
    newSet.add(id2);
    java.util.ArrayList oldSet2 = new java.util.ArrayList(oldSet);
    
    oldSet.removeAll(newSet);
    oldSet2.retainAll(newSet);
    for (java.util.Iterator i=oldSet.iterator(); i.hasNext(); )
    {
      DomainIdentifier id = (DomainIdentifier)i.next();
      System.out.println("OldSet Left over: "+id.getEntityDescr());
    }
    
    for (java.util.Iterator i=oldSet2.iterator(); i.hasNext(); )
    {
      DomainIdentifier id = (DomainIdentifier)i.next();
      System.out.println("OldSet2 Left over: "+id.getEntityDescr());
    }
    
  }
*/  
}
