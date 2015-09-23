# $1 Backup Dir
# $2 Path to the patch's log file
# $3 Backout command file

echo
echo Upgrading the ant 1.5.1  to ant 1.8.2
echo This script will:
echo a. install the new ant 1.8.2
echo b. move the existing ant 1.5.1 folder to Backup Dir
echo

workingDir=`pwd`

# copy the ant1.5 to the backup folder
cd $GRIDTALK_HOME
cd ..
targetDir=`pwd`
cp -pr jakarta-ant-1.5.1-bin* $workingDir/$1/ >> $2
rm -r jakarta-ant-1.5.1-bin* >> $2

# copy the ant1.8.2 into the GT install directory
cd $workingDir
cp -pr ./env/apache-ant-1.8.2* $targetDir

# change the file permission to be executable
chmod +x $targetDir/apache-ant-1.8.2/bin/ant

# include the rollback script
# add to backout list so that the new ant can be backout
echo rm -r $targetDir/apache-ant-1.8.2* >> $3
echo cp -pr ./jakarta-ant-1.5.1-bin* $targetDir >> $3

echo apache-ant is upgraded successfully >>$2
