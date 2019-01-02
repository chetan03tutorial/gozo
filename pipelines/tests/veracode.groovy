def runTest(String targetBranch, context) {
/*
     * Lightweight operations that dont require a workspace.
     * eg: variable manipulations.
     */
node(){
    def mavenPom = context.config.maven.pom ?: 'pom.xml'
    def mavenGoals = context.config.maven.package_goals ?: '-U clean  package -DskipTests=true'
    def settings= context.config.maven.settings ?: 'pipelines/conf/settings.xml'

        checkout scm
        try {
            sh """
                cd ${WORKSPACE}/sales-api
                pwd 
                source ${WORKSPACE}/pipelines/scripts/functions && \
                mvn ${mavenGoals} -f ${mavenPom} -s ../pipelines/conf/settings.xml
                """
            //sh "/apps/tools/ant/bin/ant -f ${WORKSPACE}/pipelines/conf/build.xml"
              sh """
                  pwd
                """
            stash name: "veracode", includes: 'sales-api/target/*.war'

        } catch (error) {
            echo "FAILURE: Failure in veracode packaging"
            echo error.message
            throw error
        } finally {
            //WsCleanup
            step([$class: 'WsCleanup', notFailBuild: true])
        }

}
}


def uploadVeracode(String targetBranch, context) {
def veracodeCredentials = context.config.veracode.credentials ?: 'veracode-creds'
def veracodeID =  context.config.veracode.id ?: ''

    withCredentials([
      usernamePassword(credentialsId: veracodeCredentials,
      passwordVariable: 'API_PASSWORD',
      usernameVariable: 'API_USERNAME')
    ]){
    withEnv([
     "APP_ID=${veracodeID}",
    ]) {
        checkout scm
        unstash 'veracode'
        try {
            sh """
                cd ${WORKSPACE}/sales-api
                pwd 
                source ${WORKSPACE}/pipelines/scripts/functions && \
                source ${WORKSPACE}/pipelines/scripts/veracode_upload.sh  target/
                """

            stash name: "veracodereport", includes: 'veracodeResults/*.xml'


        } catch (error) {
            echo "FAILURE: veracode failed"
            echo error.message
            throw error
        } finally {
            archiveArtifacts allowEmptyArchive: true,
                    artifacts: 'veracodeResults/',
                    fingerprint: true,
                    onlyIfSuccessful: false
              //Publish anything thats needed


            //WsCleanup
            step([$class: 'WsCleanup', notFailBuild: true])
          }
        }
    }

}

def downloadVeracode(String targetBranch, context) {
def veracodeCredentials = context.config.veracode.credentials ?: 'veracode-creds'
def veracodeID =  context.config.veracode.id ?: ''
def notificationList = context.config.veracode.notificationList ?: 'LloydsCJTDevOps@sapient.com'

    withCredentials([
      usernamePassword(credentialsId: veracodeCredentials,
      passwordVariable: 'API_PASSWORD',
      usernameVariable: 'API_USERNAME')
    ]){
    withEnv([
     "APP_ID=${veracodeID}",
    ]) {
        checkout scm
        unstash 'veracode'
        try {
            sh """
                cd ${WORKSPACE}/sales-api
                pwd 
                source ${WORKSPACE}/pipelines/scripts/functions && \
                source ${WORKSPACE}/pipelines/scripts/veracode_download.sh target/
                """
            //stash name: "veracodereport", includes: 'veracodeResults/*.xml'

        } catch (error) {
            echo "FAILURE: veracode failed"
            echo error.message
            throw error
        } finally {
            archiveArtifacts allowEmptyArchive: true,
                    artifacts: 'veracodeResults/',
                    fingerprint: true,
                    onlyIfSuccessful: false
              //Publish anything thats needed
              sh """
              zip -rq VeracodeReport.zip veracodeResults
              ls -ltr 
              echo "Please find the attached veracode reports" | mail -s "PCA : Veracode Reports" -r "buildnotifications@lloydsbanking.com" -a "VeracodeReport.zip" $notificationList
              """

            //WsCleanup
            step([$class: 'WsCleanup', notFailBuild: true])
          }
        }
    }

}


def publishSplunk(String targetBranch, String epoch, context, handler){
                       def appname = appName(context.application, targetBranch)
                       def splunkReportDir = "${context.config.splunk.reportdir}"
                       echo "PUBLISH: ${this.name()} ${appname} reports to Splunk"
                       sh 'rm -rf j2/vcReports'
                       dir ('j2/vcReports') {
                           unstash "veracodereport"
                           sh 'ls -lR'
                           splunkPublisher.SCP(    'veracodeResults/*.xml',
                                   "${splunkReportDir}")
                       }
                    }

String name() {
    return "VERACODE_TESTS"
}

return this;
