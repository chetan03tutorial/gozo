def runTest(String targetBranch, context) {
	 node(){
		  this.runTestImpl(targetBranch, context)
	 }
}

def runTestImpl(targetBranch, context) {

	 def mavenSettings = context.config.maven.settings ?: 'pipelines/conf/settings.xml'
	 //def mavenPom = context.config.maven.pom ?: 'pom.xml'
	//Fix for pipeline issue
	 def mavenPom = './sales-api/pom.xml'
	 def mavenGoals = context.config.maven.coverage_goals ?: '-U clean install cobertura:cobertura -Dcobertura.report.format=xml'
	 def appname = appName(context.application, targetBranch)
	 def sonarSources = context.config.stash.code_path ?: 'invalid-codepath'

	 checkout scm
	 try {
		sh	"""
			source ${WORKSPACE}/pipelines/scripts/functions && \
			mvn ${mavenGoals} -f ${mavenPom} -s ${mavenSettings}
			"""
		dir("${sonarSources}/target/site/cobertura/"){
				sh "pwd"
                stash   name: 'coverage', includes: 'coverage.xml'
        }
        dir("${sonarSources}/target/surefire-reports/"){
                sh "pwd && /apps/tools/ant/bin/ant -f ${WORKSPACE}/pipelines/conf/build.xml -Dbasedir=. "
                stash   name: 'junit',	includes: '**/TESTS*'
        }
		
		junit allowEmptyResults: true, testResults: '**/TESTS*.xml'
		//stash name: "workspace", useDefaultExcludes: false
		//stash name: "pipelines", includes: "pipelines/**"				

	} catch (error) {
		 echo "FAILURE: Unit Tests failed"
		 echo error.message
		 throw error
	} finally {
		archiveArtifacts 	allowEmptyArchive: true,
							artifacts: '**/TESTS*.xml,**/cobertura/coverage.xml',
							fingerprint: true,
							onlyIfSuccessful: false
		step([$class: 'WsCleanup', notFailBuild: true])
	}
}

def publishSplunk(String targetBranch, String epoch, context, handler) {
	node(){
		this.publishSplunkImpl(targetBranch, epoch, context, handler)
	}
}


def publishSplunkImpl(String targetBranch, String epoch, context, handler){
	/*
	 * Your implementation for publishing the reports of the runTest method to splunk.
	 * Can use library functions to make it easier
	 * handler also provides utility methods to fulfil this task.
	 * In this case handler.SCP and handler.RSYNC  are available
	 * Refer to workflowlib-sandbox for details
	 */
	 def splunkReportDir = context.config.splunk.reportdir
     echo "PUBLISH: ${this.name()} reports to Splunk"
     sh 'rm -rf j2/unitReports'
     dir ('j2/unitReports') {
	 	unstash 'junit'
	 	handler.SCP( 'TESTS-TestSuites.xml', "${splunkReportDir}/bdd.$targetBranch.${epoch}.xml" )
	 	unstash 'coverage'
	 	handler.SCP( 'coverage.xml', "${splunkReportDir}/coverage.$targetBranch.${epoch}.xml" )
	}
}

String name() {
	 return "UNIT_TESTS"
}

return this;
