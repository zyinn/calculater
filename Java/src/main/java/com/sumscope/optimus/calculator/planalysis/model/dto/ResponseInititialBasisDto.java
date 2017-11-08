package com.sumscope.optimus.calculator.planalysis.model.dto;

import java.util.List;

/**
 * Created by simon.mao on 2016/4/27.
 * 初次打开界面时获得的初始数据。
 */
public class ResponseInititialBasisDto {
    /**
     * 所有期货合约列表
     */
    private List<FutureContractDto> futureContracts;
    /**
     * 资金成本列表
     */
    private List<RepoRateDto> repoRateTypes;
    /**
     * 默认资金成本
     */
    private RepoRateDto repoRateDto;
    /**
     * 根据CTD计算的结果
     */
    private ResponseDto responseDto;

    /**
     * 开仓日
     */
    private String openPositionDate;

    /**
     * 平仓日
     */
    private String closePositionDate;

    public List<FutureContractDto> getFutureContracts() {
        return futureContracts;
    }

    public void setFutureContracts(List<FutureContractDto> futureContracts) {
        this.futureContracts = futureContracts;
    }

    public List<RepoRateDto> getRepoRateTypes() {
        return repoRateTypes;
    }

    public void setRepoRateTypes(List<RepoRateDto> repoRateTypes) {
        this.repoRateTypes = repoRateTypes;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public RepoRateDto getRepoRateDto() {
        return repoRateDto;
    }

    public void setRepoRateDto(RepoRateDto repoRateDto) {
        this.repoRateDto = repoRateDto;
    }

    public String getOpenPositionDate() {
        return openPositionDate;
    }

    public void setOpenPositionDate(String openPositionDate) {
        this.openPositionDate = openPositionDate;
    }

    public String getClosePositionDate() {
        return closePositionDate;
    }

    public void setClosePositionDate(String closePositionDate) {
        this.closePositionDate = closePositionDate;
    }
}
