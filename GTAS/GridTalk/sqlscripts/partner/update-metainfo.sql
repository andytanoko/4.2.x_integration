#[Tam Wei Xiang]          Jun 22 2006          Update the FMI for partner's state.
#                                              It is for GNDB00026894

USE appdb;

UPDATE fieldmetainfo
SET fieldmetainfo.Constraints='type=enum\r\npartner.state.disabled=0\r\npartner.state.enabled=1'
WHERE FieldName='STATE' AND EntityObjectName='com.gridnode.pdip.app.partner.model.Partner';