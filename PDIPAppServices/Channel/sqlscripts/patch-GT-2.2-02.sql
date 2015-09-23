# @Date:08/OCT/2003  @since 2.2.2 @Author Jagadeesh @Comments: To support B2B Soap Tpt Protocol.
use appdb;

UPDATE fieldmetainfo
set Constraints='type=enum\r\nchannelInfo.tptProtocolType.jms=JMS\r\nchannelInfo.tptProtocolType.http=HTTP\r\nchannelInfo.tptProtocolType.soap=SOAP-HTTP'
where SqlName='TptProtocolType' and EntityObjectName = 'com.gridnode.pdip.app.channel.model.ChannelInfo';

UPDATE fieldmetainfo SET 
Constraints='type=enum\r\ncommInfo.protocolType.jms=JMS\r\ncommInfo.protocolType.http=HTTP\r\ncommInfo.protocolType.soap=SOAP-HTTP'
where SqlName='ProtocolType' and EntityObjectName = 'com.gridnode.pdip.app.channel.model.CommInfo';

