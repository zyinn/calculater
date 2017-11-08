package com.sumscope.optimus.calculator.planalysis.model.dto;

/**
 * Created by simon.mao on 2016/4/27.
 */
public class FutureContractDto {
    private String tfKey;
    private String tfId;
    private String startDate;
    private String maturityDate;
    private String deliveryDate;

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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
