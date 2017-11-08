package com.sumscope.optimus.calculator.shared.gatewayinvoke;

import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;

import java.util.List;

public interface CDHRepoRatesGateway {

    List<RepoRateDto> retrieveRepoRates();

}
