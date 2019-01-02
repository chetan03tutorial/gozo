/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */
def runTest(String targetBranch, context) {
	 node(){
		  this.runTestImpl(targetBranch, context)
	 }
}
def runTestImpl(String targetBranch, context) {

    def typeOfBranch = targetBranch.startsWith('ft-') ? 'ft' : targetBranch
    def appname = appName(context.application, typeOfBranch, 12)
    def sonarExclusions = context.config.sonar.exclusions ?: ''
    def coverageExclusions = context.config.sonar.coverage_exclusions ?: ''
    def qualityGate = context.config.sonar.quality_gate ?: 'invalid-gate'
    def sonarServerID = context.config.sonar.server_id ?: 'invalid-sonarServer'
    def sonarSources = context.config.stash.code_path ?: 'invalid-codepath'
    def sonarJavaOptions = [
            '-Dsonar.projectKey'          : appname,
            '-Dsonar.projectName'         : appname,
            '-Dappname'                   : appname,
            '-DbranchName'                : typeOfBranch,
            '-Dsonar.projectVersion'      : env.BUILD_NUMBER,
            '-Dsonar.sources'             : "$sonarSources/src/main",
            '-Dsonar.exclusions'          : "\'${sonarExclusions}\'",
            '-Dsonar.coverage.exclusions' : "\'${coverageExclusions}\'",
            '-Dsonar.qualitygate'         : qualityGate,
            '-Dsonar.scm.enabled'         : 'true',
            '-Dsonar.log.level'           : 'INFO',
            '-Dsonar.junit.reportsPath'   : ".",
            '-Dsonar.cobertura.reportPath': "coverage.xml"
    ]
    node() {
        try {
            checkout scm
            unstash 'coverage'
            unstash 'junit'
            sonarRunner {
                sonarServer = sonarServerID
                preRun = "pipelines/scripts/functions"
                javaOptions = sonarJavaOptions
            }
        } catch (error) {
            echo "FAILURE: Sonar Qualification failed"
            echo error.message
            throw error
        }
        finally {
           step([$class: 'WsCleanup', notFailBuild: true])
        }

    }
}

def publishSplunk(String targetBranch, String epoch, context, handler) {
    /*
     * Your implementation for publishing the reports of the runTest method to splunk.
     * Can use library functions to make it easier
     * handler also provides utility methods to fulfil this task.
     * In this case handler.SCP and handler.RSYNC  are available
     * Refer to workflowlib-sandbox for details
     */
    echo "PUBLISH: ${this.name()} reports to Splunk"
    echo "Nothing to do here"
}

String name() {
    return "SONAR"
}

return this;
