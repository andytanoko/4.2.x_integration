<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">
		    <![CDATA[<!DOCTYPE web-j2ee-engine SYSTEM "web-j2ee-engine.dtd">]]>
		</xsl:text>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="jboss-web" name="conv-jboss-web-app">
		<web-j2ee-engine>
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
					<resource-env-ref-name>
						<xsl:value-of select="resource-env-ref-name"/>
					</resource-env-ref-name>
					<jndi-name>
						<xsl:value-of select="jndi-name"/>
					</jndi-name>
				</resource-env-ref>
			</xsl:for-each>
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
			
			<xsl:if test="security-domain">
    			<security-policy-domain>
    			    <xsl:value-of select="security-domain"/>
    			</security-policy-domain>
			</xsl:if>
        </web-j2ee-engine>
    </xsl:template>
</xsl:stylesheet>
