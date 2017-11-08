package com.sumscope.optimus.calculator.shared.model.dbmodel;

import java.math.BigDecimal;

/**
 * 记录可交割卷主键及转换因子信息
 */
public class DeliverableBondKeyInfo {

	/**
	 * 债卷bondKey
	 * 
	 */
	private String bondKey;

	/**
	 * 可交割卷的转换因子
	 * 
	 */
	private BigDecimal convertionFactor;

	public String getBondKey() {
		return bondKey;
	}

	public void setBondKey(String bondKey) {
		this.bondKey = bondKey;
	}

	public BigDecimal getConvertionFactor() {
		return convertionFactor;
	}

	public void setConvertionFactor(BigDecimal convertionFactor) {
		this.convertionFactor = convertionFactor;
	}
}
