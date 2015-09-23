/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.entity;


import java.io.File;

/**
 * @author YiJun.Liu
 *
 */
public class RnifAckEntity extends RnifMessageEntity {
	
	private File templateFile = null;
	/**
	 * 
	 */
	public RnifAckEntity() {
		// TODO Auto-generated constructor stub
	}
	public File getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

}
