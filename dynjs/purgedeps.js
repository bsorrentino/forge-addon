/*
clear
echo "clean project targets"
mvn $@ -o clean
echo -n "delete project's artifacts from local repo [y/N]"
read -n 1 del

#echo "$del"

if [ "$del" == "y" ] ; then
	echo -e "\nperform delete "
	
	mvn  $@ -fn -T2 -o \
	 org.codehaus.mojo:build-helper-maven-plugin:1.8:remove-project-artifact \
	 -Dbuildhelper.failOnError=false \
	 -Dbuildhelper.removeAll=true
	 
else
	echo -e "\nskip delete "
fi

echo -n "delete project's dependencies from local repo [y/N] "
read -n 1 del

#echo "$del"

if [ "$del" == "y" ] ; then
	echo -e "\nperform delete "
	
	mvn  $@ -fn -T2 -o \
	 org.apache.maven.plugins:maven-dependency-plugin:2.8:purge-local-repository \
	 -DreResolve=false \
	 -DsnapshotsOnly=false \
	 -Dverbose=true
	 
else
	echo -e "\nskip delete"
fi


*/