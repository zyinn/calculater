package com.sumscope.optimus.calculator.shared.facade.converter;

import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.commons.log.LogManager;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

/**
 * 债卷信息转换器
 */
public class BondInfoConverter {

	/**
	 * 转换BondInfo至BondInfoDto
	 * 
	 */
	public static BondInfoDto convertBondInfoDto(BondInfo bondInfo) {
		BondInfoDto bondInfoDto = new BondInfoDto();
		try{
			BeanUtils.copyProperties(bondInfo,bondInfoDto);
			bondInfoDto.setSettlementDate(new SimpleDateFormat("yyyy-MM-dd").parse(bondInfo.getMaturityDate()));
			bondInfoDto.setTradeDate(new SimpleDateFormat("yyyy-MM-dd").parse(bondInfo.getInterestStartDate()));
		}catch (Exception e){
			LogManager.error(e.toString());
		}
		return bondInfoDto;
	}

	private BondInfoConverter() {
	}
}
