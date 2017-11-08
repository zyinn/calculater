package com.sumscope.optimus.calculator.shared.service;

import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 金融计算服务用于获取本地数据的服务接口。该接口提供一系列用于在数据库中获取数据的方法。
 */
public interface CalculationDataService {

	/**
	 * 根据bondKey获取债卷信息
	 */
	 BondInfo getBondInfoByKeys(String bondKey);

	/**
	 * 获取某期货的可交割卷
	 * 
	 */
	 List<BondInfo> getDeliverableBonds(String tfKey);


	/**
	 * 获取债卷最新Ofr价
	 *
	 * @param bondKey 债卷key，债卷业务主键之一
	 * @param listedMarket 债卷发行市场，债卷业务主键之一
	 * @param onlyToday 只取今日价格
	 * @return 债卷最新Ofr价， null代表无法取得
	 */
	 BigDecimal getBondOfrLatestPrice(String bondKey, String listedMarket, boolean onlyToday);

	/**
	 * 获取债卷最新Bid价
	 *
	 * @param bondKey 债卷key，债卷业务主键之一
	 * @param listedMarket 债卷发行市场，债卷业务主键之一
	 * @param onlyToday 只取今日价格
	 * @return 债卷最新Bid价， null代表无法取得
	 */
	 BigDecimal getBondBidLatestPrice(String bondKey, String listedMarket, boolean onlyToday);

	/**
	 * 获取债卷最新中债价
	 * @param bondKey 债卷key，债卷业务主键之一
	 * @param listedMarket 债卷发行市场，债卷业务主键之一
	 * @return 债卷最新中债价， null代表无法取得
	 */
	 BigDecimal getBondsCdcLatestPrice(String bondKey, String listedMarket);

	/**
	 * 获取债卷最新成交价
	 * @param bondKey 债卷key，债卷业务主键之一
	 * @param listedMarket 债卷发行市场，债卷业务主键之一
	 * @return 债卷最新成交价， null代表无法取得
	 */
	 BigDecimal getBondDealLatestPrice(String bondKey, String listedMarket);

	/**
	 * 根据前缀获取所有固息债列表
	 */
	 List<BondPrimaryKeyDto> findBondsByNamePrefix(String prifix);

	/**
	 * 获取某一期限的最新发行债卷
	 */
	 BondInfo getLatestBondByTerm(int term);

	/**
	 * 获取当前所有期货列表
	 */
	 List<FutureContract> retrieveFutureContracts();

	/**
	 * 根据输入的期限获取对应的未发国债信息
	 * 
	 */
	 List<ScheduledBondInfo> getScheduledBonds(int term);

	/**
	 * 根据未发国债id来获取未发国债信息.
	 * @param id
	 * @return
     */
 	 ScheduledBondInfo getScheduledBondById(String id);

	/**
	 * 获取当前选中的期货和转换因子和对应的债券
	 * @param tfKey
	 * @param
     * @return
     */
	List<BondConvertionFactorDto> getDeliverableBondConvertionFactor(String tfKey);

	/**
	 * 收益率列表KeyValuePair
	 * @param deliverableBonds
	 * @return
     */
	List<KeyValuePair> getYieldByDeliverableBonds(List<BondInfo> deliverableBonds,List<ScheduledBondInfo> scheduledBondInfos,CalculationRequestForInit calculationRequestForInit);

	/**
	 *
	 * @return本日对应的下个工作日，对于节假日有效，返回节假日后的第一个工作日。若本日是工作日，返回的是本日。
     */
	Date getTodaysNextBusinessDate(String date);

	/**
	 *获取上一个工作日
     */
	Date getLastWorkDate(String date);
	/**
	 * 获取期货合约map key:tfID
	 * @return
     */
	Map<String,FutureContract> retrieveFutureContractByTfId();

}
