#! /bin/bash


set -ex

  if [ -z "$BM_API" ]
    then 
      echo "BM_API unset"
      exit 1;
  fi 
  if [ -z "$BM_USER" ]
    then 
      echo "BM_USER Unset"
      exit 1;
  fi 
  if [ -z "$BM_PASS" ]
    then 
      echo "BM_PASS Unset"
      exit 1;
  fi 
  if [ -z "$BM_ORG" ]
    then 
      echo "BM_ORG Unset"
      exit 1;
  fi 
  if [ -z "$BM_ENV" ]
    then 
      echo "BM_ENV Unset"
      exit 1;
  fi  
  if [ -z "$appname" ]
    then 
      echo "appname Unset"
      exit 1;
  fi 

source ${WORKSPACE}/pipelines/scripts/functions


cf login -a $BM_API -u $BM_USER -p $BM_PASS -o ${BM_ORG} -s ${BM_ENV} 

cf delete ${appname} -f -r || echo "Not fatal"

cf delete-route lbg.eu-gb.mybluemix.net -n ${appname} -f || echo "Not fatal"

cf delete-route lbg.eu-gb.mybluemix.net -n lloydsbank-lp-${appname} -f || echo "Not fatal"
cf delete-route lbg.eu-gb.mybluemix.net -n halifax-online-hp-${appname} -f || echo "Not fatal"
cf delete-route lbg.eu-gb.mybluemix.net -n bankofscotland-bp-${appname} -f || echo "Not fatal"
