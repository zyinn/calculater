package com.sumscope.optimus.calculator.shared.enums;

/**
 * 当用户修改了债卷的价格信息时，该枚举表明用户修改的是哪个价格。
 */
public enum BondPriceType {
	/**
	 * 收益率
	 */
	YIELD("YIELD"),

	/**
	 * 净价
	 */
	NET_PRICE("NET_PRICE"),
	/**
	 * 全价
	 */
	FULL_PRICE("FULL_PRICE");

	private String displayName;

	BondPriceType(String name) {
		this.displayName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
