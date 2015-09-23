# 23 Apr 2003 I8 v2.0.26 [Ang Meng Hua] 
# Modify constraints value for enum value in fieldmetainfo fileExtValue to add in additional format

USE appdb;

UPDATE fieldmetainfo SET Constraints='type=enum\r\nport.fileExtValue.format1=ddMMyyHHmmss\r\nport.fileExtValue.format2=ddMMyyHHmmssSSS\r\nport.fileExtValue.format3=ddMMyyyyHHmmss\r\nport.fileExtValue.format4=ddMMyyyyHHmmssSSS\r\nport.fileExtValue.format5=MMddyyHHmmss\r\nport.fileExtValue.format6=MMddyyHHmmssSSS\r\nport.fileExtValue.format7=MMddyyyyHHmmss\r\nport.fileExtValue.format8=MMddyyyyHHmmssSSS\r\nport.fileExtValue.format9=yyMMddHHmmss\r\nport.fileExtValue.format10=yyMMddHHmmssSSS\r\nport.fileExtValue.format11=yyyyMMddHHmmss\r\nport.fileExtValue.format12=yyyyMMddHHmmssSSS\r\nport.fileExtValue.format13=yyyyMMdd\r\nport.fileExtValue.format14=yyMMdd\r\nport.fileExtValue.format15=ddMMyyyy\r\nport.fileExtValue.format16=ddMMyy\r\nport.fileExtValue.format17=MMddyyyy\r\nport.fileExtValue.format18=MMddyy' WHERE ObjectName='_fileExtValue';


