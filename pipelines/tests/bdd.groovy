/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */

def runTest(String targetBranch, context) {
    node() {
    this.runTestImpl(targetBranch, context)
}
}

def runTestImpl( targetBranch, context) {
 
        def stashName = this.name()
        def scenarioPassThreshold = context.config.passthresholds.bdd.percent_scenarios ?: '100'

        def mavenSettings = context.config.maven.settings ?: 'pipelines/conf/settings.xml'
        def mavenPom = context.config.maven.bdd_pom ?: 'pom.xml'
        def mavenGoals = context.config.maven.bdd_goals ?: '-U clean test'
        def bddReports = context.config.stash.bdd_code_path ?: 'invalid-path'

        def appname = appName(context.application, targetBranch)
        
        def LLOYDS_API_DOMAIN
        def HFAX_API_DOMAIN
        def BOS_API_DOMAIN
        def API_ENDPOINTS
		
        sh "echo 'Before SCM'"
        //Checkout repository
        checkout scm
        sh "echo 'after SCM'"
        withEnv([
                    "LLOYDS_API_DOMAIN=http://lloyds-lp-${appname}.lbg.eu-gb.mybluemix.net",
                    "HFAX_API_DOMAIN=http://halifax-hp-${appname}.lbg.eu-gb.mybluemix.net",
                    "BOS_API_DOMAIN=http://bos-bp-${appname}.lbg.eu-gb.mybluemix.net",
					"API_ENDPOINTS=urls/endpoints.properties"
            ]){
        try {

            sh """ echo ${mavenGoals} && source ${WORKSPACE}/pipelines/scripts/functions && mvn ${mavenGoals} -f ${mavenPom} -s ${
                mavenSettings
            }
            """
            step([$class               : 'CucumberReportPublisher',
                  failedFeaturesNumber : 99999999999,
                  failedScenariosNumber: 9999999999,
                  failedStepsNumber    : 99999999999,
                  fileExcludePattern   : '',
                  fileIncludePattern   : '**/*.json',
                  jsonReportDirectory  : "$bddReports/target/reports",
                  parallelTesting      : false,
                  pendingStepsNumber   : 99999999999,
                  skippedStepsNumber   : 99999999999,
                  trendsLimit          : 0,
                  undefinedStepsNumber : 99999999999
            ])

            dir("$bddReports/target/reports") {
                stash name: stashName, includes: '*.json'
                withEnv([
                        "SCENARIO_PASS_THRESHOLD=${scenarioPassThreshold}"
                ]) {
                    sh "${env.WORKSPACE}/pipelines/scripts/bdd-pass-threshold-checker.sh"
                }
            }
            dir('api-automation/target/surefire-reports/'){
                sh "cp -rf ${WORKSPACE}/pipelines/conf/build.xml ."
                sh "/apps/tools/ant/bin/ant -f build.xml"
                sh 'tree -L 3'
                stash   name: 'bddTests' , includes: '**/TESTS*'
            }

        } catch (error) {
            echo "Application Build failed"
            echo error.message
            throw error
        } finally {
            archiveArtifacts allowEmptyArchive: true,
                    artifacts: '**/*.json,**/api-automation/target/reports*.json',
                    fingerprint: true,
                    onlyIfSuccessful: false
            step([$class: 'WsCleanup', notFailBuild: true])
        }
    }
}
def publishSplunk(String targetBranch, String epoch, context, handler){
	echo "PUBLISH: ${this.name()} reports to Splunk"
    
    node(){
        def splunkReportDir = context.config.splunk.reportdir
        echo "PUBLISH: ${this.name()} reports to Splunk"
        sh 'rm -rf j2/bddReports'
        dir ('j2/bddReports') {
            unstash 'bddTests'
            sh 'tree -L 3'
            handler.SCP( 'TESTS-TestSuites.xml', "${splunkReportDir}/bdd.${targetBranch}.${epoch}.xml")
        }
    }
}
String name(){
	return "API BDD"
}
return this;
