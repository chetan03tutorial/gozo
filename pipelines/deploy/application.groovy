/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */
 
import com.lbg.workflow.sandbox.BuildContext
 
def deploy(String targetBranch, BuildContext context) {
    /*
     * Lightweight operations that dont require a workspace.
     * eg: variable manipulations.
     */
 
    def appname = appName(context.application, targetBranch)
    def code_stash = context.config.stash.code_path ?: 'invalid-path'
 
    def bluemixEnv
    def bluemixOrg
    def bluemixAPI
    def bluemixCredentialsID
 
    def environment = targetBranch
 
    switch (environment) {
        case 'release-prod':
            bluemixEnv = context.config.environments.release.bluemix.env ?: ''
            bluemixOrg = context.config.environments.release.bluemix.org ?: ''
            bluemixAPI = context.config.environments.release.bluemix.api ?: ''
            bluemixCredentialsID = context.config.environments.release.bluemix.credentials ?: 'invalid-credentials'
            break
 
        default:
            bluemixEnv = context.config.environments.master.bluemix.env ?: ''
            bluemixOrg = context.config.environments.master.bluemix.org ?: ''
            bluemixAPI = context.config.environments.master.bluemix.api ?: ''
            bluemixCredentialsID = context.config.environments.master.bluemix.credentials ?: 'invalid-credentials'
    }
 
        node() {
        withCredentials([
                usernamePassword(credentialsId: bluemixCredentialsID,
                        passwordVariable: 'BM_PASS',
                        usernameVariable: 'BM_USER')
        ]) {
            withEnv([
                    "CF_HOME=${env.WORKSPACE}",
                    "BM_API=${bluemixAPI}",
                    "BM_ORG=${bluemixOrg}",
                    "BM_ENV=${bluemixEnv}",
                    "appname=${appname}",
                    "code_stash=$code_stash"
            ]) {
 
                        try {
                            unstash 'pipelines'
                            unstash "$code_stash"
                            sh 'pipelines/scripts/cf_deploy.sh'
                        }
                        catch (error) {
                            echo "Deployment failed"
                            throw error
                        } finally {
                            step([$class: 'WsCleanup', notFailBuild: true])
                        }
                    }
        }
    }
 
}
 
 
def purge(String targetBranch, context) {
    if ((targetBranch.toLowerCase().contains('master')||
        targetBranch.toLowerCase().contains('release-prod')||
        targetBranch.toLowerCase().contains('-np'))&& !targetBranch.toLowerCase().contains('patchset')) {
        echo "Purge aborted as the appname contains text 'master' or 'release-prod' or '-np'."    
    }else{
        echo "Purge initiated."
        node() {
            unstash 'pipelines'
            this.purgeHandler(targetBranch, context)
        }
    }
}
 
def purgeHandler(String targetBranch, context) {
 
    def appname = appName(context.application, targetBranch)
    def bluemixEnv
    def bluemixOrg
    def bluemixAPI
    def bluemixCredentialsID
 
    def environment = targetBranch
 
    switch (environment) {
        case 'release-prod':
            bluemixEnv = context.config.environments.release.bluemix.env ?: ''
            bluemixOrg = context.config.environments.release.bluemix.org ?: ''
            bluemixAPI = context.config.environments.release.bluemix.api ?: ''
            bluemixCredentialsID = context.config.environments.release.bluemix.credentials ?: 'invalid-credentials'
            break
 
        default:
            bluemixEnv = context.config.environments.master.bluemix.env ?: ''
            bluemixOrg = context.config.environments.master.bluemix.org ?: ''
            bluemixAPI = context.config.environments.master.bluemix.api ?: ''
            bluemixCredentialsID = context.config.environments.master.bluemix.credentials ?: 'invalid-credentials'
    }
 
    node() {
        withCredentials([
                usernamePassword(credentialsId: bluemixCredentialsID,
                        passwordVariable: 'BM_PASS',
                        usernameVariable: 'BM_USER')
        ]) {
            withEnv([
                    "CF_HOME=${env.WORKSPACE}",
                    "BM_API=${bluemixAPI}",
                    "BM_ORG=${bluemixOrg}",
                    "BM_ENV=${bluemixEnv}",
                    "appname=${appname}"
            ]) {
                try {
                    unstash 'pipelines'
                    echo "Purge initiated."
                    sh 'pipelines/scripts/cf_destroy.sh '
                } catch (error) {
                    echo "Purge failed, but we will continue regardless."
                } finally {
                    //
                    step([$class: 'WsCleanup', notFailBuild: true])
                }
            }
        }
    }
}
 
def name() {
    /*
     * return a unique name, eg: bluemix, cloudfoundry
     */
    return "Bluemix Deployer"
}
 
return this;