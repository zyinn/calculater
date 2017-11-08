package com.sumscope.optimus.calculator.shared.dao;

import com.sumscope.optimus.calculator.shared.model.dbmodel.*;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondCDCValuationInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_FLOOR;

/**
 * Created by fan.bai on 2016/7/11.
 * CalculationDao接口的实例类
 */
@Repository
public class CalculationDaoImpl implements CalculationDao {
    private static final String FUTRUE_CONTRACTS_RETRIEVE = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.retrieveFutureContracts";
    private static final String DELIVERABLE_BOND_KEY_RETRIEVE = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getDeliverableBondsKey";
    private static final String BONDS_CDC_LATEST_PRICE = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondCdcLatestPrice";
    private static final String BONDS_BY_NAME_PREFIX = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.findBondsByNamePrefix";
    private static final String BOND_INFO_BY_KEYS = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondInfoByKeys";
    private static final String NEXT_BUSINESS_DAY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getNextBusinessDay";
    private static final String LAST_BUSINESS_DAY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getLastWorkDay";

    private static final String SCHEDULED_BOND = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getScheduledBondByTerm";
    private static final String BOND_KEY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondKeyByTerm";
    private static final String SCHEDULED_BOND_BY_ID = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getScheduledBondById";


    private static final String BONDS_OFR_LATEST_PRICE_TODAY= "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondOfrLatestPriceToday";
    private static final String BONDS_OFR_LATEST_PRICE_HISTORY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondOfrLatestPriceHistory";

    private static final String BONDS_BID_LATEST_PRICE_TODAY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondBidLatestPriceToday";
    private static final String BONDS_BID_LATEST_PRICE_HISTORY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondBidLatestPriceHistory";

    private static final String BONDS_DEAL_LATEST_PRICE_TODAY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondDealLatestPriceToday";
    private static final String BONDS_DEAL_LATEST_PRICE_HISTORY = "com.sumscope.optimus.calculator.shared.dao.mapper.CalculationDaoMapper.getBondDealLatestPriceHistory";

    private static final int timeout = 3600;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    @CacheMe(timeout = timeout)
    public List<FutureContract> retrieveFutureContracts() {
        List<FutureContract> FutureContract = sqlSessionTemplate.selectList(FUTRUE_CONTRACTS_RETRIEVE);
        if(FutureContract!=null && FutureContract.size()>0){
            for(FutureContract futureContract:FutureContract){
                futureContract.setDeliveryDate(getYesterdayTime(futureContract.getDeliveryDate()));
            }
        }
        return FutureContract;
    }
    public static Date getYesterdayTime(Date date) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,day-1);
        return calendar.getTime();
    }
    @Override
    @CacheMe(timeout = timeout)
    public BigDecimal getBondOfrLatestPrice(String bondKey, String listedMarket, boolean onlyToday) {
        return getLatestPrice(bondKey, listedMarket, onlyToday, BONDS_OFR_LATEST_PRICE_TODAY);
    }



    @Override
    @CacheMe(timeout = timeout)
    public BigDecimal getBondBidLatestPrice(String bondKey, String listedMarket, boolean onlyToday) {
        return getLatestPrice(bondKey, listedMarket, onlyToday, BONDS_BID_LATEST_PRICE_TODAY);
    }

    @Override
    @CacheMe(timeout = timeout)
    public BigDecimal getBondsCdcLatestPrice(String bondKey, String listedMarket) {
        return getBondsCdcLatestInfo(bondKey,listedMarket)!=null ? getBondsCdcLatestInfo(bondKey,listedMarket).getYield(): null;
    }

    @Override
    @CacheMe(timeout = timeout)
    public BondCDCValuationInfo getBondsCdcLatestInfo(String bondKey, String listedMarket) {
        Map<String, Object> parameterMap = getParameterMap(bondKey, listedMarket, false);
        List<BondCDCValuationInfo> results = sqlSessionTemplate.selectList(BONDS_CDC_LATEST_PRICE, parameterMap);
        return (results!=null && results.size()>0) ? results.get(0) : null;
    }

    @Override
    @CacheMe(timeout = timeout)
    public BigDecimal getBondDealLatestPrice(String bondKey, String listedMarket,boolean onlyToday) {
        return getLatestPrice(bondKey, listedMarket, onlyToday, BONDS_DEAL_LATEST_PRICE_TODAY);
    }

    @Override
    public List<BondPrimaryKeyDto> findFixedCouponBondsByNamePrefix(String prefix) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("codePrefix", prefix);
        return sqlSessionTemplate.selectList(BONDS_BY_NAME_PREFIX, paramsMap);
    }

    @Override
    @CacheMe(timeout = timeout)
    public List<DeliverableBondKeyInfo> getDeliverableBondKeyWithCF(String tfKey) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tfKey", tfKey);
        return sqlSessionTemplate.selectList(DELIVERABLE_BOND_KEY_RETRIEVE, paramsMap);
    }

    @Override
    @CacheMe(timeout = timeout)
    public List<BondInfo> getBondInfoByKeys(List<String> bondKeys) {
        Map<String, Object> paramsMap = new HashMap<>();
        String inString = "";
        if (bondKeys != null && bondKeys.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String key : bondKeys) {
                sb.append("'").append(key).append("',");
            }
            inString = sb.toString().substring(1, sb.length() - 2);
        }
        paramsMap.put("bondKeyList", inString);
        return sqlSessionTemplate.selectList(BOND_INFO_BY_KEYS, paramsMap);
    }

    @Override
    public BondInfo getLatestBondByTerm(int term) {
        return null;
    }

    @Override
    @CacheMe(timeout = timeout)
    public List<ScheduledBondInfo> getScheduledBondKeysByTerm(int term) {
        return sqlSessionTemplate.selectList(SCHEDULED_BOND,term);
    }

    @Override
    @CacheMe(timeout = timeout)
    public Date getTodaysNextBusinessDate(String date) {
        List<Map<String, Date>> nextBDay = sqlSessionTemplate.selectList(NEXT_BUSINESS_DAY,date);
        if (nextBDay != null && nextBDay.size() > 0) {
            Map<String, Date> stringDateMap = nextBDay.get(0);
            return stringDateMap.get("next_bday");
        }
        return null;
    }

    @Override
    @CacheMe(timeout = timeout)
    public Date getLastWorkDate(String date) {
       return sqlSessionTemplate.selectOne(LAST_BUSINESS_DAY,date);
    }

    @Override
    public String getBondKeyByTerm(@Param(value = "deadLine") Integer deadLine) {
        return sqlSessionTemplate.selectOne(BOND_KEY,deadLine);
    }

    @Override
    @CacheMe(timeout = timeout)
    public ScheduledBondInfo getScheduledBondById(String id) {
        return sqlSessionTemplate.selectOne(SCHEDULED_BOND_BY_ID,id);
    }

    private BigDecimal getLatestPrice(String bondKey, String listedMarket, boolean onlyToday, String target) {
        Map<String, Object> paramsMap = getParameterMap(bondKey, listedMarket, onlyToday);
        List<Map<String, Double>> list = sqlSessionTemplate.selectList(target, paramsMap);
        Double yield = (list != null && list.size() > 0) ? (list.get(0) != null ? list.get(0).get("yield") : null) : null;
        return yield != null ? new BigDecimal(yield).setScale(4,ROUND_FLOOR) : null;
    }

    private Map<String, Object> getParameterMap(String bondKey, String listedMarket, boolean onlyToday) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("bondKey", bondKey);
        paramsMap.put("listedMarket", listedMarket);
        paramsMap.put("onlyToday", onlyToday);
        return paramsMap;
    }
}
