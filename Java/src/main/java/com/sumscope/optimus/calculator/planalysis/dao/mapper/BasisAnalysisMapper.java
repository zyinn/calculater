package com.sumscope.optimus.calculator.planalysis.dao.mapper;

import com.sumscope.optimus.calculator.planalysis.model.dto.FutureContractDto;

import java.util.List;

/**
 * Created by simon.mao on 2016/4/27.
 */
public interface BasisAnalysisMapper {
    public List<FutureContractDto> getFutureContracts() ;

    public List<String> getDeliverableBonds(FutureContractDto futureContractDto);
}
