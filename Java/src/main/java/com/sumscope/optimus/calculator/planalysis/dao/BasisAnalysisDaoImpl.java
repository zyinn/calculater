package com.sumscope.optimus.calculator.planalysis.dao;

import com.sumscope.optimus.calculator.planalysis.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.planalysis.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.FutureContractDto;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon.mao on 2016/4/27.
 * BasisAnalysisDao 的实现
 */
@Service
public class BasisAnalysisDaoImpl implements BasisAnalysisDao {
    private static final String FUTRUE_CONTRACTS_RETRIEVE = "com.sumscope.optimus.calculator.planalysis.dao.mapper.BasisAnalysisMapper.getFutureContracts";
    private static final String DELIVERABLE_BOND_KEY_RETRIEVE = "com.sumscope.optimus.calculator.planalysis.dao.mapper.BasisAnalysisMapper.getDeliverableBondsKey";
    private static final String DELIVERABLE_BONDS_RETRIEVE = "com.sumscope.optimus.calculator.planalysis.dao.mapper.BasisAnalysisMapper.getDeliverableBonds";
    private static final String BONDS_BY_NAME_PREFIX = "com.sumscope.optimus.calculator.planalysis.dao.mapper.BasisAnalysisMapper.findBondsByNamePrefix";
    private static final String BOND_INFO = "com.sumscope.optimus.calculator.planalysis.dao.mapper.BasisAnalysisMapper.getBondInfo";
    private static final int TIMEOUT = 3600;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    @CacheMe(timeout = TIMEOUT)
    public List<FutureContractDto> retrieveFutureContracts() {
        return sqlSessionTemplate.selectList(FUTRUE_CONTRACTS_RETRIEVE);
    }

    @Override
    @CacheMe(timeout = TIMEOUT)
    public List<Map<String,String>> getDeliverableBondKeys(String tfKey) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tfKey", tfKey);
        return sqlSessionTemplate.selectList(DELIVERABLE_BOND_KEY_RETRIEVE, paramsMap);
    }

    @Override
    @CacheMe(timeout = TIMEOUT)
    public List<BondInfo> getDeliverableBonds(String bondKeys) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bondKeyList", bondKeys);
        return sqlSessionTemplate.selectList(DELIVERABLE_BONDS_RETRIEVE, paramsMap);
    }

    @Override
    @CacheMe(timeout = TIMEOUT)
    public BondInfo getBondInfo(String bondKey) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("bondKey", bondKey);
        List<BondInfo> list = sqlSessionTemplate.selectList(BOND_INFO, paramsMap);
        return list.get(0);
    }

    @Override
    @CacheMe(timeout = TIMEOUT)
    public List<BondPrimaryKeyDto> findBondsByNamePrefix(String prefix) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("codePrefix", prefix);
        return sqlSessionTemplate.selectList(BONDS_BY_NAME_PREFIX, paramsMap);
    }

}
