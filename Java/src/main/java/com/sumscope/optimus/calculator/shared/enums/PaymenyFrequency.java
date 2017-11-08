package com.sumscope.optimus.calculator.shared.enums;

/**
 *  Created by fan.bai on 2016/5/4. 
 *  付息频率枚举，bond.Coupon_Frequency 
 * 
 */
public enum PaymenyFrequency {

	N("",""),

	S("半年","SemiAnnual"),

	A("一年","Annual"),

	Q("一季度","Quarterly"),

	M("一个月","Monthly");

	private String explain;

	private String calculatorEnumName;

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getCalculatorEnumName() {
		return calculatorEnumName;
	}

	public void setCalculatorEnumName(String calculatorEnumName) {
		this.calculatorEnumName = calculatorEnumName;
	}

	PaymenyFrequency(String explain, String calculatorEnumName) {
		this.explain = explain;
		this.calculatorEnumName=calculatorEnumName;
	}
}
