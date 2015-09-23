<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="GridDocument">
		<GridDocument>
			<GdocId><xsl:value-of select="GdocId"/></GdocId>
			<GdocFilename><xsl:value-of select="GdocFilename"/></GdocFilename>
			<Folder><xsl:value-of select="Folder"/></Folder>
			<SenderPartnerId></SenderPartnerId>
			<UdocNum><xsl:value-of select="UdocNum"/></UdocNum>
			<RefUdocNum><xsl:value-of select="UdocNum"/></RefUdocNum>
			<UdocFilename><xsl:value-of select="UdocFilename"/></UdocFilename>
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
			<isSent><xsl:value-of select="isSent"/></isSent>
			<isLocalPending><xsl:value-of select="isLocalPending"/></isLocalPending>
			<isExpired><xsl:value-of select="isExpired"/></isExpired>
			<isRecAckProcessed><xsl:value-of select="isRecAckProcessed"/></isRecAckProcessed>
			<CreateBy><xsl:value-of select="CreateBy"/></CreateBy>
			<SenderNodeId><xsl:value-of select="RecipientNodeId"/></SenderNodeId>
			<SenderGdocId></SenderGdocId>
			<SenderRoute><xsl:value-of select="SenderRoute"/></SenderRoute>
			<SenderPartnerFunction></SenderPartnerFunction>
			<SenderUserId></SenderUserId>
			<SenderUserName></SenderUserName>
			<SenderBizEntityId><xsl:value-of select="RecipientBizEntityId"/></SenderBizEntityId>
			<SenderBizEntityUuid><xsl:value-of select="RecipientBizEntityUuid"/></SenderBizEntityUuid>
			<SenderRegistryQueryUrl><xsl:value-of select="RecipientRegistryQueryUrl"/></SenderRegistryQueryUrl>
			<SenderPartnerType><xsl:value-of select="RecipientPartnerType"/></SenderPartnerType>
			<SenderPartnerGroup><xsl:value-of select="RecipientPartnerGroup"/></SenderPartnerGroup>
			<SenderPartnerName><xsl:value-of select="RecipientPartnerName"/></SenderPartnerName>
			<RefGdocId><xsl:value-of select="GdocId"/></RefGdocId>
			<RefUdocFilename><xsl:value-of select="UdocFilename"/></RefUdocFilename>
			<RecipientNodeId><xsl:value-of select="SenderNodeId"/></RecipientNodeId>
			<RecipientPartnerId><xsl:value-of select="SenderPartnerId"/></RecipientPartnerId>
			<RecipientPartnerFunction></RecipientPartnerFunction>
			<EncryptionLevel><xsl:value-of select="EncryptionLevel"/></EncryptionLevel>
			<RecipientPartnerType><xsl:value-of select="SenderPartnerType"/></RecipientPartnerType>
			<RecipientPartnerGroup><xsl:value-of select="SenderPartnerGroup"/></RecipientPartnerGroup>
			<RecipientPartnerName><xsl:value-of select="SenderPartnerName"/></RecipientPartnerName>
			<RecipientBizEntityId><xsl:value-of select="SenderBizEntityId"/></RecipientBizEntityId>
			<RecipientBizEntityUuid><xsl:value-of select="SenderBizEntityUuid"/></RecipientBizEntityUuid>
			<RecipientRegistryQueryUrl><xsl:value-of select="SenderRegistryQueryUrl"/></RecipientRegistryQueryUrl>
			<RecipientGdocId><xsl:value-of select="RecipientGdocId"/></RecipientGdocId>
			<RecipientChannelUid></RecipientChannelUid>
			<RecipientChannelName></RecipientChannelName>
			<RecipientChannelProtocol></RecipientChannelProtocol>
			<PortUid></PortUid>
			<PortName></PortName>
			<DateTimeCreate><xsl:value-of select="DateTimeCreate"/></DateTimeCreate>
			<DateTimeSendEnd></DateTimeSendEnd>
			<DateTimeReceiveEnd></DateTimeReceiveEnd>
			<DateTimeSendStart></DateTimeSendStart>
			<DateTimeTransComplete></DateTimeTransComplete>
			<DateTimeExport></DateTimeExport>
			<DateTimeImport><xsl:value-of select="DateTimeImport"/></DateTimeImport>
			<DateTimeReceiveStart></DateTimeReceiveStart>
			<DateTimeView></DateTimeView>
			<DateTimeRecipientView></DateTimeRecipientView>
			<DateTimeRecipientExport></DateTimeRecipientExport>
			<PartnerFunction><xsl:value-of select="PartnerFunction"/></PartnerFunction>
			<ActionCode><xsl:value-of select="ActionCode"/></ActionCode>
			<RnProfileUid><xsl:value-of select="RnProfileUid"/></RnProfileUid>
			<ProcessDefId><xsl:value-of select="ProcessDefId"/></ProcessDefId>
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
		    <ProcessInstanceID><xsl:value-of select="ProcessInstanceID"/></ProcessInstanceID>
    		    <UserTrackingID><xsl:value-of select="UserTrackingID"/></UserTrackingID>	
            </xsl:if>
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
