package com.lbg.ib.api.sales.docupload.mapper;

import com.lbg.ib.api.sales.docupload.domain.ProcessDetails;
import com.lbg.ib.api.sales.docupload.dto.transaction.ADRequest;
import com.lbg.ib.api.sales.docupload.dto.transaction.ADResponse;
import com.lbg.ib.api.sales.docupload.dto.transaction.ColleagueDTO;

public class ADServiceMapper {

    private ADServiceMapper() {

    }

    public static ADRequest convertToDto(ProcessDetails processDetails) {
        ADRequest requestDto = new ADRequest();

        ColleagueDTO colleagueDto = new ColleagueDTO();
        colleagueDto.setColleagueId(processDetails.getColleagueId());
        requestDto.setColleague(colleagueDto);

        return requestDto;
    }

    public static ProcessDetails convertToDomain(ADResponse responseDto) {
        ProcessDetails processDetails = new ProcessDetails();
        processDetails.setProcessList(responseDto.getProcessList());
        processDetails.setServiceResponse(responseDto.getServiceMessage());
        processDetails.setColleagueId(responseDto.getColleagueId());
        return processDetails;
    }
}
