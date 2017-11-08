package com.sumscope.optimus.calculator.shared.model.dto;

import java.math.BigDecimal;

/**
 * 记录债卷主键及转换因子数据
 */
public class BondConvertionFactorDto {

	/**
	 * 债卷key，与listedMarket共同组成业务主键决定一个债卷
	 * 
	 */
	private String bondKey;

	/**
	 * 转换因子
	 * 对于某期货可交割卷，转换因子从数据库中读出返回前端
	 * 其他债卷，转换因子根据计算得出返回前端
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
