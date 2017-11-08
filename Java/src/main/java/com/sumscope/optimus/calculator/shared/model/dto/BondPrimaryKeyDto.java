package com.sumscope.optimus.calculator.shared.model.dto;

import com.sumscope.optimus.calculator.shared.enums.PaymenyFrequency;

import java.math.BigDecimal;

/**
 *  用于记录债卷业务主键信息，bondKey与listedMarket是一个债卷的业务主键，bondCode则是一个债卷的显示信息 
 *  该结构用于提供根据用户输入显示可选固息债的功能， 可从mv_bond_enhanced表可读取 
 * 
 */
public class BondPrimaryKeyDto {

	/**
	 *  债卷code，例如: 150016.IB 
	 * 
	 */
	private String bondCode;

	/**
	 *  债卷key，与listedMarket共同构成业务主键.例如 Z0003002015GOVBGB02 
	 * 
	 */
	private String bondKey;

	/**
	 *  债卷交易所，与bondKey共同构成业务主键，例如：CIB 
	 * 
	 */
	private String listedMarket;

	/**
	 * 简称
	 */
	private String shortName;

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

	public String getBondCode() {
		return bondCode;
	}

	public void setBondCode(String bondCode) {
		this.bondCode = bondCode;
	}

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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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
