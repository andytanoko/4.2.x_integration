@echo off

set logfile=GridTalk_setup.log
if "%GRIDTALK_HOME%" == "" (
  echo GRIDTALK_HOME needs to be defined first before setup. > %logfile%
  goto abort
)


:setup
echo setup starting ..... > %logfile%
call setenv.bat

set properties=-Dsetup.properties=setup-win.properties

rem Put tools.jar into JRE to prevent exception for JSP
if not exist %JAVA_HOME%\lib\tools.jar (
  echo copy tools.jar >> %logfile%
  copy /B /Y .\jre150_20\lib\tools.jar %JAVA_HOME%\lib\tools.jar
)

rem Replace the security policy file, as Bouncy Castle API requires key length greater than the default one
echo replacing  local_policy.jar >> %logfile%
echo replacing  US_export_policy.jar >> %logfile%
copy /B /Y .\jre150_20\lib\security\local_policy.jar %JAVA_HOME%\lib\security\local_policy.jar
copy /B /Y .\jre150_20\lib\security\US_export_policy.jar %JAVA_HOME%\lib\security\US_export_policy.jar

ant %properties% -buildfile setup.xml >> %logfile%
goto end


:end
echo setup completed >> %logfile%
goto :EOF

:abort
echo setup aborted >> %logfile%
goto :EOF