package com.sumscope.optimus.calculator.shared.gatewayinvoke;
import com.sumscope.optimus.calculator.planalysis.gateway.RepoRateGatewayService;
import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuejian.sun on 2016/8/24.
 * 获取资金成本列表
 */
@Component
public class CDHRepoRatesGatewayImpl implements CDHRepoRatesGateway {

    @Autowired
    private RepoRateGatewayService repoRatesGateway;

    @Override
    @CacheMe(timeout = 3600)
    public List<RepoRateDto> retrieveRepoRates() {
        return repoRatesGateway.retrieveRepoRatesWithPrice();
    }
}
