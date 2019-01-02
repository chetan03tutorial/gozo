package com.lbg.ib.api.sales.communication.service;
/*
Created by Rohit.Soni at 18/06/2018 13:50
*/

import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationRequest;

public interface ScheduleCommunicationService {
    public void invokeScheduleCommunication(ScheduleCommunicationRequest scheduleCommunicationRequest);
}
