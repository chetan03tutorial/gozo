# PCA-SALES-API-MASTER


[![Build Status](http://jenkins.sandbox.extranet.group/jenkins/buildStatus/icon?job=pao-savings-api-master-build)](http://jenkins.sandbox.extranet.group/jenkins/job/pao-savings-api-master-build/)
[![Quality Gate](http://10.113.140.168:80/api/badges/gate?key=pao-savings-api-master)](http://sonar.sandbox.extranet.group/dashboard/index/pao-savings-api-master)

#### BUILD-PIPELINE: [JENKINS-JOB-DEFINATION](http://gerrit.sandbox.extranet.group/plugins/gitblit/blob/?r=core-jenkins-yaml.git&f=pao-ci/pao-savings-api-master-build.yml&h=master)


## Lloyds Java/Spring REST API

### Getting started

Make sure you have Java installed (preferably version 1.5.0_22). 
And maven 3.0.5


## Goals of this API

This API has been written to enable the development of REST endpoints for interacting with the SOAP services to get and update data from and to the downstream systems of the bank. The SOAP services interact with SALSA,BAPI (FS Services) and SOA endpoints.

#### Tools/Frameworks used

- Java
- Spring 3
- Apache CXF
- Cucumber (BDD Testing)
- Junits


### Maven tasks

### Compile the code - (In the folder $/../pca-sales-api)
mvn clean compile 

###Package the war - (In the folder parallel to pca-sales-api)

Step 1 (In the folder $/../pca-sales-api)
mvn clean install

Step 2 (In the parent to the folder pca-sales-api)
mvn clean package

### Running the app
mvn luis:run -Dluis.server.port=8090 -Psandbox

This will start the server and the application.


### Running all the tests 
Running against localhost 
mvn -U test -Dmaven.test.failure.ignore=true -DbranchName=http://localhost:8090 -DisLocal=yes -Dcucumber.options="--tags @SANDBOX"

Running against a branch
mvn -U test -Dmaven.test.failure.ignore=true -DbranchName=master -Dcucumber.options="--tags @SANDBOX"

### Git branching

The main development branch is master for drop 1 and mvp2-master for drop 2.

This acts as a holding branch and a base to branch off of. Do not work directly in this branch. make a short lived branch off of this and create a pull request to merge your finished work. Before creating your pull request merge dev/core into your branch to make sure you have the latest version and fixed any merge conflicts. To submit the code for merge, perform a "git ready <branchName>" and assign a reviewer in gerrit.

### Jenkins jobs of interest

For Building feature branches - 

http://jenkins.sandbox.extranet.group/jenkins/job/pca-sales-api-feature/

For Master build-

http://jenkins.sandbox.extranet.group/jenkins/job/pca-sales-api-master-build/

For BDDs

Feature - http://jenkins.sandbox.extranet.group/jenkins/job/pca-sales-api-feature-bdd/

Master - http://jenkins.sandbox.extranet.group/jenkins/job/pca-sales-api-master-bdd/


## CJT Devops :
#### Confluence Page : http://confluence.sandbox.extranet.group/display/LCCT/SALES-RELEASE
