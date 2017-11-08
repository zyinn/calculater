package com.sumscope.optimus.calculator.shared.enums;

/**
 * 收益率类型枚举，表明债卷收益率类型
 */
public enum YieldTypeEnum {

	/**
	 * ofr收益率
	 */
	ofr("ofr","Ofr"),

	/**
	 * bid收益率
	 */
	bid("bid","Bid"),

	/**
	 * 成交收益率
	 */
	deal("deal","成交"),

	/**
	 * 中债收益率
	 */
	cdc("cdc","中债");

	private  String key;

	private String name;

	public String getName() {
		return null;
	}

	public String getKey() {
		return null;
	}

	private YieldTypeEnum(String key, String name) {

	}

}
