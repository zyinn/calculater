package com.sumscope.optimus.calculator.tfcalculator.commons.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 决定计算目标的枚举
 */
public enum CalculationTarget {


	/**
	 * 计算目标 = IRR/基差
	 * 
	 */
	 IRR_BASE("IRR_BASE","Irr"),

	/**
	 * 计算目标 = 债卷价格
	 */
	BOND_PRICE("BOND_PRICE","UnderlyingFairQuote"),

	/**
	 * 计算目标 = 期货理论价格
	 * 
	 */
	 FUTURE_THEORETICAL_PRICE("FUTURE_THEORETICAL_PRICE","MktQuote"),

	/**
	 * 计算目标 = 期货场景分析
	 * 
	 */
     FUTURE_ANALYSIS("FUTURE_ANALYSIS","FairQuote");


	private String displayName;
	private String CalculationTargetName;

	private static final Map<String,CalculationTarget> calculationTargetMap = new HashMap<>();

	static{
		for(CalculationTarget value : values()){
			calculationTargetMap.put(value.CalculationTargetName,value);
		}
	}

	public static CalculationTarget fromDisplayName(String displayName){
		return calculationTargetMap.get(displayName);
	}

	CalculationTarget(String future_analysis, String name) {
		this.displayName = name;
		this.CalculationTargetName =future_analysis;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCalculationTargetName() {
		return CalculationTargetName;
	}

	public void setCalculationTargetName(String calculationTargetName) {
		this.CalculationTargetName = calculationTargetName;
	}
}
