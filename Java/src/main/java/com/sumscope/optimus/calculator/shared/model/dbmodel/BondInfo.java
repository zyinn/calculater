package com.sumscope.optimus.calculator.shared.model.dbmodel;

import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;
import java.math.BigDecimal;
import com.sumscope.optimus.calculator.shared.enums.PaymenyFrequency;
import com.sumscope.optimus.calculator.shared.enums.DayCount;

/**
 *  用于记录债卷的信息，数据从bond表得到。 
 * 
 */
public class BondInfo extends BondPrimaryKeyDto implements Comparable{

	/**
	 *  起息日
	 * 
	 */
	private String interestStartDate;

	/**
	 *  到期日期 
	 * 
	 */
	private String maturityDate;

	/**
	 *  固息利率，或者叫发行收益率
	 *  数据库字段 Issue_Rate 
	 * 
	 */
	private BigDecimal fixedCoupon;

	/**
	 *  付息频率 
	 *  数据库字段 Coupon_Frequency 
	 * 
	 */
	private PaymenyFrequency paymentFrequency;

	/**
	 *  日期， 例如"Act365" 
	 *  数据库字段 Interest_Basis 
	 * 
	 */
	private DayCount dayCount;

	public String getInterestStartDate() {
		return interestStartDate;
	}

	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public BigDecimal getFixedCoupon() {
		return fixedCoupon;
	}

	public void setFixedCoupon(BigDecimal fixedCoupon) {
		this.fixedCoupon = fixedCoupon;
	}

	public PaymenyFrequency getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(PaymenyFrequency paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public DayCount getDayCount() {
		return dayCount;
	}

	public void setDayCount(DayCount dayCount) {
		this.dayCount = dayCount;
	}

	@Override
	public int compareTo(Object o) {
		BondInfo bondInfo = (BondInfo) o;
		String bondCode = bondInfo.getBondCode();
		return this.getBondCode().compareTo(bondCode);
	}
}
