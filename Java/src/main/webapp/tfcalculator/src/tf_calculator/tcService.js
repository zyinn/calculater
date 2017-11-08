// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['tc.mainModule'], function (mainModule) {
    "use strict";
    
    var dataDefine = function () {
        return {
            dto: {
                changeFuture: {
                    futureInitRequestDto: {
                        targetBonds: "selectedBondType.value",
                        futureContract: {
                            tfId: "selectedFuture.tfId",
                            tradingDate: "selectedFuture.startDate",
                            deliveryDate: "selectedFuture.maturityDate"
                        },
                        capitalCost: "capitalCost",
                        bondPrice: {
                            'yield': "selectedBond.yield",
                            netPrice: "selectedBond.netPrice",
                            fullPrice: "selectedBond.fullPrice",
                            yieldType: "selectedYieldType.value",
                            lastBondUpdateDate: "selectedBond.lastBondUpdateDate"
                        },
                        bondInfo: {
                            bondKey: "selectedBond.bondKey",
                            listedMarket: "selectedBond.listedMarket",
                            // convertionFactor: "selectedBond.convertionFactor",
                            
                            fixedCoupon: "selectedBond.fixedCoupon",
                            paymentFrequency: "selectedBond.paymentFrequency.value", 
                            interestStartDate: "selectedBond.interestStartDate", 
                            maturityDate: "selectedBond.maturityDate"
                        },
                        calculationMainRequest: {
                            calculationType: "selectedCalTgtType.value"
                        }
                    }
                },
                
                changeFuture_bondPrice: {
                    BPMainRequestDto: {
                        futurePrice: "selectedFuture.futurePrice",
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                },
                
                changeFuture_futureAnalysis: {
                    FSAMainRequestDto: {
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                },
                
                doCalCulate: {
                    calculationRequestDto: {
                        futureContract: {
                            tfId: "selectedFuture.tfId",
                            tradingDate: "selectedFuture.startDate",
                            deliveryDate: "selectedFuture.maturityDate"
                        },
                        capitalCost: "capitalCost",
                        bondPrice: {
                            'yield': "selectedBond.yield",
                            netPrice: "selectedBond.netPrice",
                            fullPrice: "selectedBond.fullPrice",
                            yieldType: "selectedYieldType.value",
                            lastBondUpdateDate: "selectedBond.lastBondUpdateDate"
                        },
                        bondPriceType: "selectedBond.bondPriceType",
                        bondInfo: {
                            bondKey: "selectedBond.bondKey",
                            listedMarket: "selectedBond.listedMarket",
                            
                            fixedCoupon: "selectedBond.fixedCoupon",
                            paymentFrequency: "selectedBond.paymentFrequency.value", 
                            interestStartDate: "selectedBond.interestStartDate", 
                            maturityDate: "selectedBond.maturityDate"
                        },
                        calculationMainRequest: {
                            calculationType: "selectedCalTgtType.value"
                        }
                    },
                    IRRMainRequestDto: {
                        futurePrice: "selectedFuture.futurePrice"
                    }
                },
                
                doCalCulate_bondPrice: {
                    BPMainRequestDto: {
                        futurePrice: "selectedFuture.futurePrice",
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                },
                
                doCalCulate_futureAnalysis: {
                    FSAMainRequestDto: {
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                },
                
                cal: {
                    "calculationRequestDto": {
                       
                    }, "capitalCost": 2.5, 
                    "bondInfo": {
                        "listedMarket": "CIB", 
                        "fixedCoupon": 2.55, 
                        
                        "interestStartDate": "2016-04-28", 
                        "maturityDate": "2019-04-28", 
                        "paymentFrequency": "A"
                    }, 
                    "calculationMainRequest": { "calculationType": "IRR_BASE" },
                    
                    
                    "IRRMainRequestDto": { "futurePrice": 100 }
                    
                },
                
                
                
                bondInfo_scheduledBonds: {
                    bondCode: "selectedBond.id",
                    
                    fixedCoupon: "selectedBond.fixedCoupon",
                    paymentFrequency: "selectedBond.paymentFrequency.value", 
                    interestStartDate: "selectedBond.interestStartDate", 
                    maturityDate: "selectedBond.maturityDate"
                },
                
                bondInfo_virtualBond: {
                    listedMarket: "selectedBond.listedMarket",
                    // convertionFactor: "selectedBond.convertionFactor",
                    
                    fixedCoupon: "selectedBond.fixedCoupon",
                    paymentFrequency: "selectedBond.paymentFrequency.value", 
                    interestStartDate: "selectedBond.interestStartDate", 
                    maturityDate: "selectedBond.maturityDate"
                }

            }
        };
    }();
    
    mainModule.service('tcService', [
        '$location', '$filter', 'servicePathConst', 'httpService', 'commonService',
        function ($location, $filter, servicePathConst, httpService, commonService) {
            
            var filetimeToDatetimeFilter = $filter("filetimeToDatetime");
            
            // Update viewmodel from data
            this.updateViewModel = function (data, vm) {
                if (!data || !vm) return;
                
                for (var prop in data) {
                    if (!data.hasOwnProperty(prop)) continue;
                    
                    vm[prop] = data[prop];
                }
            };
            
            // 期货改变
            this.changeFuture = function (scope, succeedCallback, failedCallback) {
                
                if (!scope.vm || !scope.vm.selectedFuture) {
                    if (failedCallback) failedCallback();
                    return;
                }
                
                if (!scope.vm.selectedBond) scope.vm.selectedBond = angular.copy(scope.originVM.selectedBond);
                
                if (scope.vm.calculationResult && scope.vm.calculationResult.exception) {
                    delete scope.vm.calculationResult["exception"];
                    scope.vm.calculationResult = angular.copy(scope.originVM.calculationResult);
                }
                
                if (!scope.vm.calculationResult.calculationMainResult) scope.vm.calculationResult.calculationMainResult = {};
                
                if (!scope.vm.calculationResult.calculationMainResult.irr) scope.vm.calculationResult.calculationMainResult.irr = 1;
                
                var dto = commonService.getDto(scope.vm, dataDefine.dto.changeFuture, scope.DATE_FORMAT);
                // var dto = scope.vm.selectedFuture.tfId;
                
                if (scope.vm && scope.vm.selectedCalTgtType) {
                    if (scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[1].value) {
                        angular.merge(dto, commonService.getDto(scope.vm, dataDefine.dto.changeFuture_bondPrice, scope.DATE_FORMAT));
                    } else if (scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[2].value ||
                                scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[3].value) {
                        angular.merge(dto, commonService.getDto(scope.vm, dataDefine.dto.changeFuture_futureAnalysis, scope.DATE_FORMAT));
                        delete dto.IRRMainRequestDto;
                    }
                }
                
                httpService.postService(servicePathConst.tc_post_future_init, dto, function (data) {
                    if (!data) {
                        commonService.commonErrorDialog(scope, undefined, "没有获取到所选债券数据");
                        if (failedCallback) failedCallback();
                        return;
                    }
                    
                    if (!scope.vm.calculationResult) scope.vm.calculationResult = {};
                    
                    angular.merge(scope.vm.calculationResult, data);
                    
                    scope.vm.calculationResult.deliverableBonds = undefined;

                    var yieldType = commonService.getPropertyX(data, "defaultBondPrice.yieldType");
                    if (yieldType) {
                        scope.vm.selectedYieldType = scope.yieldTypeButtons.findItem(function (e) {
                            return e.value === yieldType;
                        });
                    }
                    
                    // 付息频率 dto -> vm
                    // 转换因子 dto -> vm
                    function processBond(item, index) {
                        var result = undefined;
                        
                        if (item.paymentFrequency) {
                            result = scope.paymentFrequencys.findItem(function (e) { return e.value === item.paymentFrequency; });
                            if (result) item.paymentFrequency = result;
                        }
                        
                        if (item.couponType) {
                            result = scope.paymentFrequencys.findItem(function (e) { return e.value === item.couponType; });
                            if (result) item.paymentFrequency = result;
                        }
                        
                        if (scope.vm.calculationResult.bondConvertionFactors && scope.vm.calculationResult.bondConvertionFactors.length > 0 && scope.vm.calculationResult.bondConvertionFactors instanceof Array) {
                            result = scope.vm.calculationResult.bondConvertionFactors.findItem(function (e) { return e.bondKey === item.bondKey });
                            if (result) item.convertionFactor = result.convertionFactor;
                            
                            result = scope.vm.calculationResult.bondConvertionFactors.findItem(function (e) { return e.bondKey === item.id });
                            if (result) item.convertionFactor = result.convertionFactor;
                        }
                        
                        if (item.couponRate || item.couponRate === 0) {
                            item.fixedCoupon = item.couponRate;
                        }
                        
                        if (item.tradeDate) item.interestStartDate = filetimeToDatetimeFilter(item.tradeDate);
                        if (item.settlementDate) item.maturityDate = filetimeToDatetimeFilter(item.settlementDate);
                        
                        if (item.issueStartDate) item.issueStartDate = filetimeToDatetimeFilter(item.issueStartDate);
                    };
                    
                    function loadBondList(list) {
                        if (list && list.length > 0 && list instanceof Array) {
                            list.forEach(processBond);
                            
                            scope.vm.calculationResult.deliverableBonds = list;
                            scope.vm.selectedBond = scope.vm.calculationResult.deliverableBonds[0];
                            
                            angular.merge(scope.vm.selectedBond, scope.vm.calculationResult.defaultBondPrice);
                            
                            angular.merge(scope.vm.selectedBond, data.calculationMainResult);
                        }
                    };
                    
                    // 可交割券
                    loadBondList(scope.vm.calculationResult.selectableBonds);
                    
                    // 未发国债
                    loadBondList(scope.vm.calculationResult.scheduledBondInfos);
                    
                    angular.merge(scope.vm.selectedFuture, scope.vm.calculationResult.futruePrice);
                    
                    if (data.exception) {
                        commonService.commonErrorDialog(scope, undefined, "收益率为空，请数量输入收益率再参与计算");
                    } else {
                        scope.originVM = angular.copy(scope.vm);
                    }
                    
                    if (!data.defaultBondPrice && (!scope.vm.selectedCalTgtType || scope.vm.selectedCalTgtType.value !== "BOND_PRICE"))
                        commonService.commonErrorDialog(scope, undefined, "数据库错误。 ofr、bid、成交、中债估值都为空。请手动输入债券收益率。");
                    
                    if (succeedCallback) succeedCallback();

                }, function (data) {
                    
                    if (data && data.return_message && data.return_message.exceptionCode === "E1001") {
                        
                        data.return_message.exceptionCode = "提示";
                        data.return_message.exceptionName = "";
                        data.return_message.exceptionMessage = "";
                        
                        commonService.commonErrorDialog(scope, data, "该合约无对应期限的 “未发国债” ， 请切换到 “可交割券”。");
                    } else {
                        commonService.commonErrorDialog(scope, data, "获取所选债券数据失败");
                    }
                    
                    if (failedCallback) failedCallback();
                });
            };
            
            // 一般计算交互
            this.doCalCulate = function (scope, succeedCallback, failedCallback) {
                
                if (!scope.vm) return;
                
                if (!scope.vm.calculationResult) scope.vm.calculationResult = angular.copy(scope.originVM.calculationResult);
                
                if (!scope.vm.selectedBond) {
                    commonService.commonErrorDialog(scope, undefined, "债券信息不能为空");
                    if (failedCallback) failedCallback();
                    return;
                }
                
                var dto = commonService.getDto(scope.vm, dataDefine.dto.doCalCulate, scope.DATE_FORMAT);
                
                if (scope.vm.selectedCalTgtType) {
                    if (scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[1].value) {
                        angular.merge(dto, commonService.getDto(scope.vm, dataDefine.dto.doCalCulate_bondPrice, scope.DATE_FORMAT));
                    } else if (scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[2].value ||
                                scope.vm.selectedCalTgtType.value === scope.calTgtTypeButtons[3].value) {
                        angular.merge(dto, commonService.getDto(scope.vm, dataDefine.dto.doCalCulate_futureAnalysis, scope.DATE_FORMAT));
                        delete dto.IRRMainRequestDto;
                    }
                }
                
                if (scope.vm.selectedBondType) {
                    if (scope.vm.selectedBondType.value === scope.bondTypeButtons[1].value) {
                        dto.calculationRequestDto.bondInfo = commonService.getDto(scope.vm, dataDefine.dto.bondInfo_scheduledBonds, scope.DATE_FORMAT);
                    } else if (scope.vm.selectedBondType.value === scope.bondTypeButtons[3].value) {
                        dto.calculationRequestDto.bondInfo = commonService.getDto(scope.vm, dataDefine.dto.bondInfo_virtualBond, scope.DATE_FORMAT);
                    }
                }
                
                httpService.postService(servicePathConst.tc_post_do_calculation, dto, function (data) {
                    if (!data) {
                        if (failedCallback) failedCallback();
                        
                        commonService.commonErrorDialog(scope, undefined, "计算失败");
                        return;
                    }
                    
                    angular.merge(scope.vm.calculationResult, data);
                    
                    angular.merge(scope.vm.selectedBond, data.bondPrice);
                    if (data.calculationMainResult) angular.merge(scope.vm.selectedBond, data.calculationMainResult);
                    
                    if (data.bondConvertionFactor) scope.vm.selectedBond.convertionFactor = data.bondConvertionFactor;
                    
                    var futurePrice = commonService.getPropertyX(data, "calculationMainResult.futurePrice");
                    if (futurePrice) commonService.setPropertyX(scope, "vm.selectedFuture.futurePrice", futurePrice);
                    
                    if (succeedCallback) succeedCallback();

                }, function (data) {
                    commonService.commonErrorDialog(scope, data, "计算失败");
                    
                    if (failedCallback) failedCallback();
                });
            };
        }
    ]);

});

