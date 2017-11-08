package com.sumscope.optimus.calculator.tfcalculator.commons.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 债卷窗口枚举类型，表明用户当前所选择的债卷窗口
 * 
 */
public enum TargetBonds {
	/**
	 * 获取期货的
	 */
	DELIVABLE_BONDS("DELIVABLE_BONDS"),

	/**
	 * 未发国债
	 */
	SCHEDULED_BONDS("SCHEDULED_BONDS"),
	/**
	 * 自选卷
	 */
	SELECTED_BOND("SELECTED_BOND"),
	/**
	 * 虚拟卷
	 */
	VIRTUAL_BOND("VIRTUAL_BOND");

	private String displayName;

	private static final Map<String,TargetBonds> targetBondsMap = new HashMap<>();

	static{
		for(TargetBonds value : values()){
			targetBondsMap.put(value.displayName,value);
		}
	}

	public static TargetBonds fromDisplayName(String displayName){
		return targetBondsMap.get(displayName);
	}


	TargetBonds(String name) {
		this.displayName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


}
