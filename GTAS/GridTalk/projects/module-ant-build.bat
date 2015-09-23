@echo off
SET CLASSPATH=%JAVA_HOME%\jre\lib\plugin.jar;%CLASSPATH%

if  "%1"==""  goto default
REM ant -quiet -Dmodule.properties="%1"-build.properties -buildfile module-ant-build.xml build
ant -quiet -Dgtas.module="%1"-buildfile gtas-module-build.xml

default:
ant -quiet -Dbuild.default=defined -Dmodule.properties=common-build.properties -buildfile common-ant-build.xml build

:end