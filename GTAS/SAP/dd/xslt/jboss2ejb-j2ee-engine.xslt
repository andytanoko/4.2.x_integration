<?xml version="1.0" encoding="UTF-8"?>    
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">
		    <![CDATA[<!DOCTYPE ejb-j2ee-engine SYSTEM "ejb-j2ee-engine.dtd">]]>
		</xsl:text>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="jboss" name="conv-jboss">
		<ejb-j2ee-engine>
			<xsl:call-template name="conv-enterprise-beans"/>
		</ejb-j2ee-engine>
	</xsl:template>
	
	<xsl:template match="enterprise-beans" name="conv-enterprise-beans">
		<xsl:element name="enterprise-beans">
			<xsl:for-each select="enterprise-beans/session">
				<xsl:call-template name="conv-enterprise-bean"/>
			</xsl:for-each>
			<xsl:for-each select="enterprise-beans/entity">
				<xsl:call-template name="conv-enterprise-bean"/>
			</xsl:for-each>
			<xsl:for-each select="enterprise-beans/message-driven">
				<xsl:call-template name="conv-enterprise-bean"/>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="conv-enterprise-bean">
		<enterprise-bean>
			<ejb-name>
				<xsl:value-of select="ejb-name"/>
			</ejb-name>
			<xsl:if test="jndi-name">
				<jndi-name>
					<xsl:value-of select="jndi-name"/>
				</jndi-name>
			</xsl:if>
			<xsl:for-each select="ejb-ref">
				<ejb-ref>
					<ejb-ref-name>
						<xsl:value-of select="ejb-ref-name"/>
					</ejb-ref-name>
					<jndi-name>
						<xsl:value-of select="jndi-name"/>
					</jndi-name>
				</ejb-ref>
			</xsl:for-each>
			<xsl:for-each select="ejb-local-ref">
				<ejb-local-ref>
					<ejb-ref-name>
						<xsl:value-of select="ejb-ref-name"/>
					</ejb-ref-name>
					<jndi-name>
						<xsl:value-of select="local-jndi-name"/>
					</jndi-name>
				</ejb-local-ref>
			</xsl:for-each>
			<xsl:for-each select="resource-ref">
				<resource-ref>
					<res-ref-name>
						<xsl:value-of select="res-ref-name"/>
					</res-ref-name>
					<res-link>
						<xsl:value-of select="jndi-name"/>
					</res-link>
				</resource-ref>
			</xsl:for-each>
			<xsl:for-each select="resource-env-ref">
				<resource-env-ref>
					<res-env-ref-name>
						<xsl:value-of select="resource-env-ref-name"/>
					</res-env-ref-name>
					<jndi-name>
						<xsl:value-of select="jndi-name"/>
					</jndi-name>
				</resource-env-ref>
			</xsl:for-each>
			<xsl:if test="local-name(.)='session'">
			    <xsl:call-template name="write-session-props"/>
			</xsl:if>
			<xsl:if test="local-name(.)='entity'">
			    <xsl:call-template name="write-entity-props"/>
			</xsl:if>
			<xsl:if test="local-name(.)='message-driven'">
			    <xsl:call-template name="write-message-props"/>
			</xsl:if>
		</enterprise-bean>
	</xsl:template>
	
	<xsl:template name="write-session-props">
	    <xsl:variable name="ConfigurationName" select="normalize-space(configuration-name)"/>
	    <xsl:variable name="ContainerConfig" select="../../container-configurations/container-configuration[normalize-space(container-name)=$ConfigurationName]"/>
	    
	    <xsl:variable name="SessionTimeout" select="$ContainerConfig/container-cache-conf/cache-policy-conf/max-bean-life"/>
	    <xsl:variable name="PassiveTimeout" select="$ContainerConfig/container-cache-conf/cache-policy-conf/max-bean-age"/>
	    <xsl:variable name="LruLimit" select="$ContainerConfig/container-cache-conf/cache-policy-conf/max-capacity"/>
	    <xsl:variable name="InitialPoolSize" select="$ContainerConfig/container-pool-conf/MinimumSize"/>
	    <xsl:variable name="MaxPoolSize" select="$ContainerConfig/container-pool-conf/MaximumSize"/>
	    
	    <session-props>
            <xsl:if test="$SessionTimeout">
                <session-timeout>
                    <xsl:value-of select="$SessionTimeout"/>
                </session-timeout>
            </xsl:if>
            <xsl:if test="$PassiveTimeout and $LruLimit">
                <passivation>
                    <passive-timeout>
                        <xsl:value-of select="$PassiveTimeout"/>
                    </passive-timeout>
                    <lrulimit>
                        <xsl:value-of select="$LruLimit"/>
                    </lrulimit>            
                </passivation>
            </xsl:if>
            <xsl:call-template name="write-pool-size">
                <xsl:with-param name="pContainerConfig" select="$ContainerConfig"/>
            </xsl:call-template>
        </session-props>
	</xsl:template>
	
	<xsl:template name="write-entity-props">
	    <xsl:variable name="ConfigurationName" select="normalize-space(configuration-name)"/>
	    <xsl:variable name="ContainerConfig" select="../../container-configurations/container-configuration[normalize-space(container-name)=$ConfigurationName]"/>
	    
	    <xsl:variable name="InitialCacheSize" select="$ContainerConfig/container-cache-conf/cache-policy-conf/min-capacity"/>
	    <xsl:variable name="InitialPoolSize" select="$ContainerConfig/container-pool-conf/MinimumSize"/>
	    <xsl:variable name="MaxPoolSize" select="$ContainerConfig/container-pool-conf/MaximumSize"/>
	    
	    <entity-props>
	        <xsl:if test="$InitialCacheSize">
                <initial-cache-size>
                    <xsl:value-of select="$InitialCacheSize"/>
                </initial-cache-size>       
            </xsl:if>
            
            <xsl:call-template name="write-pool-size">
                <xsl:with-param name="pContainerConfig" select="$ContainerConfig"/>
            </xsl:call-template>
        </entity-props>
	</xsl:template>
	
	<xsl:template name="write-message-props">
	    <xsl:variable name="ConfigurationName" select="normalize-space(configuration-name)"/>
	    <xsl:variable name="ContainerConfig" select="../../container-configurations/container-configuration[normalize-space(container-name)=$ConfigurationName]"/>
	    
	    <xsl:variable name="DestinationName" select="destination-jndi-name"/>
	    <xsl:variable name="InitialPoolSize" select="$ContainerConfig/container-pool-conf/MinimumSize"/>
	    <xsl:variable name="MaxPoolSize" select="$ContainerConfig/container-pool-conf/MaximumSize"/>
	    
	    <message-props>
	        <xsl:if test="$DestinationName">
                <destination-name>
                    <xsl:value-of select="$DestinationName"/>
                </destination-name>       
            </xsl:if>
            
            <xsl:call-template name="write-pool-size">
                <xsl:with-param name="pContainerConfig" select="$ContainerConfig"/>
            </xsl:call-template>
        </message-props>
	</xsl:template>
	
	<xsl:template name="write-pool-size">
	    <xsl:param name="pContainerConfig"/>
	    
	    <xsl:variable name="InitialPoolSize" select="$pContainerConfig/container-pool-conf/MinimumSize"/>
	    <xsl:variable name="MaxPoolSize" select="$pContainerConfig/container-pool-conf/MaximumSize"/>
	    
	    <xsl:if test="$InitialPoolSize">
            <property>
                <property-name>InitialSize</property-name>
                <property-value>
                    <xsl:value-of select="$InitialPoolSize"/>
                </property-value>
            </property>
        </xsl:if>
        <xsl:if test="$MaxPoolSize">
            <property>
                <property-name>MaxSize</property-name>
                <property-value>
                    <xsl:value-of select="$MaxPoolSize"/>
                </property-value>
            </property>
        </xsl:if>
	</xsl:template>
</xsl:stylesheet>
