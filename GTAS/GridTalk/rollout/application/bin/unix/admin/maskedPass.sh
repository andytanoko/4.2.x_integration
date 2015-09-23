
if [ $# -lt 1 -o "$1" == "-h" ]; then
  echo 
  echo maskedPass - mask the plain text password
  echo
  echo Usage: 
  echo
  echo maskedPass.sh password
  echo
  exit
fi

. $GRIDTALK_HOME/bin/setenv.sh
$JAVA_HOME/bin/java -jar MaskedPassword.jar $1
