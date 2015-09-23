javac -d classes -cp .\lib\ src\com\gridnode\gtas\tool\*.java

xcopy /Y /s classes\* .\release\
xcopy /Y /s lib\* .\release\

jar cvfm .\release\MaskedPassword.jar .\meta-inf\MANIFEST.MF -C release com com\gridnode\gtas\pdip\


rem ;.\classes\com\gridnode\pdip\framework\util