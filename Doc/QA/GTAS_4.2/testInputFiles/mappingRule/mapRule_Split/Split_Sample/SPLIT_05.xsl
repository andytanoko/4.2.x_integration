<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="ONE"/>
	<xsl:template match="/">
		<xsl:for-each select="/ConsolidatedPO/PO">
			<xsl:if test="string(PO_DocID)=$ONE">
				<PO>
					<xsl:copy-of select="*"/>
				</PO>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
