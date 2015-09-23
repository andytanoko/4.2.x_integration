# path for nohup.out
log_path=$GRIDTALK_HOME/bin

# path for nohup.out archive
archive_path=$GRIDTALK_HOME/bin/log_archive

# file age will determine how old is the log to be delete
# Example:
# file_age = 3 means all the log files that older than 3 days or older than 72hours will be delete
file_age='30'

# Extension of the log file
file_ext='log'