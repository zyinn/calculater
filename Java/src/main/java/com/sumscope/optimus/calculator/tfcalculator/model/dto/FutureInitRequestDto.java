package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.TargetBonds;

/**
 * 当期货改变是使用该请求Dto
 */
public class FutureInitRequestDto extends AbstractTFCalculatorRequestDto {

    /**
     * 当前债卷窗口枚举类型
     */
    private TargetBonds targetBonds;

    /**
     * 债卷价格，本类中仅取当前页面的债卷收益率
     */
    private BondPriceDto bondPrice;

    /**
     * 债卷信息Dto
     * 当前债卷信息仅在虚拟卷时设置全量，其他窗口仅设置bondKey和listedMarket
     */
    private BondInfoDto bondInfo;

    public TargetBonds getTargetBonds() {
        return targetBonds;
    }

    public void setTargetBonds(TargetBonds targetBonds) {
        this.targetBonds = targetBonds;
    }

    public BondPriceDto getBondPrice() {
        return bondPrice;
    }

    public void setBondPrice(BondPriceDto bondPrice) {
        this.bondPrice = bondPrice;
    }

    public BondInfoDto getBondInfo() {
        return bondInfo;
    }

    public void setBondInfo(BondInfoDto bondInfo) {
        this.bondInfo = bondInfo;
    }
}
