--- IMPORTANT: Please perform backup on the gtvan.rpt_schedule table
--- The following script update the rpt_schedule's customer list to the group list

connect GTVAN/gridnode;

SET SERVEROUTPUT ON;

DECLARE

customerList "rpt_schedule"."customer_list"%TYPE;
uid "rpt_schedule"."uid"%TYPE;
groupName varchar(1000);
resourceCount Number;
customerName varchar(1000);
groupNameList varchar(1000);
s_index pls_integer := 1;
e_index pls_integer;
p_del varchar(1) := ',';


CURSOR rptScheduleCursor IS SELECT "uid", "customer_list" FROM "rpt_schedule";

BEGIN
DBMS_OUTPUT.PUT_LINE('Opening cursor');
OPEN rptScheduleCursor;

LOOP

FETCH rptScheduleCursor INTO uid, customerList;
EXIT WHEN rptScheduleCursor%NOTFOUND;

DBMS_OUTPUT.PUT_LINE('####Start changing rpt_schedule record with uid: '||uid|| '####');
SELECT COUNT(*) INTO resourceCount FROM "isat_resource" WHERE "type"='Customer Name' AND "value"=customerList;

if(resourceCount = 1) THEN
   SELECT "group_name" INTO groupName FROM "isat_resource" WHERE "type"='Customer Name' AND "value"=customerList;
   UPDATE "rpt_schedule" SET "customer_list"=groupName WHERE "uid"=uid;
   DBMS_OUTPUT.PUT_LINE('Update to groupName:' || groupName);
ELSE
   DBMS_OUTPUT.PUT_LINE('Customer List: '|| customerList || ' not found in isat_resource, maybe contain multiple customer names, start parsing...');
   LOOP
       e_index := INSTR(customerList, p_del);
       if(e_index > 0 OR LENGTH(customerList) > 0) THEN
          
	  if(e_index > 0) THEN 
               customerName := SUBSTR(customerList, s_index, e_index-LENGTH(p_del));
          ELSE
	       customerName := customerList;
          END IF;

          DBMS_OUTPUT.PUT_LINE('Parsing customer name: '|| customerName);
          SELECT COUNT(*) INTO resourceCount FROM "isat_resource" WHERE "type"='Customer Name' AND "value"=customerName;
          DBMS_OUTPUT.PUT_LINE('ResourceCount: '|| resourceCount);
          if(resourceCount > 0) THEN
	     SELECT "group_name" INTO groupName FROM (SELECT "group_name" FROM "isat_resource" WHERE "type"='Customer Name' AND "value"=customerName GROUP BY "group_name"); 
	     DBMS_OUTPUT.PUT_LINE('Located groupName: '|| groupName);

	     IF(LENGTH(groupNameList) > 0) THEN
               groupNameList := CONCAT( CONCAT(groupNameList, p_del), groupName);
             ELSE
               groupNameList := groupName;
	     END IF;
             
             IF(customerName = customerList) THEN
	        customerList := '';
             ELSE
                customerList := SUBSTR(customerList, e_index+1, LENGTH(customerList));
		s_index := 1;
             END IF;
	        DBMS_OUTPUT.PUT_LINE('Parsed customerList: '|| customerList || ' GroupNameList: '||groupNameList);
          ELSE
	     DBMS_OUTPUT.PUT_LINE('Cannot find correspond customerName: '||customerName || ' in table isat_resource. Abort update to rpt_schedule with uid:'||uid);
             groupNameList := '';
	     EXIT;
	  END IF;
       ELSE
          EXIT;
       END IF;
   END LOOP;
   DBMS_OUTPUT.PUT_LINE('Final GroupList: '|| groupNameList);
   if(LENGTH(groupNameList) > 0) THEN
      UPDATE "rpt_schedule" SET "customer_list"=groupNameList WHERE "uid"=uid;
   END IF;
END IF;

s_index := 1;
e_index := 1;
groupNameList := '';

DBMS_OUTPUT.PUT_LINE('####End updated for rpt_schedule record with uid: '||uid|| '####');
DBMS_OUTPUT.PUT_LINE('');
END LOOP;

CLOSE rptScheduleCursor;

END;

.

run;


commit;