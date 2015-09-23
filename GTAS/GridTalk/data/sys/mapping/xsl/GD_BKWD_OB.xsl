<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0"
>

<xsl:output method="xml" indent = "yes"/> 

<xsl:template match="GridDocument">
<GridDocument>
	<ListView>
		<GDocFilename><xsl:value-of select="GdocFilename"/></GDocFilename>
		<UDocFilename><xsl:value-of select="UdocFilename"/></UDocFilename>
		<AuditFilename></AuditFilename>
		<GridDocID><xsl:value-of select="GdocId"/></GridDocID>
		<SPartnerID><xsl:value-of select="SenderPartnerId"/></SPartnerID>
		<UDocNum><xsl:value-of select="UdocNum"/></UDocNum>
		<RefUDocNum><xsl:value-of select="RefUdocNum"/></RefUDocNum>
		<UDocVersion><xsl:value-of select="UdocVersion"/></UDocVersion>
		<UDocDocumentType><xsl:value-of select="UdocDocType"/></UDocDocumentType>
		<UDocFileType><xsl:value-of select="UdocFileType"/></UDocFileType>
		<Folder>OUTBOUND</Folder>
<xsl:choose>
	<xsl:when test="SrcFolder='Inbound'">	
		<SourceFolder>INBOUND</SourceFolder>
	</xsl:when>
	<xsl:otherwise>
		<SourceFolder>IMPORT</SourceFolder>
	</xsl:otherwise>	
</xsl:choose>	
		<DestinationFolder></DestinationFolder>
		<NotifyUserEmail><xsl:value-of select="NotifyUserEmail"/></NotifyUserEmail>
		<UDocFileSize><xsl:value-of select="UdocFileSize"/></UDocFileSize>
		<ProcessInstanceID><xsl:value-of select="ProcessInstanceID"/></ProcessInstanceID>
		<ProcessOriginatorID></ProcessOriginatorID>
		<ProcessMsgType></ProcessMsgType>
		<ProcessDefName><xsl:value-of select="ProcessDefId"/></ProcessDefName>
		<OriImportFilename></OriImportFilename>
		<RPartnerName><xsl:value-of select="RecipientPartnerName"/></RPartnerName>
		<SPartnerName><xsl:value-of select="SenderPartnerName"/></SPartnerName>
		<HasAttachment><xsl:value-of select="hasAttachment"/></HasAttachment>
		<GnRbmId></GnRbmId>
		<Status>
			<!-- In memory because may be used quite frequently -->
			<IsXML></IsXML>
			<IsExported><xsl:value-of select="isExported"/></IsExported>
			<IsReceiveAckReq><xsl:value-of select="isReceiveAckReq"/></IsReceiveAckReq>
			<IsViewAckReq><xsl:value-of select="isViewAckReq"/></IsViewAckReq>
			<IsExportAckReq><xsl:value-of select="isExportAckReq"/></IsExportAckReq>
			<IsOpen>false</IsOpen>
			<IsSent><xsl:value-of select="isSent"/></IsSent>
			<IsLocalPending><xsl:value-of select="isLocalPending"/></IsLocalPending>
			<DocumentStatus></DocumentStatus>
		</Status>
		<GridFolder>
			<!-- In memory because may be used quite frequently -->
			<GridFolderPath></GridFolderPath>
			<IsShareable></IsShareable>
			<ShareableProperties></ShareableProperties>
			<GridClass></GridClass>
			<GridObject></GridObject>
			<GridAttributes></GridAttributes>
			<GridFolderNum></GridFolderNum>
			<Description></Description>
		</GridFolder>
	</ListView>
	<Details>
		<Transport>
			<UDocDTD></UDocDTD>
			<EncryptionLevel><xsl:value-of select="EncryptionLevel"/></EncryptionLevel>
			<GridNodeID><xsl:value-of select="RecipientNodeId"/></GridNodeID>
			<UUID><xsl:value-of select="RecipientBizEntityUuid"/></UUID>
			<BusinessEntityID><xsl:value-of select="RecipientBizEntityId"/></BusinessEntityID>
			<RPartnerType><xsl:value-of select="RecipientPartnerType"/></RPartnerType>
			<RPartnerGrp><xsl:value-of select="RecipientPartnerType"/></RPartnerGrp>
			<RPartnerID><xsl:value-of select="RecipientPartnerId"/></RPartnerID>
			<RPartnerFunction><xsl:value-of select="RecipientPartnerFunction"/></RPartnerFunction>
			<RGDocID><xsl:value-of select="RecipientGdocId"/></RGDocID>
		</Transport>
		<System>
			<GDocDTD></GDocDTD>
			<GDocDTDVersion></GDocDTDVersion>
			<GDocType></GDocType>
			<UploadGMGNID></UploadGMGNID>
			<DownloadGMGNID></DownloadGMGNID>
			<Creator></Creator>
			<URLPortal></URLPortal>
			<ArchiveKey></ArchiveKey>
			<IsTest></IsTest>
		</System>
		<Mapping>
			<SPartnerFunction><xsl:value-of select="SenderPartnerFunction"/></SPartnerFunction>
			<RefGDocID><xsl:value-of select="RefGdocId"/></RefGDocID>
			<RefUDocFilename><xsl:value-of select="RefUdocFilename"/></RefUDocFilename>
		</Mapping>
		<SenderInfo>
			<SNodeID><xsl:value-of select="SenderNodeId"/></SNodeID>
			<SUUID><xsl:value-of select="SenderBizEntityUuid"/></SUUID>
			<SGDocID><xsl:value-of select="SenderGdocId"/></SGDocID>
			<SPartnerFunction><xsl:value-of select="SenderPartnerFunction"/></SPartnerFunction>
			<SUserID><xsl:value-of select="SenderUserId"/></SUserID>
			<SUserName><xsl:value-of select="SenderUserName"/></SUserName>
			<SBusinessEntityID><xsl:value-of select="SenderBizEntityId"/></SBusinessEntityID>
			<SPort></SPort>
			<SRoute><xsl:value-of select="SenderRoute"/></SRoute>
		</SenderInfo>
		<MappedInfo>
			<PartnerID></PartnerID>
			<PartnerType></PartnerType>
			<PartnerGrp></PartnerGrp>
		</MappedInfo>
		<Backend>
			<EDIStandard></EDIStandard>
			<EDIVersion></EDIVersion>
			<EDIMessage></EDIMessage>
			<BizDocNum></BizDocNum>
			<BizDocCreationDate></BizDocCreationDate>
			<DocLogicalName></DocLogicalName>
		</Backend>
		<Custom>
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
		</Custom>
		<Attachments>
			<AttachmentNames></AttachmentNames>
			<xsl:for-each select="AttachmentUid">
				<AttachmentUID><xsl:value-of select="."/></AttachmentUID>
			</xsl:for-each>
		</Attachments>
		<History>
			<DTCreated><xsl:value-of select="DateTimeCreate"/></DTCreated>
			<DTImported><xsl:value-of select="DateTimeImport"/></DTImported>
			<DTReceivedStart></DTReceivedStart>
			<DTReceivedEnd></DTReceivedEnd>
			<DTDownloadStart></DTDownloadStart>
			<DTDownloadEnd></DTDownloadEnd>
	<xsl:choose>		
		<xsl:when test="SenderRoute='DIRECT'">	
			<DTSendOutStart><xsl:value-of select="DateTimeSendStart"/></DTSendOutStart>
			<DTSendOutEnd><xsl:value-of select="DateTimeSendEnd"/></DTSendOutEnd>
			<DTUploadStart></DTUploadStart>
			<DTUploadEnd></DTUploadEnd>
		</xsl:when>	
		<xsl:otherwise>	
			<DTSendOutStart></DTSendOutStart>
			<DTSendOutEnd></DTSendOutEnd>
			<DTUploadStart><xsl:value-of select="DateTimeSendStart"/></DTUploadStart>
			<DTUploadEnd><xsl:value-of select="DateTimeSendEnd"/></DTUploadEnd>
		</xsl:otherwise>	
	</xsl:choose>	
			<DTExported><xsl:value-of select="DateTimeExport"/></DTExported>
			<DTView><xsl:value-of select="DateTimeView"/></DTView>
			<DTTransactionComplete><xsl:value-of select="DateTimeTransComplete"/></DTTransactionComplete>
			<!-- Timestamps after this can have multiple DT -->
			<DTSExported></DTSExported>
			<DTSViewed></DTSViewed>
			<DTSSendOutStart></DTSSendOutStart>
			<DTSSendOutEnd></DTSSendOutEnd>
			<DTSUploadStart></DTSUploadStart>
			<DTSUploadEnd></DTSUploadEnd>
			<DTSTransactionComplete></DTSTransactionComplete>
		</History>
	</Details>
	<RosettaNet>
		<DeliveryHeader>
			<IsSecureTransportRequired></IsSecureTransportRequired>
			<MessageDateTime></MessageDateTime>
			<ReceiverDomain></ReceiverDomain>
			<ReceiverGlobalBusinessIdentifier></ReceiverGlobalBusinessIdentifier>
			<ReceiverLocationID></ReceiverLocationID>
			<SenderDomain></SenderDomain>
			<SenderGlobalBusinessIdentifier></SenderGlobalBusinessIdentifier>
			<SenderLocationID></SenderLocationID>
			<MessageTrackingID></MessageTrackingID>
		</DeliveryHeader>
		<ServiceHeader>
			<BusinessActivityIdentifier></BusinessActivityIdentifier>
			<FromGlobalPartnerRoleClassificationCode></FromGlobalPartnerRoleClassificationCode>
			<FromGlobalBusinessServiceCode></FromGlobalBusinessServiceCode>
			<InReplyToGlobalBusinessActionCode></InReplyToGlobalBusinessActionCode>
			<InReplyToMessageStandard></InReplyToMessageStandard>
			<InReplyToStandardVersion></InReplyToStandardVersion>
			<InReplyToVersionIdentifier></InReplyToVersionIdentifier>
			<MessageTrackingID></MessageTrackingID>
			<AttachmentDescription></AttachmentDescription>
			<AttachmentGlobalMimeTypeQualifierCode></AttachmentGlobalMimeTypeQualifierCode>
			<AttachmentURI></AttachmentURI>
			<NumberOfAttachments></NumberOfAttachments>
			<ActionIdentityGlobalBusinessActionCode></ActionIdentityGlobalBusinessActionCode>
			<ActionIdentityToMessageStandard></ActionIdentityToMessageStandard>
			<ActionIdentityStandardVersion></ActionIdentityStandardVersion>
			<ActionIdentityVersionIdentifier></ActionIdentityVersionIdentifier>
			<SignalIdentityGlobalBusinessSignalCode></SignalIdentityGlobalBusinessSignalCode>
			<SignalIdentityVersionIdentifier></SignalIdentityVersionIdentifier>
			<ToGlobalPartnerRoleClassificationCode></ToGlobalPartnerRoleClassificationCode>
			<ToGlobalBusinessServiceCode></ToGlobalBusinessServiceCode>
			<GlobalUsageCode></GlobalUsageCode>
			<PartnerDomain></PartnerDomain>
			<PartnerGlobalBusinessIdentifier></PartnerGlobalBusinessIdentifier>
			<PartnerLocationID></PartnerLocationID>
			<PartnerURI></PartnerURI>
			<PartnerDefinedPIPPayloadBindingID></PartnerDefinedPIPPayloadBindingID>
			<PIPGlobalProcessCode></PIPGlobalProcessCode>
			<PIPInstanceIdentifier></PIPInstanceIdentifier>
			<PIPVersionIdentifier></PIPVersionIdentifier>
			<ProcessTransactionId></ProcessTransactionId>
			<ProcessActionId></ProcessActionId>
			<FromGlobalPartnerClassificationCode></FromGlobalPartnerClassificationCode>
			<ToGlobalPartnerClassificationCode></ToGlobalPartnerClassificationCode>
			<AttemptCount></AttemptCount>
		</ServiceHeader>
	</RosettaNet>
</GridDocument>

</xsl:template>

</xsl:stylesheet>

