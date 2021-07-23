#!/bin/bash
#
# Perform environment setup installing all necessary 
# applications for Java development: Maven, Git, Java, PostgreSQL.
################################################################################
help()
{
# Display Help
echo "this script performs environment setup installing all necessary 
applications for Java development: Maven, Git, Java, PostgreSQL"
echo
echo "usage: environmentscript.sh [options...]"
echo "options:"
echo "-d|--drop   drop database"
echo "-c|--create    create databse and tables"
echo "-t|--truncate    delete database info"
echo "-b|--build    build the project with maven"
echo "-rf|--runfile    run the project to file"
echo "-rb|--rundatabase    run the project to database"
echo "--crimes   run for crimes"
echo "--stops   run for stop and search"
echo "-v|--verbose   Verbouse mode"

}
################################################################################
output(){
 if (( VERBOSE < 1 )); then
            $1 >&- 2>&-
         else
            $1
         fi
}
################################################################################
operation_result() {
if (( $? >0 )); then
            echo "$1 failed"
		 else
		    echo "$1 complete"
         fi
}
################################################################################
build_project(){
cd /home/shared/Task3 || return 1
mvn clean compile assembly:single
}
################################################################################
run_crimes_to_file(){
prepare_postgresql
cd /home/shared/Task3 || return 1
java -cp target/PoliceData-1.0-SNAPSHOT-jar-with-dependencies.jar by.epam.bigdatalab.Main -Dstart=2019-05 -Dend=2019-05 -Dmethod=crimes -Dpath=/home/shared/LondonStations.csv -Dsave=file -Doutput=/home/shared/Crimes.txt 
}
################################################################################
run_crimes_to_db(){
prepare_postgresql
cd /home/shared/Task3 || return 1
java -cp target/PoliceData-1.0-SNAPSHOT-jar-with-dependencies.jar by.epam.bigdatalab.Main -Dstart=2019-05 -Dend=2019-05 -Dmethod=crimes -Dpath=/home/shared/LondonStations.csv -Dsave=db 
}
################################################################################
################################################################################
run_stops_to_file(){
prepare_postgresql
cd /home/shared/Task3 || return 1
java -cp target/PoliceData-1.0-SNAPSHOT-jar-with-dependencies.jar by.epam.bigdatalab.Main -Dstart=2019-05 -Dend=2019-05 -Dmethod=stops -Dsave=file -Doutput=/home/shared/Stops.txt 
}
################################################################################
run_stops_to_db(){
prepare_postgresql
cd /home/shared/Task3 || return 1
java -cp target/PoliceData-1.0-SNAPSHOT-jar-with-dependencies.jar by.epam.bigdatalab.Main -Dstart=2019-05 -Dend=2019-05 -Dmethod=stops -Dsave=db 
}
################################################################################
delete_db_data(){
prepare_postgresql;

psql -U postgres -d crimes -f /home/shared/Task3/ResetTables.sql
}

################################################################################
prepare_postgresql(){
service postgresql-9.6 start >&- 2>&-
}
################################################################################
drop_db(){
prepare_postgresql;

if [ "$(psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='crimes'" )" = '1' ]
then
   drop_db_command  
fi
}
################################################################################
create_db(){
prepare_postgresql;

if [ "$(psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='crimes'" )" != '1' ]
then
   create_db_command;
fi
create_db_tables_command;
}
################################################################################
drop_db_command(){
psql -U postgres -c "
	DROP DATABASE crimes;
"
}
################################################################################
create_db_command(){
psql -U postgres -c "
	CREATE DATABASE crimes;
"
}
################################################################################
create_db_tables_command(){
psql -U postgres -d crimes -f /home/shared/Task3/TableScript.sql
}
################################################################################

VERBOSE=0
CREATE=0
DROP=0
TRUNCATE=0
BUILD=0
RUN_FILE=0
RUN_DB=0
CRIMES=0
STOPS=0
HELP=0

for i in "$@"; do 

case $i in
    -v|--verbose)
    VERBOSE=1
    ;;
esac
case $i in
    -c|--create)
    CREATE=1
    ;;
esac
case $i in
    -d|--drop)
    DROP=1
    ;;
esac
case $i in
    -h|--help)
    HELP=1
    ;;
esac
case $i in
    -t|--truncate)
    TRUNCATE=1
    ;;
esac
case $i in
    -b|--build)
    BUILD=1
    ;;
esac
case $i in
    -rf|--runfile)
    RUN_FILE=1
    ;;
esac
case $i in
    -rb|--rundatabase)
    RUN_DB=1
    ;;
esac
case $i in
    --crimes)
    CRIMES=1
    ;;
esac
case $i in
    --stops)
    STOPS=1
    ;;
esac
done

if(( HELP > 0 )); then
   help

   else
   
   if (( DROP > 0 )); then
      echo "deleting database"
      output drop_db
      operation_result "deleting database"		        
   fi

   if (( CREATE > 0 )); then
      echo "creating database"
      output create_db 
	  operation_result "creating database"	
      
   fi
   
   if (( TRUNCATE > 0 )); then
      echo "deleting database data"
      output delete_db_data 
	  operation_result "deleting database data"	
      
   fi
   
   if (( BUILD > 0 )); then
      echo "building project"
      output build_project 
	  operation_result "building project"	
      
   fi
   
   if (( RUN_DB > 0 )); then
      if (( CRIMES > 0 )); then
         output run_crimes_to_db    
      fi
	  if (( STOPS > 0 )); then
         output run_stops_to_db    
      fi
   fi
   
   if (( RUN_FILE > 0 )); then
      if (( CRIMES > 0 )); then
         output run_crimes_to_file    
      fi
	  if (( STOPS > 0 )); then
         output run_stops_to_file    
      fi
   fi
	
fi