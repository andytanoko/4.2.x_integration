del /Q /S .\dist\*
rd /S /Q .\dist\*
mkdir .\dist\build
mkdir .\dist\build\txmr.war\WEB-INF\classes\

javac -classpath ./txmr.war/WEB-INF/lib/mail-1.4.jar;./txmr.war/WEB-INF/lib/activation-1.1.jar;%J2EE_HOME%/lib/j2ee.jar -d ./dist/build/txmr.war/WEB-INF/classes/ ./txmr.war/servlet/*.java 
xcopy /e /Y /H /I txmr.war .\dist\build\txmr.war /EXCLUDE:exclude.txt

xcopy /e /Y /H /I txmr.war .\dist\build\txmr.war

cd .\dist\build\txmr.war
zip -r ..\..\txmr.war *

cd ..\..\..\

exit
