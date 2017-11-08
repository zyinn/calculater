package com.sumscope.optimus.calculator.shared.model.dbmodel;

import java.util.Date;

/**
 * 期货数据
 */
public class FutureContract {

	/**
	 * tfKey
	 * 
	 */
	private String tfKey;

	/**
	 * tfId
	 * 
	 */
	private String tfId;

	/**
	 * 交割日
	 * 
	 */
	private Date deliveryDate;

	/**
	 * 期限
	 */
	private int bondTerm;

	/**
	 * 交易日
	 */
	private Date tradingDate;

	public String getTfKey() {
		return tfKey;
	}

	public void setTfKey(String tfKey) {
		this.tfKey = tfKey;
	}

	public String getTfId() {
		return tfId;
	}

	public void setTfId(String tfId) {
		this.tfId = tfId;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public int getBondTerm() {
		return bondTerm;
	}

	public void setBondTerm(int bondTerm) {
		this.bondTerm = bondTerm;
	}

	public Date getTradingDate() {
		return tradingDate;
	}

	public void setTradingDate(Date tradingDate) {
		this.tradingDate = tradingDate;
	}
}
