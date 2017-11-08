package com.sumscope.optimus.calculator.shared.enums;

/**
 *  Created by fan.bai on 2016/5/4. 
 *  计算日期枚举，使用者：bond.Interest_Basis 
 * 
 */
public enum DayCount {

	BA0("360日","Act360"),

	BAA("当前年","ActAct"),

	BA5("365日","Act365"),

	B30("360日","B30360");

	private String expalin;

	private String calculatorEnumName;

	public String getExpalin() {
		return null;
	}

	public String getCalculatorEnumName() {
		return null;
	}

	private DayCount(String explain, String name) {

	}

}
