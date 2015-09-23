# 26 May 2003 I8 v2.1 [Koh Han Sing] Make correction to partnerFunction.workflowActivitiesUids constraints field.
# 

use appdb;

UPDATE fieldmetainfo SET Constraints= 'type=foreign\r\nforeign.key=workflowActivity.uid\r\nforeign.display=workflowActivity.description\r\nforeign.cached=false\r\ncollection=true' WHERE LABEL='partnerFunction.workflowActivitiesUids'