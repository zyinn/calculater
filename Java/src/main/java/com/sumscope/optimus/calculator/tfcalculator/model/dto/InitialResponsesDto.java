package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.calculator.shared.model.dto.FutureContractDto;


import java.util.List;

/**
 * 当页面第一次打开时用于初始化的响应Dto
 * 包括计算结果及一些用于前端显示的Dto
 */
public class InitialResponsesDto {

    private IniResponseDto calculationResult;

    private List<RepoRateDto> repoRates;

    private List<FutureContractDto> futures;

    public IniResponseDto getCalculationResult() {
        return calculationResult;
    }

    public void setCalculationResult(IniResponseDto calculationResult) {
        this.calculationResult = calculationResult;
    }

    public List<RepoRateDto> getRepoRates() {
        return repoRates;
    }

    public void setRepoRates(List<RepoRateDto> repoRates) {
        this.repoRates = repoRates;
    }

    public List<FutureContractDto> getFutures() {
        return futures;
    }

    public void setFutures(List<FutureContractDto> futures) {
        this.futures = futures;
    }
}
