package com.sumscope.optimus.calculator.shared.dao;

import com.sumscope.optimus.calculator.shared.model.dbmodel.*;
import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 金融计算器用于读取数据的Dao类
 */
public interface CalculationDao {

	/**
	 * 获取某期货的可交割卷
	 * 
	 */
	 List<DeliverableBondKeyInfo> getDeliverableBondKeyWithCF(String tfKey);

	/**
	 * 根据BondKey 获取债卷信息
	 */
	 List<BondInfo> getBondInfoByKeys(List<String> bondKeyInfos);

	/**
	 * 获取债卷最新Ofr价
	 */
	 BigDecimal getBondOfrLatestPrice(String bondKey, String listedMarket, boolean onlyToday);

	/**
	 * 获取债卷最新Bid价
	 */
	 BigDecimal getBondBidLatestPrice(String bondKey, String listedMarket, boolean onlyToday);

	/**
	 * 获取某债券的中债 最新收益率
	 * @param bondKey 债券bondKey
	 * @param listedMarket 债券listedMarket
	 * @return 对应的最新收益率
	 */
	BigDecimal getBondsCdcLatestPrice(String bondKey, String listedMarket);

	/**
	 * 获取某债券的中债 最新收益率
	 * @param bondKey 债券bondKey
	 * @param listedMarket 债券listedMarket
	 * @return 对应的最新收益率
	 */
	BondCDCValuationInfo getBondsCdcLatestInfo(String bondKey, String listedMarket);

	/**
	 * 获取债卷最新成交价
	 */
	 BigDecimal getBondDealLatestPrice(String bondKey, String listedMarket,boolean onlyToday);

	/**
	 * 根据输入前缀获取固息债卷列表
	 * @param prifix 输入前缀
	 * @return 符合输入前缀的固定利息债卷列表
	 */
	 List<BondPrimaryKeyDto> findFixedCouponBondsByNamePrefix(String prifix);

	/**
	 * 获取某一期限的最后发行债卷
	 */
	 BondInfo getLatestBondByTerm(int term);

	/**
	 * 获取当前所有期货列表
	 */
	 List<FutureContract> retrieveFutureContracts();

	/**
	 * 根据期限获取对应的未发国债主键信息
	 * 
	 */
	 List<ScheduledBondInfo> getScheduledBondKeysByTerm(int term);

	/**
	 * @return 本日对应的下个工作日，对于节假日有效，返回节假日后的第一个工作日。若本日是工作日，返回的是本日。
	 */
	Date getTodaysNextBusinessDate(String date);
	/**
	 * @return 本日对应的上一个工作日。
	 */
	Date getLastWorkDate(String date);

	/**
	 * 根据用户所选中的未发国债来查询债券的key值
	 * @param deadLine
	 * @return
     */
	String getBondKeyByTerm(Integer deadLine);


	ScheduledBondInfo getScheduledBondById(String id);
}
