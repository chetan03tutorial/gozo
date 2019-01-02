/*
 * Author: Abhay Chrungoo <achrungoo@sapient.com>
 * Contributing HOWTO: TODO
 */

def runTest(String targetBranch, context) {
    /*
     * Lightweight operations that dont require a workspace.
     * eg: variable manipulations.
     */
    node() {
        /*
         * Your implementation that requires a workspace.
         * May need to produce multiple test artifacts
         *
         * Publish results to Jenkins using plugins
         * eg: cucumber reports, cobertura reports
         *
         * Stash any reports that may need to be published to splunk
         * or used in a later step
         *
         * Use try {} catch{} finally {} blocks to continue beyod failures and
         * save/publish any intermediate results if needed
         */
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
}

String name() {
    return "Some name that uniquely identifies the specific kind of test. eg: pa11y, sitespeed etc"
}

return this;
