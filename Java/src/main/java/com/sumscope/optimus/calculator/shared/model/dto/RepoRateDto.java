package com.sumscope.optimus.calculator.shared.model.dto;

import java.math.BigDecimal;

/**
 *  资金成本Dto 
 * 
 */
public class RepoRateDto {

	/**
	 *  资金成本code 
	 * 
	 */
	private String code;

	/**
	 *  当前资金成本price 
	 * 
	 */
	private BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
