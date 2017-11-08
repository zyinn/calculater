package com.sumscope.optimus.calculator.shared.model.dto;

/**
 * 债卷信息
 */
public class FutureContractDto {

	private String tfKey;

	private String tfId;

	/**
	 * 开始日期
	 */
	private String startDate;

	/**
	 * 到期日期
	 */
	private String maturityDate;

	public String getTfKey() {
		return tfKey;
	}

	public void setTfKey(String tfKey) {
		this.tfKey = tfKey;
	}

	public String getTfId() {
		return tfId;
	}

	public void setTfId(String tfId) {
		this.tfId = tfId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
}
