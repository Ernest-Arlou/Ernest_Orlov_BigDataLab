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
echo "-p|--postgresql    postgresql instaltion"
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
installationResult() {
if (( $? >0 )); then
            echo "$1 failed"
		 else
		    echo "$1 complete"
         fi
}
################################################################################
delete_db_data(){
prepare_postgresql
sudo -u postgres psql -d crimes -c "
TRUNCATE TABLE \"Outcome-status\" CASCADE;
TRUNCATE TABLE \"Street\" CASCADE;
TRUNCATE TABLE \"Location\" CASCADE;
TRUNCATE TABLE \"Crimes\" CASCADE;
"
}

################################################################################
prepare_postgresql(){
service postgresql-9.6 start >&- 2>&-
cd /home >&- 2>&-
}
################################################################################
drop_db(){
prepare_postgresql
if [ "$(sudo -u postgres psql -tAc "SELECT 1 FROM pg_database WHERE datname='crimes'" )" = '1' ]
then
   drop_db_command  
fi
}
################################################################################
create_db(){
prepare_postgresql

if [ "$(sudo -u postgres psql -tAc "SELECT 1 FROM pg_database WHERE datname='crimes'" )" != '1' ]
then
   create_db_command;
fi
create_db_tables_command;
}
################################################################################
drop_db_command(){
sudo -u postgres psql -c "
	DROP DATABASE crimes;
"
}
################################################################################
create_db_command(){
sudo -u postgres psql -c "
CREATE DATABASE crimes
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;"
}
################################################################################
create_db_tables_command(){
sudo -u postgres psql -d crimes -c "
	CREATE TABLE IF NOT EXISTS public.\"Street\"
(
    id bigint NOT NULL,
    name character varying COLLATE pg_catalog.\"default\" NOT NULL,
    CONSTRAINT \"Street_pkey\" PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.\"Street\"
    OWNER to postgres;
	
CREATE SEQUENCE IF NOT EXISTS \"Location_id_seq\";
CREATE TABLE IF NOT EXISTS public.\"Location\"
(
    id bigint NOT NULL DEFAULT nextval('\"Location_id_seq\"'),
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    \"street-id\" bigint NOT NULL,
	CONSTRAINT \"Location_pkey\" PRIMARY KEY (id),
    CONSTRAINT \"street-id\" FOREIGN KEY (\"street-id\")
        REFERENCES public.\"Street\" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER SEQUENCE \"Location_id_seq\"
OWNED BY \"Location\".id;

ALTER TABLE public.\"Location\"
    OWNER to postgres;
	
CREATE SEQUENCE IF NOT EXISTS \"Outcome-status_id_seq\";
CREATE TABLE IF NOT EXISTS public.\"Outcome-status\"
(
    id bigint NOT NULL DEFAULT nextval('\"Outcome-status_id_seq\"'),
    category character varying COLLATE pg_catalog.\"default\" NOT NULL,
    date date NOT NULL,
    CONSTRAINT \"Outcome-status_pkey\" PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER SEQUENCE \"Outcome-status_id_seq\"
OWNED BY \"Outcome-status\".id;

ALTER TABLE public.\"Outcome-status\"
    OWNER to postgres;
		

CREATE TABLE IF NOT EXISTS public.\"Crimes\"
(
    category character varying COLLATE pg_catalog.\"default\" NOT NULL,
    context character varying COLLATE pg_catalog.\"default\" NOT NULL,
    id bigint NOT NULL,
    \"location-subtype\" character varying COLLATE pg_catalog.\"default\" NOT NULL,
    \"location-type\" character varying COLLATE pg_catalog.\"default\" NOT NULL,
    month date NOT NULL,
    \"persistent-id\" character varying COLLATE pg_catalog.\"default\" NOT NULL,
	\"location-id\" bigint NOT NULL,
	\"outcome-status-id\" bigint,
    CONSTRAINT Crimes_pkey PRIMARY KEY (id),
	CONSTRAINT \"location-id\" FOREIGN KEY (\"location-id\")
        REFERENCES public.\"Location\" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT \"outcome-status-id\" FOREIGN KEY (\"outcome-status-id\")
        REFERENCES public.\"Outcome-status\" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.\"Crimes\"
    OWNER to postgres;
"
}
################################################################################


VERBOSE=0
CREATE=0
DROP=0
TRUNCATE=0
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
done

if(( HELP > 0 )); then
   help

   else
   
   if (( DROP > 0 )); then
      echo "deleting database"
      output drop_db
      installationResult "deleting database"		        
   fi

   if (( CREATE > 0 )); then
      echo "creating database"
      output create_db 
	  installationResult "creating database"	
      
   fi
   
   if (( TRUNCATE > 0 )); then
      echo "deleting database data"
      output delete_db_data 
	  installationResult "deleting database data"	
      
   fi
	
fi