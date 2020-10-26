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
echo "-j|--java    java instaltion"
echo "-g|--git    git instaltion"
echo "-m|--maven    maven instaltion"
echo "-p|--postgresql    postgresql instaltion"
echo "-v|--verbose   Verbouse mode"

}
################################################################################
# Install Maven.
# Returns:
#   0 if maven was installed, 1 on error.
################################################################################
function install_maven() {
yum install -y wget 
TEMPORARY_DIRECTORY="$(mktemp -d)"
DOWNLOAD_TO="$TEMPORARY_DIRECTORY/maven.tgz"

wget -O "$DOWNLOAD_TO" http://www-us.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz

tar xzf $DOWNLOAD_TO -C $TEMPORARY_DIRECTORY
rm $DOWNLOAD_TO

mv $TEMPORARY_DIRECTORY/apache-maven-* /usr/local/maven
echo -e 'export M2_HOME=/usr/local/maven\nexport PATH=${M2_HOME}/bin:${PATH}' > /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh

echo -e '\n\n!! Note you must relogin to get mvn in your path !!'

rm -r "$TEMPORARY_DIRECTORY"

}
################################################################################
# Install Java.
# Returns:
#   0 if maven was installed, 1 on error.
################################################################################
install_java() {
if ! yum install -y java-1.8.0-openjdk-devel; then
return 1
fi
}
################################################################################
# Install Git.
# Returns:
#   0 if maven was installed, 1 on error.
################################################################################
install_git() {
if ! yum install -y git; then
return 1
fi
}
################################################################################
# Install PostgreSQL.
# Returns:
#   0 if maven was installed, 1 on error.
################################################################################
install_postgreslq() {
rpm -Uvh https://download.postgresql.org/pub/repos/yum/reporpms/EL-6-x86_64/pgdg-redhat-repo-latest.noarch.rpm
yum -y install https://yum.postgresql.org/9.6/redhat/rhel-6-x86_64/postgresql96-server-9.6.19-1PGDG.rhel6.x86_64.rpm
if ! service postgresql-9.6 initdb; then
return 1
fi
if ! service postgresql-9.6 start; then
return 1
fi
}
################################################################################
installationResult() {
if (( $? >0 )); then
            echo "$1 installation failed"
		 else
		    echo "$1 installation complete"
         fi
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
VERBOSE=0
MAVEN=0
GIT=0
JAVA=0
POSTGRESQL=0
HELP=0

for i in "$@"; do 

case $i in
    -v|--verbose)
    VERBOSE=1
    ;;
esac
case $i in
    -m|--mavem)
    MAVEN=1
    ;;
esac
case $i in
    -j|--java)
    JAVA=1
    ;;
esac
case $i in
    -g|--git)
    GIT=1
    ;;
esac
case $i in
    -p|--postgresql)
    POSTGRESQL=1
    ;;
esac
case $i in
    -h|--help)
    HELP=1
    ;;
esac
done

if(( HELP > 0 )); then
   help

   else
   
   if (( JAVA > 0 )); then
      echo "installing java"
      output install_java
      installationResult java		        
   fi

   if (( MAVEN > 0 )); then
      echo "installing maven"
      output install_maven 
      installationResult maven
   fi
	
   if (( GIT > 0 )); then
      echo "installing git"
      output install_git 
      installationResult git
   fi
	
   if (( POSTGRESQL > 0 )); then
      echo "installing postgresql"
      output install_postgreslq 
      installationResult postgresql
   fi
	
   java -version
   mvn -version
   git --version
   psql --version
	
fi