#change the below line to your jboss home directory
set jboss_home=C:\source\WorkEnv\jboss-3.0.0_tomcat-4.0.3

set bridge_class_path=%jboss_home%\client\jboss-j2ee.jar
set bridge_class_path=%bridge_class_path%;%jboss_home%\client\jnp-client.jar
set bridge_class_path=%bridge_class_path%;%jboss_home%\client\jbossmq-client.jar
set bridge_class_path=%bridge_class_path%;%jboss_home%\client\log4j.jar
set bridge_class_path=%bridge_class_path%;%jboss_home%\client\concurrent.jar
set bridge_class_path=%bridge_class_path%;%jboss_home%\client\jboss-client.jar

#change the below line to your framework directory
set framework_dir=C:\source\PDIPFramework

set bridge_class_path=%bridge_class_path%;%framework_dir%\reqlib\junit-3.7.jar
set bridge_class_path=%bridge_class_path%;%framework_dir%\framework\deploy\framework.jar

#change the below line to your base directory
set base_dir=C:\source\PDIPBaseServices

set bridge_class_path=%bridge_class_path%;%base_dir%\reqlib\smqclient-2.1.3.jar
set bridge_class_path=%bridge_class_path%;%base_dir%\reqlib\jms.jar
set bridge_class_path=%bridge_class_path%;%base_dir%\transport\deploy\bridge.jar
set bridge_class_path=%bridge_class_path%;%base_dir%\transport\deploy\transportData.jar

java -cp %CLASSPATH%;%bridge_class_path%; com.gridnode.pdip.base.transport.bridge.GTBridgeController %1 %2 %3 %4 %5 %6 %7 %8
