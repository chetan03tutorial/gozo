#!/bin/groovy
 
/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */
 
@Library('workflowlib-sandbox@jenkinsaoo')
import com.lbg.workflow.sandbox.*
 
import java.util.LinkedList
 
properties([
   buildDiscarder(logRotator(artifactDaysToKeepStr: '30', artifactNumToKeepStr: '10', daysToKeepStr: '30', numToKeepStr: '5')),
   [$class: 'RebuildSettings', autoRebuild: true, rebuildDisabled: false]
])
 
def configuration = "pipelines/conf/job-configuration.json"
def builder = 'pipelines/build/package.groovy'
def deployer = 'pipelines/deploy/application.groovy'
def    unitTests = ['pipelines/tests/unit.groovy']
def    staticAnalysis =  [    'pipelines/tests/sonar.groovy']
def    integrationTests = ['pipelines/tests/bdd.groovy', 'pipelines/tests/zap.groovy' ]
 
BuildHandlers handlers = new ConfigurableBuildHandlers(    builder,
                           deployer,
                           unitTests,
                           staticAnalysis,
                           integrationTests) as BuildHandlers
 
String notify = 'LloydsCJTAOOFT1PCA@sapient.com,LloydsCJTAOOFT2PCA@sapient.com,lloydscjtdevops@sapient.com'
Integer timeout = 60
 
invokeBuildPipelineHawk( 'pca-sales-api', handlers, configuration, notify, timeout )