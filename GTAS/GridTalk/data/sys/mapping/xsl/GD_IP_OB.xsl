<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="GridDocument">
		<GridDocument>
			<GdocId><xsl:value-of select="GdocId"/></GdocId>
			<GdocFilename><xsl:value-of select="GdocFilename"/></GdocFilename>
			<Folder><xsl:value-of select="Folder"/></Folder>
			<SenderPartnerId><xsl:value-of select="SenderPartnerId"/></SenderPartnerId>
      <xsl:if test="count(UdocNum)!=0">
			  <UdocNum><xsl:value-of select="UdocNum"/></UdocNum>
      </xsl:if>
      <xsl:if test="count(RefUdocNum)!=0">
 			  <RefUdocNum><xsl:value-of select="RefUdocNum"/></RefUdocNum>
      </xsl:if>
      <xsl:if test="count(UdocFilename)!=0">
			  <UdocFilename><xsl:value-of select="UdocFilename"/></UdocFilename>
      </xsl:if>
			<UdocVersion><xsl:value-of select="UdocVersion"/></UdocVersion>
			<UdocDocType><xsl:value-of select="UdocDocType"/></UdocDocType>
			<UdocFileType><xsl:value-of select="UdocFileType"/></UdocFileType>
			<UniqueDocIdentifier><xsl:value-of select="UniqueDocIdentifier"/></UniqueDocIdentifier>
			<UdocFullPath></UdocFullPath>
			<ExportedUdocFullPath></ExportedUdocFullPath>
			<SrcFolder><xsl:value-of select="SrcFolder"/></SrcFolder>
			<NotifyUserEmail/>
			<UdocFileSize><xsl:value-of select="UdocFileSize"/></UdocFileSize>
			<isExported><xsl:value-of select="isExported"/></isExported>
			<isViewAckReq><xsl:value-of select="isViewAckReq"/></isViewAckReq>
			<isExportAckReq><xsl:value-of select="isExportAckReq"/></isExportAckReq>
			<isReceiveAckReq><xsl:value-of select="isReceiveAckReq"/></isReceiveAckReq>
			<isViewed>false</isViewed>
			<isSent></isSent>
			<isLocalPending><xsl:value-of select="isLocalPending"/></isLocalPending>
			<isExpired><xsl:value-of select="isExpired"/></isExpired>
			<isRecAckProcessed><xsl:value-of select="isRecAckProcessed"/></isRecAckProcessed>
			<CreateBy><xsl:value-of select="CreateBy"/></CreateBy>
      <xsl:if test="count(SenderNodeId)!=0">
  			<SenderNodeId><xsl:value-of select="SenderNodeId"/></SenderNodeId>
      </xsl:if>
      <xsl:if test="count(SenderGdocId)!=0">
		  	<SenderGdocId><xsl:value-of select="SenderGdocId"/></SenderGdocId>
      </xsl:if>
			<SenderRoute><xsl:value-of select="SenderRoute"/></SenderRoute>
			<SenderPartnerFunction><xsl:value-of select="SenderPartnerFunction"/></SenderPartnerFunction>
			<SenderUserId><xsl:value-of select="SenderUserId"/></SenderUserId>
			<SenderUserName><xsl:value-of select="SenderUserName"/></SenderUserName>
			<SenderBizEntityId><xsl:value-of select="SenderBizEntityId"/></SenderBizEntityId>
			<SenderBizEntityUuid><xsl:value-of select="SenderBizEntityUuid"/></SenderBizEntityUuid>
			<SenderRegistryQueryUrl><xsl:value-of select="SenderRegistryQueryUrl"/></SenderRegistryQueryUrl>
			<SenderPartnerType><xsl:value-of select="SenderPartnerType"/></SenderPartnerType>
			<SenderPartnerGroup><xsl:value-of select="SenderPartnerGroup"/></SenderPartnerGroup>
			<SenderPartnerName><xsl:value-of select="SenderPartnerName"/></SenderPartnerName>
			<RefGdocId><xsl:value-of select="RefGdocId"/></RefGdocId>
			<RefUdocFilename><xsl:value-of select="RefUdocFilename"/></RefUdocFilename>
      <xsl:if test="count(RecipientNodeId)!=0">
  			<RecipientNodeId><xsl:value-of select="RecipientNodeId"/></RecipientNodeId>
      </xsl:if>
			<RecipientPartnerId><xsl:value-of select="RecipientPartnerId"/></RecipientPartnerId>
			<RecipientPartnerFunction><xsl:value-of select="RecipientPartnerFunction"/></RecipientPartnerFunction>
			<EncryptionLevel><xsl:value-of select="EncryptionLevel"/></EncryptionLevel>
			<RecipientPartnerType><xsl:value-of select="RecipientPartnerType"/></RecipientPartnerType>
			<RecipientPartnerGroup><xsl:value-of select="RecipientPartnerGroup"/></RecipientPartnerGroup>
			<RecipientPartnerName><xsl:value-of select="RecipientPartnerName"/></RecipientPartnerName>
			<RecipientBizEntityId><xsl:value-of select="RecipientBizEntityId"/></RecipientBizEntityId>
			<RecipientBizEntityUuid><xsl:value-of select="RecipientBizEntityUuid"/></RecipientBizEntityUuid>
			<RecipientRegistryQueryUrl><xsl:value-of select="RecipientRegistryQueryUrl"/></RecipientRegistryQueryUrl>
      <xsl:if test="count(RecipientGdocId)!=0">
  			<RecipientGdocId><xsl:value-of select="RecipientGdocId"/></RecipientGdocId>
      </xsl:if>
      <xsl:if test="count(RecipientChannelUid)!=0">
  			<RecipientChannelUid><xsl:value-of select="RecipientChannelUid"/></RecipientChannelUid>
      </xsl:if>
			<RecipientChannelName><xsl:value-of select="RecipientChannelName"/></RecipientChannelName>
			<RecipientChannelProtocol><xsl:value-of select="RecipientChannelProtocol"/></RecipientChannelProtocol>
      <xsl:if test="count(PortUid)!=0">
  			<PortUid><xsl:value-of select="PortUid"/></PortUid>
      </xsl:if>
			<PortName><xsl:value-of select="PortName"/></PortName>
			<DateTimeCreate><xsl:value-of select="DateTimeCreate"/></DateTimeCreate>
			<DateTimeSendEnd><xsl:value-of select="DateTimeSendEnd"/></DateTimeSendEnd>
			<DateTimeReceiveEnd><xsl:value-of select="DateTimeReceiveEnd"/></DateTimeReceiveEnd>
			<DateTimeSendStart><xsl:value-of select="DateTimeSendStart"/></DateTimeSendStart>
			<DateTimeTransComplete><xsl:value-of select="DateTimeTransComplete"/></DateTimeTransComplete>
			<DateTimeExport><xsl:value-of select="DateTimeExport"/></DateTimeExport>
			<DateTimeImport><xsl:value-of select="DateTimeImport"/></DateTimeImport>
			<DateTimeReceiveStart><xsl:value-of select="DateTimeReceiveStart"/></DateTimeReceiveStart>
			<DateTimeView><xsl:value-of select="DateTimeView"/></DateTimeView>
			<DateTimeRecipientView><xsl:value-of select="DateTimeRecipientView"/></DateTimeRecipientView>
			<DateTimeRecipientExport><xsl:value-of select="DateTimeRecipientExport"/></DateTimeRecipientExport>
			<PartnerFunction><xsl:value-of select="PartnerFunction"/></PartnerFunction>
			<ActionCode><xsl:value-of select="ActionCode"/></ActionCode>
      <xsl:if test="count(RnProfileUid)!=0">
			  <RnProfileUid><xsl:value-of select="RnProfileUid"/></RnProfileUid>
      </xsl:if>
	    <xsl:if test="count(ProcessDefId)!=0">
		    <ProcessDefId><xsl:value-of select="ProcessDefId"/></ProcessDefId>
      </xsl:if>
			<isRequest><xsl:value-of select="isRequest"/></isRequest>
			<Custom1><xsl:value-of select="Custom1"/></Custom1>
			<Custom2><xsl:value-of select="Custom2"/></Custom2>
			<Custom3><xsl:value-of select="Custom3"/></Custom3>
			<Custom4><xsl:value-of select="Custom4"/></Custom4>
			<Custom5><xsl:value-of select="Custom5"/></Custom5>
			<Custom6><xsl:value-of select="Custom6"/></Custom6>
			<Custom7><xsl:value-of select="Custom7"/></Custom7>
			<Custom8><xsl:value-of select="Custom8"/></Custom8>
			<Custom9><xsl:value-of select="Custom9"/></Custom9>
			<Custom10><xsl:value-of select="Custom10"/></Custom10>
			<DocTransStatus><xsl:value-of select="DocTransStatus"/></DocTransStatus>
			<MessageDigest><xsl:value-of select="MessageDigest"/></MessageDigest>
			<AuditFileName><xsl:value-of select="AuditFileName"/></AuditFileName>
			<ReceiptAuditFileName><xsl:value-of select="ReceiptAuditFileName"/></ReceiptAuditFileName>									
	    <xsl:if test="count(ProcessInstanceUid)!=0">
		    <ProcessInstanceUid><xsl:value-of select="ProcessInstanceUid"/></ProcessInstanceUid>
		    <!--ProcessInstanceID><xsl:value-of select="ProcessInstanceID"/></ProcessInstanceID>
    		    <UserTrackingID><xsl:value-of select="UserTrackingID"/></UserTrackingID-->	
            </xsl:if>
		    <ProcessInstanceID><xsl:value-of select="ProcessInstanceID"/></ProcessInstanceID>
    		    <UserTrackingID><xsl:value-of select="UserTrackingID"/></UserTrackingID>	
			<hasAttachment><xsl:value-of select="hasAttachment"/></hasAttachment>
			<isAttachmentLinkUpdated><xsl:value-of select="isAttachmentLinkUpdated"/></isAttachmentLinkUpdated>
			<xsl:for-each select="AttachmentUid">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:for-each select="Activity">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<TracingID><xsl:value-of select="TracingID"/></TracingID> <!--TWX 20 Nov 2006 -->
		</GridDocument>
	</xsl:template>
</xsl:stylesheet>
