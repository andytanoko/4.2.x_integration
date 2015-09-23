if [ -e ftp.pid ]; then
  kill `cat ftp.pid`
  rm ftp.pid
fi;