#! /bin/bash
 
set -ex
WORKSPACE=`pwd`
 
source pipelines/scripts/functions
 
cd api-automation
 
docker_terminate(){
  sudo docker stop $1
  sudo docker rm $1
}
 
docker_cleanup(){
  docker_terminate $zapID
  docker_terminate $seleniumID
}
trap docker_cleanup EXIT
 
LOCAL_IP=$(hostname -i)
echo "Local IP is ${LOCAL_IP}"
 
echo "Starting ZAP proxy..."
sudo docker pull $docker_image
zapID=$(sudo docker run -l job=$BUILD_TAG -d -u zap -p :8080 -i $docker_image \
             zap-x.sh -daemon -host 0.0.0.0 -Xmx1024M \
                      -config api.disablekey=true \
                      -config api.addrs.addr.name=.* \
                      -config api.addrs.addr.regex=true)
ZAP_PORT=$(sudo docker port $zapID 8080 | sed 's/.*://')
 
timeout=600
polltime=3
while ! curl --noproxy ${LOCAL_IP} --connect-timeout 3 -s -H "Host:shared-nexus-01" ${LOCAL_IP}:${ZAP_PORT} >/dev/null ; do
  sleep $polltime
  time=$((time + polltime))
  if [ $time -gt $timeout ] ; then
    echo "Failed to start ZAP in $timeout seconds. Aborting"
    exit 200
  fi
done
echo "ZAP proxy running on ${LOCAL_IP}:${ZAP_PORT}"
 
export HTTP_PROXY=${LOCAL_IP}:${ZAP_PORT}
export HTTPS_PROXY=${LOCAL_IP}:${ZAP_PORT}
export http_proxy=${LOCAL_IP}:${ZAP_PORT}
export https_proxy=${LOCAL_IP}:${ZAP_PORT}
export no_proxy=localhost,127.0.0.1
 
# TODO: I need to ensure selenium version here matches one requested in tests
# TODO: to do this, I either need to have container with many/all versions in it
# TODO: or some other system that can provide this. If other system, it needs
# TODO: to be able to connect to my instance of ZAP. I can probably configure this
# TODO: via environment variables and read them in gulp task.
 
# echo "Starting Selenium server container..."
# seleniumID=$(sudo docker run -l job=$BUILD_TAG -d -p :4444 \
#                              -e HTTP_PROXY=${LOCAL_IP}:${ZAP_PORT} \
#                              -e http_proxy=${LOCAL_IP}:${ZAP_PORT} \
#                              -i selenium/standalone-firefox:2.53.1-beryllium)
# SELENIUM_PORT=$(sudo docker port $seleniumID 4444 | sed 's/.*://')
 
# timeout=600
# polltime=3
# while ! curl --noproxy ${LOCAL_IP} --connect-timeout 3 -s -H "Host:shared-nexus-01" ${LOCAL_IP}:${SELENIUM_PORT} >/dev/null ; do
#   sleep $polltime
#   time=$((time + polltime))
#   if [ $time -gt $timeout ] ; then
#     echo "Failed to start Selenium container in $timeout seconds. Aborting"
#     exit 201
#   fi
# done
# echo "Selenium server running on ${LOCAL_IP}:${SELENIUM_PORT}"
# export SELENIUM_PORT
 
echo "XXXX: Gulp now follows"
echo "=========================================="
echo $LLOYDS_API_DOMAIN
echo "=========================================="
 
mvn test -Dendpoint.lloyds=$LLOYDS_API_DOMAIN -Dendpoint.bos=$BOS_API_DOMAIN -Dendpoint.halifax=$HFAX_API_DOMAIN -Dhttp.proxyHost=${LOCAL_IP} -Dhttp.proxyPort=${ZAP_PORT} || true
 
mkdir -p zap-report
echo "XXXX: Getting ZAP report..."
sudo docker exec $zapID zap-cli report -f html -o report.html
sudo docker exec $zapID zap-cli report -f xml -o report.xml
sudo docker exec $zapID cat report.html >${RESULTSDIR}/report.html
sudo docker exec $zapID cat report.xml >${RESULTSDIR}/report.xml
echo "---------------------------------------------------"
cat ${RESULTSDIR}/report.xml
echo "---------------------------------------------------"
 
echo "XXXX: Gulp done. Selenium and ZAP will be stopped on exit."
 
# Remove spaces from outputfiles
#cd ${RESULTSDIR}
#find . -type f -name "* *.json" -exec bash -c 'mv "$0" "${0// /_}"' {} \;
 
 
# Remove absolute paths from BDD json. Dont let this fail the build
#sed -i 's+'${WORKSPACE}'/++g'  *.* || true