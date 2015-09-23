<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="FILE"/>
	<!-- ****************************************************************************************************************************************************************************** -->
	<xsl:template match="/">
		<xsl:for-each select="/EDI850/PO/POLIN">
			<xsl:if test="string(HEADER/PONum)=$FILE">
                <EDI850>
                <PO>			
				  <POLIN>
					 <xsl:copy-of select="*"/>
				  </POLIN>
				</PO>  
				</EDI850>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
