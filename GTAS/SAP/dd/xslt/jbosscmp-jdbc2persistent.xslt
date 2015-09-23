<?xml version="1.0" encoding="UTF-8"?>
    
    <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="ejb-jar.xml" select="ejb-jar.xml"/>
	
	<xsl:variable name="EjbJarNodes" select="document($ejb-jar.xml)"/>
	
	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">
		    <![CDATA[<!DOCTYPE persistent-ejb-map SYSTEM "persistent.dtd">]]>
		</xsl:text>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="jbosscmp-jdbc">
		<persistent-ejb-map>
        	<locking type="Local"/>
        	<db-properties>
        		<data-source-name>
        		    <xsl:value-of select="defaults/datasource"/>
        		</data-source-name>
        	</db-properties>
        	<entity-beans>
        	    <xsl:for-each select="enterprise-beans/entity">
        	        <entity-bean>
            			<ejb-name>
            			    <xsl:value-of select="ejb-name"/>
            			</ejb-name>
            			<table-name>
            			    <xsl:value-of select="table-name"/>
            			</table-name>
            			
            			<xsl:call-template name="conv-cmp-fields"/>
            			<xsl:call-template name="conv-finder">
            			    <xsl:with-param name="pEjbName" select="ejb-name"/>
            			</xsl:call-template>
            			
            		</entity-bean>
            		
        		</xsl:for-each>
        	</entity-beans>
        	
        	<xsl:call-template name="conv-relationships"/>
        	
        </persistent-ejb-map>
	</xsl:template>
	
	<xsl:template name="conv-cmp-fields">
	    <xsl:for-each select="cmp-field">
	        
	        <xsl:variable name="EjbName" select="../ejb-name"/>
	        <xsl:variable name="EjbJarPkField" 
	                      select="$EjbJarNodes/ejb-jar/enterprise-beans/entity[ejb-name=$EjbName]/primkey-field"/>
	        
	        <xsl:if test="field-name=$EjbJarPkField">
        	    <field-map 
        			key-type="PrimaryKey">
        			<field-name><xsl:value-of select="field-name"/></field-name>
        			<column>
        				<column-name><xsl:value-of select="column-name"/></column-name>
        			</column>
        		</field-map>
    		</xsl:if>
    		
    		<xsl:if test="field-name!=$EjbJarPkField">
        	    <field-map 
        			key-type="NoKey">
        			<field-name><xsl:value-of select="field-name"/></field-name>
        			<column>
        				<column-name><xsl:value-of select="column-name"/></column-name>
        			</column>
        		</field-map>
    		</xsl:if>
    		
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="conv-finder">
	    <xsl:param name="pEjbName"/>
	    <xsl:for-each select="$EjbJarNodes/ejb-jar/enterprise-beans/entity">
		    <xsl:variable name="EjbJarEjbName" select="ejb-name"/>
		    
		    <xsl:if test="$EjbJarEjbName=$pEjbName">
		        
		        <xsl:for-each select="query">
        			<finder-descriptor>
        				<method-name>   
        				    <xsl:value-of select="query-method/method-name"/>
        				</method-name>
        				<method-params>
        				    <xsl:for-each select="query-method/method-params/method-param">
        					    <method-param>
        					        <xsl:value-of select="."/>
        					    </method-param>
        					</xsl:for-each>
        				</method-params>
        				<load-selected-objects 
        					lock="read"/>
        			</finder-descriptor>
    			</xsl:for-each>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="conv-relationships">
	    
	    <xsl:variable name="Relationships" select="relationships"/>
	    <relationships>
    	    <xsl:for-each select="$EjbJarNodes/ejb-jar/relationships/ejb-relation">
    	        <table-relation>
    	        
        	        <xsl:variable name="EjbJarRelationName" select="ejb-relation-name"/>
        	        <xsl:variable name="EjbJarFirstRelationRole" 
        	                      select="ejb-relationship-role[1]"/>           
        	        <xsl:variable name="EjbJarSecondRelationRole" 
        	                      select="ejb-relationship-role[2]"/>
        	                      
        	        <xsl:variable name="FirstRelationRole" 
        	                      select="$Relationships/ejb-relation[ejb-relation-name=$EjbJarRelationName]/ejb-relationship-role[ejb-relationship-role-name=$EjbJarFirstRelationRole/ejb-relationship-role-name]"/>           
        	        <xsl:variable name="SecondRelationRole" 
        	                      select="$Relationships/ejb-relation[ejb-relation-name=$EjbJarRelationName]/ejb-relationship-role[ejb-relationship-role-name=$EjbJarSecondRelationRole/ejb-relationship-role-name]"/>
        	        
        	        <!-- handle a one-to-many relation -->
        	        <xsl:if test="$EjbJarFirstRelationRole/multiplicity!=$EjbJarSecondRelationRole/multiplicity">
        	            
        	            <!-- write ONE and MANY roles in the same order as found in ejb-jar.xml -->
        	            <xsl:if test="$EjbJarFirstRelationRole/multiplicity='One'">
        	                <xsl:variable name="EjbJarOneRole" select="$EjbJarFirstRelationRole"/>	                
        	                <xsl:call-template name="write_relation_role">
        	                    <xsl:with-param name="pKeyType" select="'PrimaryKey'"/>
        	                    <xsl:with-param name="pEjbName" select="$EjbJarOneRole/relationship-role-source/ejb-name"/>
        	                    <xsl:with-param name="pCmrFieldName" select="$EjbJarOneRole/cmr-field/cmr-field-name"/>
        	                </xsl:call-template> 			
        	            </xsl:if>
        	            <xsl:if test="$EjbJarFirstRelationRole/multiplicity='Many'">
        	                <xsl:variable name="EjbJarManyRole" select="$EjbJarFirstRelationRole"/>
        	                <xsl:variable name="OneRole" select="$SecondRelationRole"/>
        	                <xsl:call-template name="write_relation_role">
        	                    <xsl:with-param name="pKeyType" select="'NoKey'"/>
        	                    <xsl:with-param name="pEjbName" select="$EjbJarManyRole/relationship-role-source/ejb-name"/>
        	                    <xsl:with-param name="pCmrFieldName" select="$EjbJarManyRole/cmr-field/cmr-field-name"/>
        	                    <xsl:with-param name="pPkFieldName" select="$OneRole/key-fields/key-field/field-name"/>
        	                    <xsl:with-param name="pFkColumnName" select="$OneRole/key-fields/key-field/column-name"/>
        	                </xsl:call-template>
        	            </xsl:if>
        	            
        	            <xsl:if test="$EjbJarSecondRelationRole/multiplicity='One'">
        	                <xsl:variable name="EjbJarOneRole" select="$EjbJarSecondRelationRole"/>
        	                <xsl:call-template name="write_relation_role">
        	                    <xsl:with-param name="pKeyType" select="'PrimaryKey'"/>
        	                    <xsl:with-param name="pEjbName" select="$EjbJarOneRole/relationship-role-source/ejb-name"/>
        	                    <xsl:with-param name="pCmrFieldName" select="$EjbJarOneRole/cmr-field/cmr-field-name"/>
        	                </xsl:call-template>
        	            </xsl:if>	            
        	            <xsl:if test="$EjbJarSecondRelationRole/multiplicity='Many'">
        	                <xsl:variable name="EjbJarManyRole" select="$EjbJarSecondRelationRole"/>
        	                <xsl:variable name="OneRole" select="$FirstRelationRole"/>
        	                <xsl:call-template name="write_relation_role">
        	                    <xsl:with-param name="pKeyType" select="'NoKey'"/>
        	                    <xsl:with-param name="pEjbName" select="$EjbJarManyRole/relationship-role-source/ejb-name"/>
        	                    <xsl:with-param name="pCmrFieldName" select="$EjbJarManyRole/cmr-field/cmr-field-name"/>
        	                    <xsl:with-param name="pPkFieldName" select="$OneRole/key-fields/key-field/field-name"/>
        	                    <xsl:with-param name="pFkColumnName" select="$OneRole/key-fields/key-field/column-name"/>
        	                </xsl:call-template>
        	            </xsl:if>
        	        </xsl:if>
        	        
        	        <!-- handle a many-to-many relation -->
        	        <xsl:if test="$EjbJarFirstRelationRole/multiplicity=$EjbJarSecondRelationRole/multiplicity
        	                      and $EjbJarFirstRelationRole/multiplicity='Many'">
        	            <help-table>
        	                <xsl:value-of select="$FirstRelationRole/../relation-table-mapping/table-name"/>
        	            </help-table>
        	            <xsl:call-template name="write_relation_role">
        	                <xsl:with-param name="pKeyType" select="'PrimaryKey'"/>
                            <xsl:with-param name="pEjbName" select="$EjbJarFirstRelationRole/relationship-role-source/ejb-name"/>
                            <xsl:with-param name="pCmrFieldName" select="$EjbJarFirstRelationRole/cmr-field/cmr-field-name"/>
                            <xsl:with-param name="pPkFieldName" select="$FirstRelationRole/key-fields/key-field/field-name"/>
        	                <xsl:with-param name="pFkColumnName" select="$FirstRelationRole/key-fields/key-field/column-name"/>
                        </xsl:call-template>
                        <xsl:call-template name="write_relation_role">
                            <xsl:with-param name="pKeyType" select="'PrimaryKey'"/>
                            <xsl:with-param name="pEjbName" select="$EjbJarSecondRelationRole/relationship-role-source/ejb-name"/>
                            <xsl:with-param name="pCmrFieldName" select="$EjbJarSecondRelationRole/cmr-field/cmr-field-name"/>
                            <xsl:with-param name="pPkFieldName" select="$SecondRelationRole/key-fields/key-field/field-name"/>
                            <xsl:with-param name="pFkColumnName" select="$SecondRelationRole/key-fields/key-field/column-name"/>
                        </xsl:call-template>
        	        </xsl:if>
        	    </table-relation>
    	    </xsl:for-each>
	    </relationships>
	</xsl:template>
	
	<xsl:template name="write_relation_role">
	    <xsl:param name="pKeyType"/>
	    <xsl:param name="pEjbName"/>
	    <xsl:param name="pCmrFieldName"/>
	    <xsl:param name="pPkFieldName"/>
	    <xsl:param name="pFkColumnName"/>
	    
	    <table-relationship-role>
	        <xsl:attribute name="key-type">
	            <xsl:value-of select="$pKeyType"/>
	        </xsl:attribute>
			<ejb-name>
			    <xsl:value-of select="$pEjbName"/>
			</ejb-name>
			<cmr-field>
			    <xsl:value-of select="$pCmrFieldName"/>
			</cmr-field>
			<xsl:if test="$pFkColumnName">
    			<fk-column>
    				<column-name>
    				    <xsl:value-of select="$pFkColumnName"/>
    				</column-name>
    				<pk-field-name>
    				    <xsl:value-of select="$pPkFieldName"/>
    				</pk-field-name>
    			</fk-column>
			</xsl:if>
		</table-relationship-role>
	</xsl:template>
	
</xsl:stylesheet>
