/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */

def pack(String targetBranch, String targetEnv, context) {

    def mavenSettings = context.config.maven.settings ?: 'pipelines/conf/settings.xml'
    //def mavenPomMCA = context.config.maven.pom_mca ?: 'pom.xml'
    //def mavenPomIB = context.config.maven.pom_ib ?: 'pom.xml'
    //Fix for Jenkins Pipeline
    def mavenPomMCA = context.config.maven.pom_mca ?: './sales-api/pom.xml'
    def mavenPomIB = context.config.maven.pom_ib ?: './sales-api/pom.xml'
    def mavenGoals = context.config.maven.build_goals ?: 'package'
    def code_stash = context.config.stash.code_path ?: 'invalid-path'

    node() {
        try {
            checkout scm
            stash name: "$code_stash", includes: "$code_stash/**"
            sh """source ${WORKSPACE}/pipelines/scripts/functions && \
            mvn ${mavenGoals} -f ${mavenPomMCA}  -s ${mavenSettings}
            """
            stash name: "$code_stash", includes: "$code_stash/**"
            archiveArtifacts artifacts: "**/target/*.war, **/target/*.ear, **/*pom*" 
        } catch (error) {
            echo "Application Build failed"
            echo error.message
            throw error
        } finally {
            step([$class: 'WsCleanup', notFailBuild: true])
        }
    }
}

def publishNexus(String targetBranch, String targetEnv, context) {

    def mavenSettings = context.config.maven.settings ?: 'pipelines/conf/settings.xml'
    def mavenPomMCA = context.config.maven.pom_mca ?: 'pom.xml'
    def mavenPomIB = context.config.maven.pom_ib ?: 'pom.xml'
    def mavenGoals = context.config.maven.deploy_goals ?: 'deploy'
    def environment = targetBranch
    def code_stash = context.config.stash.code_path ?: 'invalid-path'
    def groupID = context.config.maven.groupID ?: 'invalid-group-id'


    def major
    def minor
    def war_prefix
    def ear_prefix


    switch (environment) {
        case 'release-prod':
            major = context.config.environments.release.artifact_info.major ?: '1'
            minor = context.config.environments.release.artifact_info.minor ?: '1'
            war_prefix = context.config.environments.release.artifact_info.war_prefix ?: ''
            ear_prefix = context.config.environments.release.artifact_info.ear_prefix ?: ''
            break
        default:
            major = context.config.environments.master.artifact_info.major ?: '1'
            minor = context.config.environments.master.artifact_info.minor ?: '1'
            war_prefix = context.config.environments.master.artifact_info.war_prefix ?: ''
            ear_prefix = context.config.environments.master.artifact_info.ear_prefix ?: ''
            break
    }

    node() {

        checkout scm
        withCredentials([
                usernamePassword(credentialsId: 'nexus-uploader',
                        passwordVariable: 'PASS',
                        usernameVariable: 'USER')
        ]) {
            withEnv([
                    "major=$major",
                    "minor=$minor",
                    "war_prefix=$war_prefix",
                    "ear_prefix=$ear_prefix",
                    "code_path=$code_stash",
                    "mavenSettings=$mavenSettings",
                    "mavenPomMCA=$mavenPomMCA",
                    "mavenPomIB=$mavenPomIB",
                    "mavenGoals=$mavenGoals",
                    "groupID=$groupID",
                    "targetBranch=$targetBranch"
            ]) {
                try {
                    sh """ 
                        source ${WORKSPACE}/pipelines/scripts/functions && pipelines/scripts/nexus_upload.sh
                        """
                } catch (error) {
                    echo "Caught: ${error}"
                    echo "Upload to nexus has been failed"
                    throw error
                } finally {
                    step([$class: 'WsCleanup', notFailBuild: true])
                }
            }
        }
    }
}


def name() {
    return "Maven Builder"
}

//Optional Methods. Not part of Signature
String artifactName(String targetBranch, String targetEnv, context) {
    /*
     * eg: return "${targetBranch}-${context.application}-artifact-${env.BUILD_NUMBER}.tar.gz"
     */

}

return this;
