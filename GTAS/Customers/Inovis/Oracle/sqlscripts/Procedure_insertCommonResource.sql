--- ---------------------------------------------------------
--- Create new sequence for generating UID for isat_resource
--- ---------------------------------------------------------

CREATE INDEX resource_idx1 ON "isat_resource" ("group_name","type","code");

CREATE SEQUENCE ISAT_RESOURCE_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER ISAT_RESOURCE_TRIGGER 
BEFORE INSERT ON "isat_resource"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT ISAT_RESOURCE_SEQ.NEXTVAL INTO :NEW."uid" FROM DUAL;
END;
/

--- -----------------------------------------
--- Create Procedure INSERT "isat_resource"
--- -----------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_insert
(
  n_type "isat_resource"."type"%type,
  n_code "isat_resource"."code"%type,
  n_value "isat_resource"."value"%type,
  n_group_name "isat_resource"."group_name"%type
)
IS
BEGIN
  INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES (n_type, n_code, n_value, n_group_name);
EXCEPTION 
  WHEN DUP_VAL_ON_INDEX THEN
    DBMS_OUTPUT.PUT_LINE(n_type||' alredy inserted !');
END isat_resource_insert;


--- -------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_document_transaction"
--- -------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_doc_check
(
  n_group_name "isat_resource"."group_name"%type,
  n_document_type "isat_resource"."code"%type,
  n_customer_name "isat_resource"."code"%type,
  n_partner_name "isat_resource"."code"%type
) 
AS
   resource_type_doc VARCHAR2(10) := 'Doc Type';
   resource_type_customer VARCHAR2(15) := 'Customer Name';
   resource_type_partner VARCHAR2(15) := 'Partner Name';
   resource_count  NUMBER(10);
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_doc AND t."code"=n_document_type AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_doc, n_document_type, n_document_type, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;

  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_customer AND t."code"=n_customer_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_customer, n_customer_name, n_customer_name, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;

  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_partner AND t."code"=n_partner_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_partner, n_partner_name, n_partner_name, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;
    
END isat_resource_doc_check;
/

CREATE OR REPLACE TRIGGER doc_trans_after_insert_CR
  AFTER INSERT ON "isat_document_transaction"
  FOR EACH ROW
  WHEN (new."group_name" IS NOT NULL)
  CALL isat_resource_doc_check(:new."group_name",:new."document_type",:new."customer_name",:new."partner_name")
/


--- ------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_process_transaction"
--- ------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_proc_check
(
  n_group_name "isat_resource"."group_name"%type,
  n_pip_name "isat_resource"."code"%type
) 
AS
   resource_type VARCHAR2(10) := 'PIP Name';
   resource_count  NUMBER(10);
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type AND t."code"=n_pip_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
  isat_resource_insert(resource_type,n_pip_name,n_pip_name,n_group_name);  
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;
    
END isat_resource_proc_check;
/


CREATE OR REPLACE TRIGGER proc_trans_after_insert
  AFTER INSERT OR UPDATE ON "isat_process_transaction"
  FOR EACH ROW
  WHEN (new."group_name" IS NOT NULL)
  CALL isat_resource_proc_check(:new."group_name",:new."pip_name")
/
