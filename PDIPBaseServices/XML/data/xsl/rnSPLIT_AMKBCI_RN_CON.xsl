<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="RECEIVER"/>

	<xsl:template match="AMKBCI">
		<xsl:apply-templates/>
	</xsl:template>
	
	
	<xsl:template match="Pip3C3InvoiceNotification">
		<xsl:if test="string(toRole/PartnerRoleDescription/PartnerDescription/BusinessDescription/SeagateProprietaryVendorNumber)=$RECEIVER">
			<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE Pip3C3InvoiceNotification SYSTEM "3C3_MS_R01_00_InvoiceNotification.dtd"&gt;</xsl:text>
			
			<Pip3C3InvoiceNotification>
				<xsl:copy-of select="*"/>
			</Pip3C3InvoiceNotification>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
