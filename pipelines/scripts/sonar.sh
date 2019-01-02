#!/usr/bin/env bash


source pipelines/scripts/functions

PROJECT_KEY=$( echo pca-sales-api-${BRANCH_NAME} | sed  -e 's/\//-/' )

mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar \
-f sales-api/pom.xml \
-s pipelines/conf/settings.xml \
-Dsonar.host.url=${SONAR_HOST_URL} \
-Dsonar.jdbc.url=${SONAR_JDBC_URL} \
-Dsonar.jdbc.username=${SONAR_JDBC_USERNAME} \
-Dsonar.jdbc.password=${SONAR_JDBC_PASSWORD} \
-Dsonar.login=${SONAR_LOGIN} \
-Dsonar.pasword=${SONAR_PASSWORD} \
-Dsonar.projectKey=${PROJECT_KEY} \
-DbranchName=${BRANCH_NAME} \
-Dsonar.projectVersion=${BUILD_NUMBER} \
-Dsonar.scm.enabled=true \
-Dsonar.scm.provider=git

#-Dsonar.projectName=pas-api-coms-mgr-${BRANCH_NAME} \
