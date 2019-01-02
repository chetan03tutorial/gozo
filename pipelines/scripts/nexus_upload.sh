#!/bin/bash

#Function to capture exit code and throw exception

error_generate(){
if ! [ "${exit_code}" == "" ];then
	echo $1
	exit 1
fi
}

#Validate version from Nexus artifacts path

if ! (wget http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/com/lbg/ib/api/sales/${war_prefix}-v${major}/maven-metadata.xml >/dev/null 2>&1) ;then
	minor_release=0
else
	minor_release=$(curl -s http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/com/lbg/ib/api/sales/${war_prefix}-v${major}/maven-metadata.xml | grep -o ".*\/release\>" | cut -d ">" -f2 | cut -d "<" -f1  | rev | cut -d "." -f1 | rev)
fi

sed -i 's#AppVersion#'"${major}"'.'"${minor}"'.'"$((minor_release+1))"'#g' ${WORKSPACE}/${code_path}/src/main/resources/config/configAppProperties.xml || true
sed -i 's#timestamp#'"$(date +%d-%m-%Y-%H:%M)"'#g' ${WORKSPACE}/${code_path}/src/main/resources/config/configAppProperties.xml || echo ""
sed -i 's#false#true#g' ${WORKSPACE}/${code_path}/src/main/resources/config/configAppProperties.xml


#Building artifact from $mavenPomMCA file
mvn clean install -f $mavenPomMCA -s $mavenSettings
warfile=$(basename ${code_path}/target/*.war)

#Upload fat war
if [ "${warfile}" != "" ];then

	mvn ${mavenGoals}:deploy-file -DgroupId=${groupID} -DartifactId=${war_prefix}-v${major} -Dversion=${minor}.$((minor_release+2)) -Dpackaging=war  -Dfile=./sales-api/target/${warfile}  -DrepositoryId=deploy-repo  -Durl=http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/ -Dnexus.user=${USER} -Dnexus.password=${PASS} -s $mavenSettings
	if ! [ "$?" == "0" ];then
		echo Unable to push code to nexus.
		error_generate "Unable to push code to nexus."
	fi
else
	echo WAR file not found to push to nexus.
	error_generate "WAR file not found under workspace"
fi

############ Ear generation

##Upload fat ear
mkdir version
echo $(echo ${major}.${minor}.$((minor_release+1)))-$(git rev-parse HEAD) > ./version/version.txt

if [ -f ${WORKSPACE}/sales-pca-api-ear/pom.xml ];then
	mvn -f sales-pca-api-ear/pom.xml -U clean install -DskipTests ||  exit_code=$?
	error_generate "Failure while creating package for ear."
	
	earfile=$(basename sales-pca-api-ear/target/*.ear)
	echo "${earfile}"
	if [ "${earfile}" != "" ];then
		mvn deploy:deploy-file -DgroupId=${groupID} -DartifactId=${ear_prefix}-v${major} -Dversion=${minor}.$((minor_release+2)) -Dpackaging=ear  -Dfile=./sales-pca-api-ear/target/${earfile}  -DrepositoryId=deploy-repo  -Durl=http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/ -Dnexus.user=${USER} -Dnexus.password=${PASS} -s $mavenSettings || exit_code=$?
		if [ "$?" == "0" ];then
			echo Unable to push code to nexus.
			error_generate "Unable to push code to nexus."
		fi
	else
		echo EAR file not found to push to nexus.
		error_generate "EAR file not found to push to nexus."
	fi
fi


########################
#Below changes for IB specific artifacts (skinny war/ear)
echo 'START | IB-Sales Artifact creation'
rm -rf ${code_path}/target/
file=${code_path}/src/main/webapp/WEB-INF/web.xml
#removing security-constraint tag
#sed -i $(grep -n "security-constraint" $file | head -1 | cut -d ":" -f1),$(grep -n "security-constraint" $file | tail -1 | cut -d ":" -f1)d $file || true
#removing security-role tag
#sed -i $(grep -n "security-role" $file | head -1 | cut -d ":" -f1),$(grep -n "security-role" $file | tail -1 | cut -d ":" -f1)d $file || true
#change in dtd file
sed -i 's#"./GALAXY_LOGS"#"${GalaxyLogDir}"#g' ${code_path}/src/main/resources/config/config.dtd || true

#Building artifact from $mavenPomIB file
echo 'Start IB-Sales WAR creation'
mvn clean install -DskipTests -f sales-api/pom-ib.xml -s $mavenSettings

#Upload fat war
if [ "${warfile}" != "" ];then

	mvn ${mavenGoals}:deploy-file -DgroupId=${groupID} -DartifactId=${war_prefix}-v${major} -Dversion=${minor}.$((minor_release+3)) -Dpackaging=war  -Dfile=./sales-api/target/${warfile}  -DrepositoryId=deploy-repo  -Durl=http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/ -Dnexus.user=${USER} -Dnexus.password=${PASS} -s $mavenSettings
	if ! [ "$?" == "0" ];then
		echo Unable to push code to nexus.
		error_generate "Unable to push code to nexus."
	fi
else
	echo WAR file not found to push to nexus.
	error_generate "WAR file not found under workspace"
fi

##Get Skinny war
echo "${WORKSPACE}" 
if [ -f ${WORKSPACE}/sales-pca-api-ear/pom.xml ];then
	mvn -f sales-pca-api-ear/pom.xml -U clean install -DskipTests ||  exit_code=$?
	error_generate "Failure while creating package for ear."
	
	earfile=$(basename sales-pca-api-ear/target/*.ear)
	echo "${earfile}"
	if [ "${earfile}" != "" ];then
		mvn deploy:deploy-file -DgroupId=${groupID} -DartifactId=${ear_prefix}-v${major} -Dversion=${minor}.$((minor_release+3)) -Dpackaging=ear  -Dfile=./sales-pca-api-ear/target/${earfile}  -DrepositoryId=deploy-repo  -Durl=http://nexus.sandbox.extranet.group/nexus/content/repositories/releases/ -Dnexus.user=${USER} -Dnexus.password=${PASS} -s $mavenSettings || exit_code=$?
		if [ "$?" == "0" ];then
			echo Unable to push code to nexus.
			error_generate "Unable to push code to nexus."
		fi
	else
		echo EAR file not found to push to nexus.
		error_generate "EAR file not found to push to nexus."
	fi
fi

echo 'END | IB-Sales Artifact creation'

rm -rf version
