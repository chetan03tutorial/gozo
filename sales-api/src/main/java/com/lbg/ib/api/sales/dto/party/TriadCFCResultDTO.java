package com.lbg.ib.api.sales.dto.party;
/*
Created by Rohit.Soni at 31/05/2018 11:26
*/

import java.util.List;

public class TriadCFCResultDTO {
    List<TriadResultDTO> triadResultDTOList;
    List<CFCNewLimitResultDTO> cfcNewLimitResultDTOS;

    public TriadCFCResultDTO(List<TriadResultDTO> triadResultDTOList, List<CFCNewLimitResultDTO> cfcNewLimitResultDTOS) {
        this.triadResultDTOList = triadResultDTOList;
        this.cfcNewLimitResultDTOS = cfcNewLimitResultDTOS;
    }

    public List<TriadResultDTO> getTriadResultDTOList() {
        return triadResultDTOList;
    }

    public void setTriadResultDTOList(List<TriadResultDTO> triadResultDTOList) {
        this.triadResultDTOList = triadResultDTOList;
    }

    public List<CFCNewLimitResultDTO> getCfcNewLimitResultDTOS() {
        return cfcNewLimitResultDTOS;
    }

    public void setCfcNewLimitResultDTOS(List<CFCNewLimitResultDTO> cfcNewLimitResultDTOS) {
        this.cfcNewLimitResultDTOS = cfcNewLimitResultDTOS;
    }
}
