# 23 Apr 2003 I8 v2.0.26 [Ang Meng Hua] 
# Modify constraints value for enum value in fieldmetainfo fileExtValue to add in additional format

USE appdb;

UPDATE fieldmetainfo SET Constraints='type=enum\r\nport.fileExtValue.format1=ddMMyyHHmmsss\r\nport.fileExtValue.format2=ddMMyyyyHHmmsss\r\nport.fileExtValue.format3=MMddyyHHmmsss\r\nport.fileExtValue.format4=MMddyyyyHHmmsss\r\nport.fileExtValue.format5=yyMMddHHmmsss\r\nport.fileExtValue.format6=yyyyMMddHHmmsss\r\nport.fileExtValue.format7=yyyyMMdd\r\nport.fileExtValue.format8=yyMMdd\r\nport.fileExtValue.format9=ddMMyyyy\r\nport.fileExtValue.format10=ddMMyy\r\nport.fileExtValue.format11=MMddyyyy\r\nport.fileExtValue.format12=MMddyy' WHERE ObjectName='_fileExtValue';


