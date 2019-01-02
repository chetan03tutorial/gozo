#############################################################
#############################################################
@api @ignore
Feature: conversion eligibility API

  Scenario Outline: Check E160
      Given token service is up and running for the user having token "<tokenRequestFile>" for brand "<brand>"
      And user info service is also up and running "<brand>"
      And then hit the overdraft upfront service with the request "<overdraftRequestFile>" for brand "<brand>"
      And then hit the overdraft remove service "E160" with "<e160Request>" for brand "<brand>"
      And validate the currency code as "GBP"

    Examples:
      | brand   | tokenRequestFile                        | overdraftRequestFile                            | overdraftLimit    |e160Request|
      | halifax | jsonRequestFiles/overdraft/tokenRequest | jsonRequestFiles/overdraft/overdraftRequestFile | +00005750         |jsonRequestFiles/e160/e160Request|

