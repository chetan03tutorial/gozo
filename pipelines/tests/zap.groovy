/*
 * Author: Abhay Chrungoo <abhay@ziraffe.io>
 * Contributing HOWTO: TODO
 */
def runTest(String targetBranch, context){
  node() {
    checkout scm
    def stashName = this.name()
    def appname = appName(context.application, targetBranch, 12)
    def zapGulpTask = context.config.zap.gulptask ?: "cucumber-sauce-firefox-Linux"
    def zapResultDir = context.config.zap.resultdir ?: "invalid-path"
    def typeOfBranch = targetBranch.startsWith('ft-') ? 'ft' : targetBranch
    def zapQualityGate = context.config.zap.sonarQuality_gate ?: 'invalid-gate'
    def sonarServerID = context.config.sonar.server_id ?: 'invalid-sonarServer'
    def mavenSettings = context.config.maven.settings ?: 'pipelines/conf/settings.xml'
    def mavenPom = context.config.maven.bdd_pom ?: 'pom.xml'
    def mavenGoals = context.config.maven.bdd_goals ?: '-U clean test'
    
    def LLOYDS_API_DOMAIN
    def HFAX_API_DOMAIN
    def BOS_API_DOMAIN
    try{
      withEnv([
            "appname=${appname}",
            "targetBranch=${targetBranch}",
            "GULP_OPTION=${zapGulpTask}",
            "SERVER_ENV=production",
            "RESULTSDIR=${env.WORKSPACE}/${zapResultDir}",
            "LLOYDS_API_DOMAIN=http://lloyds-lp-${appname}.lbg.eu-gb.mybluemix.net",
            "HFAX_API_DOMAIN=http://halifax-hp-${appname}.lbg.eu-gb.mybluemix.net",
            "BOS_API_DOMAIN=http://bos-bp-${appname}.lbg.eu-gb.mybluemix.net"
        ]){
            sh "rm -rf ${RESULTSDIR} && mkdir -p ${RESULTSDIR}"
            sh "pipelines/scripts/zap.sh || true"
      }


    }catch (error) {
      echo "FAILURE: zap Tests failed"
      echo error.message
      throw error
    }finally{

      archiveArtifacts  allowEmptyArchive: true,
      artifacts: "**/${zapResultDir}/*",
      fingerprint: true,
      onlyIfSuccessful: false

    }

    def sonarJavaOptions = [
      '-Dsonar.projectKey': "${appname}-zap" ,
      '-Dsonar.projectName': "${appname}-zap" ,
      '-Dappname': "${appname}-zap" ,
      '-DbranchName': typeOfBranch ,
      '-Dsonar.projectVersion': env.BUILD_NUMBER ,
      '-Dsonar.projectDescription': appname + '_zap_test_results' ,
      '-Dsonar.sources': "${zapResultDir}/" ,
      '-Dsonar.zaproxy.reportPath': "${zapResultDir}/report.xml" ,
      '-Dsonar.qualitygate': "${zapQualityGate}" ,
      '-Dsonar.scm.enabled': 'true' ,
      '-Dsonar.log.level':'ERROR'
    ]

    dir("${WORKSPACE}"){
        try {
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
}


def publishSplunk(String targetBranch, String epoch, context, handler){

}
String name(){
  return "ZAP"
}
return this;
