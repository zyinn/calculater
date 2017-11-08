package com.sumscope.optimus.calculator.shared.model.dto;

import java.util.Date;
import java.math.BigDecimal;
import com.sumscope.optimus.calculator.shared.enums.PaymenyFrequency;

/**
 * 债卷信息数据
 */
public class BondInfoDto {

	/**
	 * 债卷key，与listedMarket共同组成业务主键决定一个债卷
	 */
	private String bondKey;

	/**
	 * 债卷发行市场，与bondKey共同组成业务主键决定一个债卷
	 */
	private String listedMarket;

	/**
	 * 债卷code
	 * 
	 */
	private String bondCode;

	/**
	 * 简称
	 */
	private String shortName;

	/**
	 * 交易日
	 */
	private Date tradeDate;

	/**
	 * 结算日
	 */
	private Date settlementDate;

	/**
	 * 固定利息，发行收益率
	 */
	private BigDecimal fixedCoupon;

	/**
	 * 付息频率
	 */
	private PaymenyFrequency paymentFrequency;

	/**
	 *起息日
	 */
	private String interestStartDate;

	/**
	 * 到期日
	 */
	private String maturityDate;

	public String getBondKey() {
		return bondKey;
	}

	public void setBondKey(String bondKey) {
		this.bondKey = bondKey;
	}

	public String getListedMarket() {
		return listedMarket;
	}

	public void setListedMarket(String listedMarket) {
		this.listedMarket = listedMarket;
	}

	public String getBondCode() {
		return bondCode;
	}

	public void setBondCode(String bondCode) {
		this.bondCode = bondCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public BigDecimal getFixedCoupon() {
		return fixedCoupon;
	}

	public void setFixedCoupon(BigDecimal fixedCoupon) {
		this.fixedCoupon = fixedCoupon;
	}

	public PaymenyFrequency getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(PaymenyFrequency paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public String getInterestStartDate() {
		return interestStartDate;
	}

	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
}
