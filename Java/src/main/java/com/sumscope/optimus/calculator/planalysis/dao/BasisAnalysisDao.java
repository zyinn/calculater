package com.sumscope.optimus.calculator.planalysis.dao;

import com.sumscope.optimus.calculator.planalysis.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.planalysis.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.FutureContractDto;
import com.sumscope.optimus.calculator.planalysis.model.dbmodel.BondCDCValuationInfo;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by fan.bai on 2016/5/3.
 * Dao层接口，用于获取数据库数据
 */
public interface BasisAnalysisDao {
    /**
     * 获取所有期货合约
     * @return 所有期货合约以及其最新价格
     */
    List<FutureContractDto> retrieveFutureContracts();

    /**
     * 根据债卷bond_key获取债卷基本信息
     * @param bondKey 债卷bond_key
     * @return BondInfo 债卷基本信息
     */
    BondInfo getBondInfo(String bondKey);

    /**
     * 获取某期货合约的所有可交割卷
     * @param bondKeys 指定的期货
     * @return 某期货合约的所有可交割卷
     */
    List<BondInfo> getDeliverableBonds(String bondKeys);

    List<Map<String,String>> getDeliverableBondKeys(String tfKey);

    /**
     * 根据用户输入的4个前缀，搜索所有的固息卷
     * @param prifix 4位前缀，例如 "1500"
     * @return 所有符合前缀的债券机器成交收益率
     */
    List<BondPrimaryKeyDto> findBondsByNamePrefix(String prifix);

}
