#############################################################
#############################################################
@api @ignore
Feature: conversion eligibility API

  Scenario Outline: Check E160
      Given token service is up and running for the user having token "<tokenRequestFile>" for brand "<brand>"
      And user info service is also up and running "<brand>"
      And then hit the overdraft upfront service with the request "<overdraftRequestFile>" for brand "<brand>"
      And then hit the overdraft amend service "E170" with "<e170Request>" for brand "<brand>"
      And validate the featureNextReviewFlagDate as "1"

    Examples:
      | brand   | tokenRequestFile                        | overdraftRequestFile                            | overdraftLimit    |e170Request|
      | halifax | jsonRequestFiles/overdraft/tokenRequest | jsonRequestFiles/overdraft/overdraftRequestFile | +00005750         |jsonRequestFiles/e170/e170Request|

