package com.lbg.ib.api.sales.communication.service;

import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;

public interface CommunicationDispatchService {

    ScheduleEmailSmsResponse scheduleCommunication(ScheduleEmailSmsRequest commsRequest);

    ScheduleEmailSmsResponse sendCommunication(ScheduleEmailSmsRequest userRequest);
}
