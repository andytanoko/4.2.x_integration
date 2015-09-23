<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml" indent="yes" />

   <xsl:param name="UDOC" />

   <xsl:template match="GridDocument">
      <GridDocument>
         <xsl:apply-templates />
      </GridDocument>
   </xsl:template>

   <xsl:variable name="test1">
      <xsl:value-of select="document($UDOC)/Pip3A4PurchaseOrderRequest/GlobalDocumentFunctionCode" />
   </xsl:variable>

   <xsl:variable name="test2">
      <xsl:value-of select="document($UDOC)/Pip3A4PurchaseOrderRequest/PurchaseOrder/DocumentReference/ProprietaryDocumentIdentifier" />
   </xsl:variable>

<!--  -->
   <xsl:template match="Custom1">
      <Custom1>
         <xsl:value-of select="$test1" />
      </Custom1>
   </xsl:template>

<!--  -->
   <xsl:template match="Custom2">
      <Custom2>
         <xsl:value-of select="$test2" />
      </Custom2>
   </xsl:template>

<!--  -->
   <xsl:template match="Custom3">
      <Custom3>
         <xsl:value-of select="document($UDOC)/Pip3A4PurchaseOrderRequest/PurchaseOrder/DocumentReference/ProprietaryDocumentIdentifier" />
      </Custom3>
   </xsl:template>

<!--  -->
   <xsl:template match="Custom4">
      <Custom4>
         <xsl:value-of select="document($UDOC)/Pip3A4PurchaseOrderRequest/PurchaseOrder/DocumentReference[GlobalDocumentReferenceTypeCode ='Purchase Order']/ProprietaryDocumentIdentifier" />
      </Custom4>
   </xsl:template>

<!--  -->
   <xsl:template match="Custom5">
      <Custom5>
         <xsl:value-of select="concat( $test1, '**', $test2, '**', document($UDOC)/Pip3A4PurchaseOrderRequest/PurchaseOrder/ProprietaryDocumentIdentifier, '**', document($UDOC)/Pip3A4PurchaseOrderRequest/PurchaseOrder[DocumentReference ='Purchase Order']/ProprietaryDocumentIdentifier )" />
      </Custom5>
   </xsl:template>

   <xsl:template match="*">
      <xsl:copy-of select="." />
   </xsl:template>
</xsl:stylesheet>
