#############################################################
#############################################################
@api @ignore
Feature: conversion eligibility API

  Scenario Outline: Check E160
      Given token service is up and running for the user having token "<tokenRequestFile>" for brand "<brand>"
      And user info service is also up and running "<brand>"
      And then hit the overdraft upfront service with the request "<overdraftRequestFile>" for brand "<brand>"
      And then hit the overdraft create service "E169" with "<e169Request>" for brand "<brand>"
      And validate the hasOdIssued as "true"

    Examples:
      | brand   | tokenRequestFile                        | overdraftRequestFile                            | overdraftLimit    |e169Request|
      | halifax | jsonRequestFiles/overdraft/tokenRequest | jsonRequestFiles/overdraft/overdraftRequestFile | +00005750         |jsonRequestFiles/e169/e169Request|

