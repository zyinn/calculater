package com.sumscope.optimus.calculator.shared.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 期货价格Dto
 */
public class FuturePriceDto {

	/**
	 * 期货价格
	 * 
	 */
	private BigDecimal futurePrice;

	/**
	 * 最后更新日期-金融计算器项目触发的更新时间，非实际价格变动时间
	 */
	private Date lastUpdateDatetime;

	public BigDecimal getFuturePrice() {
		return futurePrice;
	}

	public void setFuturePrice(BigDecimal futurePrice) {
		this.futurePrice = futurePrice;
	}

	public Date getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Date lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}
}
