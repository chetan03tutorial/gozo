#!/bin/bash

source "${WORKSPACE}/pipelines/scripts/functions"

set -ex

which awk
which jq


if [ -z "${SCENARIO_PASS_THRESHOLD}" ]
   then 
     echo "SCENARIO_PASS_THRESHOLD unset"
     exit 1;
fi 


#cd api-automation/target/reports/

if [ "$(ls *.json 2>/dev/null)" == "" ];then
	exit_code=1
	error_generate "There are no json report files to process for cucumber."
fi

bddAnalysis(){
failCounter=0
passedCounter=0
for file in $(ls *.json)
do
	totalTC=$(cat $file | jq ".[] | .id" | wc -l)
	for eachTC in $(seq 0 $((totalTC-1)))
	do
		elementsLength=$(cat $file | jq ".[${eachTC}].elements|length")
		for eachElement in $(seq 0 $((elementsLength-1)))
		do
			if [ "$(cat $file | jq ".[${eachTC}] | .elements[${eachElement}].steps[]" | grep "\"status\"" | sed 's/skipped/failed/g' | grep -o failed | sort -u)" == "failed" ];then
				failCounter=$((failCounter+1))
			elif [ "$(cat $file | jq ".[${eachTC}] | .elements[${eachElement}].steps[]" | grep "\"status\"" | sed 's/skipped/failed/g' | grep -o passed | sort -u)" == "passed" ];then
				passedCounter=$((passedCounter+1))
			fi
		done
	done
done

totalCounter=$((${failCounter}+${passedCounter}))
passedPer=$(echo $passedCounter $totalCounter | awk '{printf "%.f \n", $1/$2*100}')

exit_code=0
if [ "${passedPer}" -lt "$SCENARIO_PASS_THRESHOLD" ];then
	echo -e '\E[37;44m'"\033[1mBDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %.\033[0m"
	echo "BDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %." >> ${WORKSPACE}/failure.txt
	echo "${passedPer}" >> ${WORKSPACE}/pass_per.txt
	exit_code=1
elif [ "${passedPer}" -gt "$SCENARIO_PASS_THRESHOLD" ];then
	echo -e '\E[37;44m'"\033[1mBDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %.\033[0m"
	echo "BDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %." >> ${WORKSPACE}/failure.txt
	echo "${passedPer}" >> ${WORKSPACE}/pass_per.txt
	echo "BDD_PASS_SCENARIO_PASS_THRESHOLD"
elif [ "${passedPer}" == "" ];then
	echo -e '\E[37;44m'"\033[1mBDD pass percentage: 0 . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD%.\033[0m"
	echo "BDD pass percentage: 0 . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %" >> ${WORKSPACE}/failure.txt
	echo "0" >> ${WORKSPACE}/pass_per.txt
	exit_code=1
elif [ "${passedPer}" == "$SCENARIO_PASS_THRESHOLD" ];then
	echo -e '\E[37;44m'"\033[1mBDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %.\033[0m"
	echo "BDD pass percentage: ${passedPer}% . SCENARIO_PASS_THRESHOLD: $SCENARIO_PASS_THRESHOLD %." >> ${WORKSPACE}/failure.txt
	echo "${passedPer}" >> ${WORKSPACE}/pass_per.txt
	echo "BDD_PASS_SCENARIO_PASS_THRESHOLD"
fi
}
if [ "$(ls *.json 2>/dev/null )" != "" ];then
	bddAnalysis
fi

exit $exit_code