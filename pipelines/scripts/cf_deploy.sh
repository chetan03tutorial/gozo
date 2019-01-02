#!/usr/bin/env bash



source ${WORKSPACE}/pipelines/scripts/functions

error_generate(){
if ! [ "${exit_code}" == "" ];then
	echo -e '\E[37;44m'"\033[1m${1}\033[0m"
	echo $1 >> ${WORKSPACE}/failure.txt
	exit 1
fi
}

echo "Switch to working directory $code_stash ..."
cd ${WORKSPACE}/${code_stash} && cf logout

echo "Login to bluemix Org: $BM_ORG , Space: $BM_ENV ..."
cf login -a $BM_API -u $BM_USER -p $BM_PASS -o $BM_ORG -s $BM_ENV || exit_code=$?
error_generate  "Failed to login to Bluemix."

echo "Deleting existing application with name: ${appname} ..."
cf delete ${appname} -f -r || echo "Failed to delete application."

echo "Deleting existing application routes associated with app: ${appname} ..."
cf delete-route lbg.eu-gb.mybluemix.net -n  ${appname} -f || echo ""

cf delete-route lbg.eu-gb.mybluemix.net -n  lloyds-lp-${appname} -f || echo ""
cf delete-route lbg.eu-gb.mybluemix.net -n  halifax-hp-${appname} -f || echo ""
cf delete-route lbg.eu-gb.mybluemix.net -n  bos-bp-${appname} -f || echo ""


warname=$(basename target/*.war)
rm -rf target/${warname%.war}* || echo ""

echo "Publish repo to bluemix ..."
cf push ${appname} -b http://gerrit.sandbox.extranet.group:8080/core-luis-buildpack -t 180 -m 2G -u none || exit_code=$?
error_generate "Failed to push the code to Bluemix."

sleep 6m

echo "Creating routes for application: ${appname}, route : lloyds-lp-${appname} ..."
cf map-route ${appname} lbg.eu-gb.mybluemix.net -n lloyds-lp-${appname}  || exit_code=$?
error_generate "Failed to create map-route : lloyds-lp-${appname}"

echo "Creating routes for application: ${appname}, route : halifax-hp-${appname} ..."
cf map-route ${appname} lbg.eu-gb.mybluemix.net -n halifax-hp-${appname}  || exit_code=$?
error_generate "Failed to create map-route : halifax-hp-${appname}"

echo "Creating routes for application: ${appname}, route : bos-bp-${appname} ..."
cf map-route ${appname} lbg.eu-gb.mybluemix.net -n bos-bp-${appname} || exit_code=$?
error_generate "Failed to create map-route : bos-bp-${appname} "

#echo "Sleep for 2 mins, in order to get luis server up ..."
# sleep $((2*60))
