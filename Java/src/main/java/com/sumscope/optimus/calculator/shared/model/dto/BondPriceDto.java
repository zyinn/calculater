package com.sumscope.optimus.calculator.shared.model.dto;

import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债卷价格数据
 */
public class BondPriceDto {

	/**
	 * 债卷收益率
	 */
	private BigDecimal yield;

	/**
	 * 净价
	 */
	private BigDecimal netPrice;

	/**
	 * 全价
	 */
	private BigDecimal fullPrice;

	/**
	 * 收益率类型
	 */
	private YieldTypeEnum yieldType;

	/**
	 * 最后更新日期
	 */
	private Date lastBondUpdateDate;

	public BigDecimal getYield() {
		return yield;
	}

	public void setYield(BigDecimal yield) {
		this.yield = yield;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public BigDecimal getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(BigDecimal fullPrice) {
		this.fullPrice = fullPrice;
	}

	public YieldTypeEnum getYieldType() {
		return yieldType;
	}

	public void setYieldType(YieldTypeEnum yieldType) {
		this.yieldType = yieldType;
	}

	public Date getLastBondUpdateDate() {
		return lastBondUpdateDate;
	}

	public void setLastBondUpdateDate(Date lastBondUpdateDate) {
		this.lastBondUpdateDate = lastBondUpdateDate;
	}

}
