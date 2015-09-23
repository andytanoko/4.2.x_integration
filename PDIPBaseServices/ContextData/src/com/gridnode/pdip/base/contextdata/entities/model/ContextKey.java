package com.gridnode.pdip.base.contextdata.entities.model;

import java.io.*;

public class ContextKey implements Serializable {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -222686280303623803L;
		private String _branchName;
    private String _dataKeyName;
 
    public ContextKey(String dataKeyName,String branchName){
        _dataKeyName=dataKeyName;
        _branchName=branchName;
    }

    public ContextKey(String dataKeyBranchName){
        int ind1=-1,ind2=-1;
        if((ind1=dataKeyBranchName.indexOf("("))>-1 && (ind2=dataKeyBranchName.indexOf(")"))>ind1){
            _dataKeyName=dataKeyBranchName.substring(0,ind1);
            _branchName=dataKeyBranchName.substring(ind1+1,ind2);
        } else _dataKeyName=dataKeyBranchName;
    }

    public String getBranchName(){
        return _branchName;
    }
    public String getDataKeyName(){
        return _dataKeyName;
    }

    public void setBranchName(String branchName){
        _branchName=branchName;
    }
    public void setDataKeyName(String dataKeyName){
        _dataKeyName=dataKeyName;
    }

    public int hashCode(){
        return toString().hashCode();
    }

    public String toString(){
        if(_branchName==null)
            return _dataKeyName;
        return new StringBuffer().append(_dataKeyName).append("(").append(_branchName).append(")").toString();
    }

    public boolean equals(Object obj) {
        if(this==obj)
            return true;
	if (obj != null) {
            return toString().equals(obj.toString());
	}
        return false;
    }
}