<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="GridDocument">
		<GridDocument>
			<GdocId><xsl:value-of select="ListView/GridDocID"/></GdocId>
			<GdocFilename><xsl:value-of select="ListView/GDocFilename"/></GdocFilename>
			<Folder>Outbound</Folder>
			<SenderPartnerId><xsl:value-of select="ListView/SPartnerID"/></SenderPartnerId>
			<UdocNum><xsl:value-of select="ListView/UDocNum"/></UdocNum>
			<RefUdocNum><xsl:value-of select="ListView/RefUDocNum"/></RefUdocNum>
			<UdocFilename><xsl:value-of select="ListView/UDocFilename"/></UdocFilename>
			<UdocVersion><xsl:value-of select="ListView/UDocVersion"/></UdocVersion>
			<UdocDocType><xsl:value-of select="ListView/UDocDocumentType"/></UdocDocType>
			<UdocFileType><xsl:value-of select="ListView/UDocFileType"/></UdocFileType>
			<UniqueDocIdentifier></UniqueDocIdentifier>
			<UdocFullPath></UdocFullPath>
			<ExportedUdocFullPath></ExportedUdocFullPath>
			<SrcFolder>Import</SrcFolder>
			<NotifyUserEmail><xsl:value-of select="ListView/NotifyUserEmail"/></NotifyUserEmail>
			<UdocFileSize><xsl:value-of select="ListView/UDocFileSize"/></UdocFileSize>
			<isExported><xsl:value-of select="ListView/Status/IsExported"/></isExported>
			<isViewAckReq><xsl:value-of select="ListView/Status/IsViewAckReq"/></isViewAckReq>
			<isExportAckReq><xsl:value-of select="ListView/Status/IsExportAckReq"/></isExportAckReq>
			<isReceiveAckReq><xsl:value-of select="ListView/Status/IsReceiveAckReq"/></isReceiveAckReq>
			<isViewed>false</isViewed>
			<isSent><xsl:value-of select="ListView/Status/IsSent"/></isSent>
			<isLocalPending><xsl:value-of select="ListView/Status/IsLocalPending"/></isLocalPending>
			<isExpired>false</isExpired>
			<isRecAckProcessed>false</isRecAckProcessed>
			<CreateBy><xsl:value-of select="Details/System/Creator"/></CreateBy>
      <xsl:if test="count(Details/SenderInfo/SNodeID)!=0">
  			<SenderNodeId><xsl:value-of select="Details/SenderInfo/SNodeID"/></SenderNodeId>
      </xsl:if>
      <xsl:if test="count(Details/SenderInfo/SGDocID)!=0">
			  <SenderGdocId><xsl:value-of select="Details/SenderInfo/SGDocID"/></SenderGdocId>
      </xsl:if>
			<SenderRoute><xsl:value-of select="Details/SenderInfo/SRoute"/></SenderRoute>
			<SenderPartnerFunction><xsl:value-of select="Details/Mapping/SPartnerFunction"/></SenderPartnerFunction>
			<SenderUserId><xsl:value-of select="Details/SenderInfo/SUserID"/></SenderUserId>
			<SenderUserName><xsl:value-of select="Details/SenderInfo/SUserName"/></SenderUserName>
			<SenderBizEntityId><xsl:value-of select="Details/SenderInfo/SBusinessEntityID"/></SenderBizEntityId>
			<SenderBizEntityUuid><xsl:value-of select="Details/SenderInfo/SUUID"/></SenderBizEntityUuid>
			<SenderRegistryQueryUrl></SenderRegistryQueryUrl>
			<SenderPartnerType></SenderPartnerType>
			<SenderPartnerGroup></SenderPartnerGroup>
			<SenderPartnerName></SenderPartnerName>
      <xsl:if test="count(Mapping/RefGDocID)!=0">
  			<RefGdocId><xsl:value-of select="Mapping/RefGDocID"/></RefGdocId>
      </xsl:if>
			<RefUdocFilename><xsl:value-of select="Details/Mapping/RefUDocFilename"/></RefUdocFilename>
			<RecipientNodeId><xsl:value-of select="Details/Transport/GridNodeID"/></RecipientNodeId>
			<RecipientPartnerId><xsl:value-of select="Details/Transport/RPartnerID"/></RecipientPartnerId>
			<RecipientPartnerFunction><xsl:value-of select="Details/Transport/RPartnerFunction"/></RecipientPartnerFunction>
			<EncryptionLevel><xsl:value-of select="Details/Transport/EncryptionLevel"/></EncryptionLevel>
			<RecipientPartnerType><xsl:value-of select="Details/Transport/RPartnerType"/></RecipientPartnerType>
			<RecipientPartnerGroup><xsl:value-of select="Details/Transport/RPartnerGrp"/></RecipientPartnerGroup>
			<RecipientPartnerName><xsl:value-of select="ListView/RPartnerName"/></RecipientPartnerName>
			<RecipientBizEntityId><xsl:value-of select="Details/Transport/BusinessEntityID"/></RecipientBizEntityId>
			<RecipientBizEntityUuid><xsl:value-of select="Details/Transport/UUID"/></RecipientBizEntityUuid>
			<RecipientRegistryQueryUrl></RecipientRegistryQueryUrl>
      <xsl:if test="count(Details/Transport/RGDocID)!=0">
  			<RecipientGdocId><xsl:value-of select="Details/Transport/RGDocID"/></RecipientGdocId>
      </xsl:if>
      <xsl:if test="count(RecipientChannelUid)!=0">
  			<RecipientChannelUid></RecipientChannelUid>
      </xsl:if>
			<RecipientChannelName></RecipientChannelName>
			<RecipientChannelProtocol></RecipientChannelProtocol>
      <xsl:if test="count(PortUid)!=0">
   			<PortUid></PortUid>
      </xsl:if>
			<PortName></PortName>
			<DateTimeCreate><xsl:value-of select="Details/History/DTCreated"/></DateTimeCreate>
	<xsl:choose>
		<xsl:when test="Details/SenderInfo/SRoute='DIRECT'">		
			<DateTimeSendEnd><xsl:value-of select="Details/History/DTSendOutEnd"/></DateTimeSendEnd>
			<!--DateTimeReceiveEnd></DateTimeReceiveEnd-->
			<DateTimeSendStart><xsl:value-of select="Details/History/DTSendOutStart"/></DateTimeSendStart>
		</xsl:when>
		<xsl:otherwise>
			<DateTimeSendEnd><xsl:value-of select="Details/History/DTUploadEnd"/></DateTimeSendEnd>
			<DateTimeSendStart><xsl:value-of select="Details/History/DTUploadStart"/></DateTimeSendStart>
		</xsl:otherwise>
	</xsl:choose>		
			<DateTimeTransComplete><xsl:value-of select="Details/History/DTTransactionComplete"/></DateTimeTransComplete>
			<DateTimeExport><xsl:value-of select="Details/History/DTExported"/></DateTimeExport>
			<DateTimeImport><xsl:value-of select="Details/History/DTImported"/></DateTimeImport>
			<!--DateTimeReceiveStart></DateTimeReceiveStart-->
			<DateTimeView><xsl:value-of select="Details/History/DTView"/></DateTimeView>
			<!--DateTimeRecipientView></DateTimeRecipientView>
			<DateTimeRecipientExport></DateTimeRecipientExport-->
			<PartnerFunction><xsl:value-of select="Details/Mapping/SPartnerFunction"/></PartnerFunction>
			<ActionCode></ActionCode>
     <!--
      <xsl:if test="count(RnProfileUid)!=0">
  			<RnProfileUid><xsl:value-of select="RnProfileUid"/></RnProfileUid>
      </xsl:if>
      -->
     	<xsl:if test="count(ListView/ProcessDefName)!=0">
  			<ProcessDefId><xsl:value-of select="ListView/ProcessDefName"/></ProcessDefId>
      	</xsl:if>
			<isRequest></isRequest>
			<Custom1><xsl:value-of select="Details/Custom/Custom1"/></Custom1>
			<Custom2><xsl:value-of select="Details/Custom/Custom2"/></Custom2>
			<Custom3><xsl:value-of select="Details/Custom/Custom3"/></Custom3>
			<Custom4><xsl:value-of select="Details/Custom/Custom4"/></Custom4>
			<Custom5><xsl:value-of select="Details/Custom/Custom5"/></Custom5>
			<Custom6><xsl:value-of select="Details/Custom/Custom6"/></Custom6>
			<Custom7><xsl:value-of select="Details/Custom/Custom7"/></Custom7>
			<Custom8><xsl:value-of select="Details/Custom/Custom8"/></Custom8>
			<Custom9><xsl:value-of select="Details/Custom/Custom9"/></Custom9>
			<Custom10><xsl:value-of select="Details/Custom/Custom10"/></Custom10>
	   <!--
	    <xsl:if test="count(ProcessInstanceUid)!=0">
		    <ProcessInstanceUid></ProcessInstanceUid>
    		    <UserTrackingID><xsl:value-of select="UserTrackingID"/></UserTrackingID>	
            </xsl:if>
	    -->
	    <xsl:if test="count(ListView/ProcessInstanceID)!=0">
		    <ProcessInstanceID><xsl:value-of select="ListView/ProcessInstanceID"/></ProcessInstanceID>
        </xsl:if>
			<hasAttachment><xsl:value-of select="ListView/HasAttachment"/></hasAttachment>
			<isAttachmentLinkUpdated>false</isAttachmentLinkUpdated>
			<xsl:for-each select="Details/Attachments/AttachmentUID">
			    <AttachmentUid><xsl:value-of select="."/></AttachmentUid>
			</xsl:for-each>			
		</GridDocument>
	</xsl:template>
</xsl:stylesheet>
