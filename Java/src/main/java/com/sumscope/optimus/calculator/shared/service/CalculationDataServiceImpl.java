package com.sumscope.optimus.calculator.shared.service;


import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.shared.dao.CalculationDao;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.DeliverableBondKeyInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuejian.sun on 2016/8/2.
 */
@Service
public class CalculationDataServiceImpl implements CalculationDataService{

    @Autowired
    private CalculationDao calculationDao;

    private static final String FORMAT = "yyyy-MM-dd";

    private static final String LISTEDMARKET = "CIB";


    @Override
    @CacheMe(timeout = 3600)
    public BondInfo getBondInfoByKeys(String bondKey) {
        List<String> bondList = new ArrayList<>();
        bondList.add(bondKey);
        List<BondInfo> bondInfoByKeys = calculationDao.getBondInfoByKeys(bondList);
        return (bondInfoByKeys!=null&& bondInfoByKeys.size()>0) ? bondInfoByKeys.get(0) : null;
    }

    //获取可交割卷的债券代码列表
    @Override
    public List<BondInfo> getDeliverableBonds(String tfKey) {
        List<String> bondKeyList = new ArrayList<String>();
        List<DeliverableBondKeyInfo> deliverableBondKeyWithTfKey = calculationDao.getDeliverableBondKeyWithCF(tfKey);
        if(deliverableBondKeyWithTfKey!=null && deliverableBondKeyWithTfKey.size()>0){
            for(DeliverableBondKeyInfo deliverableBondKeyInfo : deliverableBondKeyWithTfKey){
                bondKeyList.add(deliverableBondKeyInfo.getBondKey());
            }
        }
        return calculationDao.getBondInfoByKeys(bondKeyList);
    }

    @Override
    public BigDecimal getBondOfrLatestPrice(String bondKey, String listedMarket, boolean onlyToday) {
        return calculationDao.getBondOfrLatestPrice(bondKey, listedMarket, true);
    }

    @Override
    public BigDecimal getBondBidLatestPrice(String bondKey, String listedMarket, boolean onlyToday) {
        return calculationDao.getBondBidLatestPrice(bondKey, listedMarket, true);
    }

    @Override
    public BigDecimal getBondsCdcLatestPrice(String bondKey, String listedMarket) {
        return calculationDao.getBondsCdcLatestPrice(bondKey, listedMarket);
    }

    @Override
    public BigDecimal getBondDealLatestPrice(String bondKey, String listedMarket) {
        return calculationDao.getBondDealLatestPrice(bondKey, listedMarket,true);
    }

    @Override
    public List<BondPrimaryKeyDto> findBondsByNamePrefix(String prifix) {
        return calculationDao.findFixedCouponBondsByNamePrefix(prifix);
    }

    @Override
    public BondInfo getLatestBondByTerm(int term) {
        return null;
    }

    @Override
    @CacheMe(timeout = 3600)
    public List<FutureContract> retrieveFutureContracts() {
        List<FutureContract> futureContracts = calculationDao.retrieveFutureContracts();
        List<FutureContract> list=new ArrayList<>();
        list.addAll(futureContracts);

        String format = new SimpleDateFormat(FORMAT).format(new Date());
        if(list!=null && list.size()>0) {
            for (FutureContract futureContract : list) {
                futureContract.setTradingDate(getTodaysNextBusinessDate(format));
            }
            return list;
        }
        return null;
    }

    @Override
    @CacheMe(timeout = 3600)
    public List<ScheduledBondInfo> getScheduledBonds(int term) {
        List<ScheduledBondInfo> scheduledBond= calculationDao.getScheduledBondKeysByTerm(term);
        if(scheduledBond!=null && scheduledBond.size()>0){
            for (ScheduledBondInfo bi : scheduledBond){
                setScheduledBondInfo(bi);
                //国债类型是'BGB'/ 'SGB'/ 'EGB',BOND_ID不能为空，发行日要大于当前时间，以年为单位，取第一个即最新报价
                String bondKey = calculationDao.getBondKeyByTerm(bi.getDeadLine());
                BigDecimal deal = this.getBondDealLatestPrice(bondKey, LISTEDMARKET);
                if(deal==null){
                    deal = getBondsCdcLatestPrice(bondKey, LISTEDMARKET);
                    if(deal==null){
                        deal = new BigDecimal(0);
                    }
                }
                getYied(deal,bi,bondKey);
            }
            return scheduledBond;
        }
            return null;
    }

    private void setScheduledBondInfo(ScheduledBondInfo bi) {
        long time = 24*60*60*1000; //一天
        Date todaysNextBusinessDate = getTodaysNextBusinessDate(new SimpleDateFormat(FORMAT).format(bi.getIssueStartDate()));
        if(todaysNextBusinessDate==null){
            Date sqlDate = new java.sql.Date(bi.getIssueStartDate().getTime()+time*3);
            todaysNextBusinessDate=sqlDate;
        }
        if(todaysNextBusinessDate.toString().equals(new SimpleDateFormat(FORMAT).format(bi.getIssueStartDate()))){
            bi.setInterestStartDate(new SimpleDateFormat(FORMAT).format(bi.getIssueStartDate().getTime()+time)); //起息日等于 发行日+1个工作日
        }else {
            bi.setInterestStartDate(todaysNextBusinessDate.toString());
        }
        bi.setMaturityDate(bi.getInterestStartDate().replace(bi.getInterestStartDate().substring(0,4),Integer.valueOf(bi.getInterestStartDate().substring(0,4))+bi.getDeadLine()+"")); //过期日 等于用户选择的国债年限+起息日年限
        bi.setShortName(bi.getDeadLine()+"Y国债");
    }

    @Override
//    @CacheMe(timeout = 3600)
    public ScheduledBondInfo getScheduledBondById(String id) {
        ScheduledBondInfo scheduledBondInfo = calculationDao.getScheduledBondById(id);
        scheduledBondInfo.setId(id);
        setScheduledBondInfo(scheduledBondInfo);
        String bondKey = calculationDao.getBondKeyByTerm(scheduledBondInfo.getDeadLine());
        BigDecimal deal = this.getBondDealLatestPrice(bondKey, LISTEDMARKET);
        if(deal==null){
            deal = new BigDecimal(0);
        }
        getYied(deal,scheduledBondInfo,bondKey);
        return scheduledBondInfo;
    }


    /**
     * 获取可交割卷的转换因子
     * @return
     */
    @CacheMe(timeout = 3600)
    public List<BondConvertionFactorDto> getDeliverableBondConvertionFactor(String tfKey) {
        List<BondConvertionFactorDto> results = new ArrayList<>();
        List<DeliverableBondKeyInfo> deliverableBond = calculationDao.getDeliverableBondKeyWithCF(tfKey);
        for (DeliverableBondKeyInfo dbk : deliverableBond){
            BondConvertionFactorDto bcf = new BondConvertionFactorDto();
            BeanUtils.copyProperties(dbk,bcf);
            results.add(bcf);
        }
        return results==null?new ArrayList<>():results;
    }

    @CacheMe(timeout = 3600)
    public List<KeyValuePair> getYieldByDeliverableBonds(List<BondInfo> deliverableBonds,List<ScheduledBondInfo> scheduledBondInfos ,CalculationRequestForInit calculationRequestForInit) {
        List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
        List<BondInfo> deliverable=new ArrayList<>();
        if(deliverableBonds!=null && deliverableBonds.size()>0){
            deliverable.addAll(deliverableBonds);
        }
        if(deliverable!=null&&deliverable.size()>0){
            List<BondInfo> bondInfoWithoutPrice = new ArrayList<BondInfo>();
            for (BondInfo bondInfo : deliverable) {
                BigDecimal price =this.getBondOfrLatestPrice(bondInfo.getBondKey(), bondInfo.getListedMarket(), true);
                if(price==null){
//                    price=new BigDecimal(0.0);
                    price=this.getBondsCdcLatestPrice(bondInfo.getBondKey(), bondInfo.getListedMarket());
                }
                if (price == null|| "".equals(price)) {
                    bondInfoWithoutPrice.add(bondInfo);
                } else {
                    KeyValuePair keyValuePair = new KeyValuePair();
                    String code=bondInfo.getBondCode();
                    keyValuePair.setKey(code);//.substring(0,code.indexOf(".IB"))
                    keyValuePair.setValue(NumberUtil.divide(price, 100));
                    keyValuePairs.add(keyValuePair);
                }
            }
            List<BondInfo> deliverableBond=new ArrayList<>();
            deliverableBond.addAll(deliverableBonds);
            deliverable.removeAll(bondInfoWithoutPrice);
            if(deliverableBond==null || deliverableBond.size()==bondInfoWithoutPrice.size()){
                    KeyValuePair keyValuePair = new KeyValuePair();
                    String code=deliverableBond.get(0).getBondCode();
                    keyValuePair.setKey(code);//.substring(0,code.indexOf(".IB"))
                    keyValuePair.setValue(NumberUtil.divide(this.getBondsCdcLatestPrice(deliverableBond.get(0).getBondKey(),deliverableBond.get(0).getListedMarket()), 100));
                    keyValuePairs.add(keyValuePair);
                    calculationRequestForInit.setSelectableBonds(deliverableBond);
            }else{
                deliverableBonds.removeAll(bondInfoWithoutPrice);
            }
        }

        List<ScheduledBondInfo> scheduled=new ArrayList<>();
        if(scheduledBondInfos!=null && scheduledBondInfos.size()>0){
            scheduled.addAll(scheduledBondInfos);
        }
        if(scheduled!=null&&scheduled.size()>0){
            List<ScheduledBondInfo> bondInfoWithoutPrice = new ArrayList<ScheduledBondInfo>();
            for (ScheduledBondInfo bondInfo : scheduled) {
                String bondKey = calculationDao.getBondKeyByTerm(bondInfo.getDeadLine());
                //未发国债的收益率默认取成交价
                BigDecimal bondDealLatestPrice = getBondDealLatestPrice(bondKey, LISTEDMARKET);
                BigDecimal deal = bondDealLatestPrice!=null ? bondDealLatestPrice : getBondsCdcLatestPrice(bondKey, LISTEDMARKET);
                if(deal==null){
                    deal=new BigDecimal(0.0);
                }
                getYied(deal,bondInfo,bondKey);
                if (deal == null || "".equals(deal)) {
                    bondInfoWithoutPrice.add(bondInfo);
                } else {
                    KeyValuePair keyValuePair = new KeyValuePair();
                    String code=bondInfo.getId();
                    keyValuePair.setKey(code);
                    keyValuePair.setValue(NumberUtil.divide(deal, 100));
                    keyValuePairs.add(keyValuePair);
                }
            }
            calculationRequestForInit.setScheduledBondInfos(scheduledBondInfos);
            scheduled.removeAll(bondInfoWithoutPrice);
        }
        return keyValuePairs;
    }



    @Override
    public Date getTodaysNextBusinessDate(String date) {
        return calculationDao.getTodaysNextBusinessDate(date);
    }

    @Override
    public Date getLastWorkDate(String date) {
        return calculationDao.getLastWorkDate(date);
    }

    private void getYied(BigDecimal deal,ScheduledBondInfo sb,String bondKey){
        if(deal!=null &&  !deal.equals(new BigDecimal(0))){
            sb.setYield(deal);
            sb.setCouponRate(deal);
            sb.setYieldType(YieldTypeEnum.deal);
            return;
        }
        if(deal ==new BigDecimal(0) || deal.equals(new BigDecimal(0))){
            BigDecimal cdc = getBondsCdcLatestPrice(bondKey, LISTEDMARKET);
            sb.setYield(cdc);
            sb.setCouponRate(cdc);
            sb.setYieldType(YieldTypeEnum.cdc);
            return;
        }
        BigDecimal ofr = getBondOfrLatestPrice(bondKey, LISTEDMARKET, true);
        if(ofr!=null){
            sb.setYield(ofr);
            sb.setCouponRate(ofr);
            sb.setYieldType(YieldTypeEnum.ofr);
            return;
        }
        BigDecimal bid = getBondBidLatestPrice(bondKey,LISTEDMARKET,true);
        if(bid!=null){
            sb.setYield(bid);
            sb.setCouponRate(bid);
            sb.setYieldType(YieldTypeEnum.bid);
            return;
        }
        BigDecimal cdc = getBondsCdcLatestPrice(bondKey, LISTEDMARKET);
        if(cdc !=null){
            sb.setYield(cdc);
            sb.setCouponRate(cdc);
            sb.setYieldType(YieldTypeEnum.cdc);
            return;
        }
//        if(deal==null&&ofr==null&&bid==null&&cdc==null){
//            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.DATABASE_ERROR,"ofr/bid/成交/中债为空,请稍后再试或者联系管理员");
//        }

    }

    @Override
    @CacheMe(timeout = 3600)
    public Map<String,FutureContract> retrieveFutureContractByTfId(){
        Map<String,FutureContract> map=new HashMap<>();
        List<FutureContract> futureContracts = calculationDao.retrieveFutureContracts();
        List<FutureContract> list=new ArrayList<>();
        list.addAll(futureContracts);
        for(FutureContract futureContract:list){
            map.put(futureContract.getTfId(),futureContract);
        }
        return map;
    }
}
