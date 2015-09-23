/* Generated by Together */

package com.gridnode.pdip.framework.file.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import java.io.*;

import com.gridnode.pdip.framework.file.access.MultiFileBlock;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

//import com.gridnode.pdip.framework.filemgr.ejb.IPdipTransfer;

public interface IFileMgr extends EJBObject {
    String createFolder(String domain, String newPath) throws RemoteException, EJBException;

    String createFile(String domain, String path, byte[] file, boolean overwrite) throws RemoteException, IOException, EJBException;

    String moveFolder(String domain, String src, String des, boolean overwrite) throws RemoteException, EJBException;

    String moveFile(String domain, String src, String des, boolean overwrite) throws RemoteException, EJBException;

    String copyFolder(String domain, String src, String des, boolean overwrite) throws RemoteException, EJBException;

    String copyFile(String domain, String src, String des, boolean overwrite) throws RemoteException, EJBException;

    String deleteFolder(String domain, String delPath) throws RemoteException, EJBException;

    String deleteFile(String domain, String delPath) throws RemoteException, EJBException;

    byte[] getFile(String domain, String path) throws RemoteException, EJBException;

    byte[] readFromFile(String domain, String path, int block, int len) throws RemoteException, EJBException;

    // Modified by Kan Mun (21/03/2002): Change block to offset since block is no use.
    byte[] readByteFromFile(String domain, String path, long offset, int len) throws RemoteException, EJBException, EOFException, FileAccessException;

    byte[] readFromFileZipped(String domain, String path, long offset, int len) throws RemoteException, EJBException;

    MultiFileBlock readFromStreamZipped(String domain, String[] paths, long offset, int len) throws RemoteException, EJBException;

    boolean exist(String domain, String path) throws RemoteException, EJBException;

    long length(String domain, String path) throws RemoteException, EJBException;

    public boolean createNewFile(String domain, String newPath) throws RemoteException, EJBException;

//    void closeTransfer(IPdipTransfer remote) throws RemoteException, EJBException;

//    IPdipTransfer openTransfer(String domain, String path, int op) throws RemoteException, EJBException;

    /** @link dependency */
    /*#FileMgrBean lnkSession1Bean;*/
}