/**
 * Create by WeiLai on 03/14/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'tc.mainModule', 'spin'
], function (mainModule, Spinner) {
    'use strict';
    
    // ReSharper disable InconsistentNaming
    var DATE_FORMAT = "yyyy-MM-dd";
    var DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // ReSharper restore InconsistentNaming
    
    var tcDataDefine = function () {
        return {
            bondTypeButtons: [
                { value: "DELIVABLE_BONDS", display: "可交割券" },
                { value: "SCHEDULED_BONDS", display: "未发国债" },
                { value: "SELECTED_BOND", display: "自选券" },
                { value: "VIRTUAL_BOND", display: "虚拟券" }
            ],
            calTgtTypeButtons: [
                { value: "IRR_BASE", display: "IRR/基差", viewUrl: "tf_calculator/views/tabView.html" },
                { value: "BOND_PRICE", display: "债券价格", viewUrl: "tf_calculator/views/tabView_bondPrice.html" },
                { value: "FUTURE_ANALYSIS", display: "期货情景分析", viewUrl: "tf_calculator/views/tabView_futureAnalysis.html" },
                { value: "FUTURE_THEORETICAL_PRICE", display: "期货理论价格", viewUrl: "tf_calculator/views/tabView_futureTheoreticalPrice.html" }
            ],
            yieldTypeButtons: [
                { value: "ofr", display: "Ofr" },
                { value: "bid", display: "Bid" },
                { value: "deal", display: "成交" },
                { value: "cdc", display: "中债" }
            ],
            paymentFrequencys: [
                //{ value: "N", display: "" },
                { value: "S", display: "半年度" },
                { value: "A", display: "年度" },
                // { value: "Q", display: "一季度" },
                // { value: "M", display: "一个月" }
            ],
            dayCounts: [
                { value: "BA0", display: "360日" },
                { value: "BAA", display: "当前年" },
                { value: "BA5", display: "365日" },
                { value: "B30", display: "360日" }
            ],
            bondPriceType: {
                "vm.selectedBond.yield": "YIELD",
                "vm.selectedBond.netPrice": "NET_PRICE",
                "vm.selectedBond.fullPrice": "FULL_PRICE"
            },
            vaildRule: {
                
                
                doCalculation_IRR_BASE: [
                    { prop: "vm.selectedBond.interestStartDate", displayName: "起息日" },
                    { prop: "vm.selectedBond.maturityDate", displayName: "到期日" },
                    { prop: "vm.selectedBond.paymentFrequency", displayName: "付息频率" },
                    { prop: "vm.selectedBond.fixedCoupon", displayName: "票息率" },
                    { prop: ["vm.selectedBond.yield", "vm.selectedBond.netPrice", "vm.selectedBond.fullPrice"], displayName: '"收益率" 或 "债券净价" 或 "债券全价"' }
                ],
                doCalculation_BOND_PRICE: [
                    { prop: "vm.selectedBond.interestStartDate", displayName: "起息日" },
                    { prop: "vm.selectedBond.maturityDate", displayName: "到期日" },
                    { prop: "vm.selectedBond.paymentFrequency", displayName: "付息频率" },
                    { prop: "vm.selectedBond.fixedCoupon", displayName: "票息率" }
                ],
                doCalculation_FUTURE_ANALYSIS: [
                    { prop: "vm.selectedBond.interestStartDate", displayName: "起息日" },
                    { prop: "vm.selectedBond.maturityDate", displayName: "到期日" },
                    { prop: "vm.selectedBond.paymentFrequency", displayName: "付息频率" },
                    { prop: "vm.selectedBond.fixedCoupon", displayName: "票息率" },
                    { prop: ["vm.selectedBond.yield", "vm.selectedBond.netPrice", "vm.selectedBond.fullPrice"], displayName: '"收益率" 或 "债券净价" 或 "债券全价"' }
                ],   
                
                doCalculation_common: [
                    { prop: "vm.selectedFuture.futurePrice", displayName: "期货价格", errMessage: "期货价格输入错误，小数位最多3位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,3})$/, max: 1000, min: 0 },
                    { prop: "vm.selectedFuture.yield", displayName: "收益率", errMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", pattern: /^(-?\d{0,2})$|^(-?\d{0,2}.\d{1,4})$/, max: 100, min: -100 },
                    { prop: "vm.selectedFuture.netPrice", displayName: "债券净价", errMessage: "债券净价输入错误，小数位最多4位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 1000, min: 0 },
                    { prop: "vm.selectedFuture.fullPrice", displayName: "债券全价", errMessage: "债券全价输入错误，小数位最多4位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 1000, min: 0 },
                    { prop: "vm.capitalCost", displayName: "资金成本", errMessage: "资金成本输入错误，小数位最多3位，最大值100%，最小值0%", pattern: /^(\d{0,3})$|^(\d{0,3}.\d{1,3})$/, max: 100, min: 0 },

                    { prop: "vm.selectedBond.fixedCoupon", displayName: "票息率", errMessage: "票息率输入错误，正确格式： 00.0000, 最大值100%，最小值0%", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 100, min: 0 },
                    { prop: "vm.selectedBond.yield", displayName: "收益率", errMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", pattern: /^(-?\d{0,2})$|^(-?\d{0,2}.\d{1,4})$/, max: 100, min: -100 },

                    { prop: "vm.selectedBond.irr", displayName: "IRR", errMessage: "IRR输入错误，小数位最多4位，最大值100%，最小值-100%", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { prop: "vm.selectedBond.basis", displayName: "基差", errMessage: "基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { prop: "vm.selectedBond.netBasis", displayName: "净基差", errMessage: "净基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },

                    { prop: "vm.calculationResult.calculationMainResult.irr", displayName: "IRR", errMessage: "IRR输入错误，小数位最多4位，最大值100%，最小值-100%", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { prop: "vm.calculationResult.calculationMainResult.basis", displayName: "基差", errMessage: "基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { prop: "vm.calculationResult.calculationMainResult.netBasis", displayName: "净基差", errMessage: "净基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 }
                ],
                
                ruleList: [
                    //{ indexList: [[0, 3], [1, 3], [2, 3]], prop: "vm.selectedBond.interestStartDate", rule: "required", displayName: "起息日" },
                    //{ indexList: [[0, 3], [1, 3], [2, 3]], prop: "vm.selectedBond.maturityDate", rule: "required", displayName: "到期日" },
                    //{ indexList: [[0, 3], [1, 3], [2, 3]], prop: "vm.selectedBond.paymentFrequency", rule: "required", displayName: "付息频率" },
                    //{ indexList: [[0, 3], [1, 3], [2, 3]], prop: "vm.selectedBond.fixedCoupon", rule: "required", displayName: "票息率" },

                    //{
                    //    indexList: [[0, 3], [2, 3]],
                    //    prop: [
                    //        "vm.selectedBond.yield", 
                    //        "vm.selectedBond.netPrice", 
                    //        "vm.selectedBond.fullPrice"
                    //    ], rule: "requiredAny", displayName: '"收益率" 或 "债券净价" 或 "债券全价"'
                    //},

                    { indexList: [[0, -1], [1, -1]], prop: "vm.selectedFuture.futurePrice", displayName: "期货价格", errMessage: "期货价格输入错误，小数位最多3位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,3})$/, max: 1000, min: 0 },
                    { indexList: [[-1, 1], [-1, 3], [3, -1]], prop: "vm.selectedBond.fixedCoupon", displayName: "票息率", errMessage: "票息率输入错误，正确格式： 00.0000, 最大值100%，最小值0%", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 100, min: 0 },

                    { indexList: [[0, -1], [1, -1], [2, 3], [3, -1]], prop: "vm.capitalCost", displayName: "资金成本", errMessage: "资金成本输入错误，小数位最多3位，最大值100%，最小值0%", pattern: /^(\d{0,3})$|^(\d{0,3}.\d{1,3})$/, max: 100, min: 0 },

                    { indexList: [[0, -1], [2, 3], [3, -1]], prop: "vm.selectedBond.yield", displayName: "收益率", errMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", pattern: /^(-?\d{0,2})$|^(-?\d{0,2}.\d{1,4})$/, max: 100, min: -100 },
                    { indexList: [[0, -1], [2, 3], [3, -1]], prop: "vm.selectedBond.netPrice", displayName: "债券净价", errMessage: "债券净价输入错误，小数位最多4位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 1000, min: 0 },
                    { indexList: [[0, -1], [2, 3], [3, -1]], prop: "vm.selectedBond.fullPrice", displayName: "债券全价", errMessage: "债券全价输入错误，小数位最多4位，最大值1000，最小值0", pattern: /^(\d{0,4})$|^(\d{0,4}.\d{1,4})$/, max: 1000, min: 0 },

                    { indexList: [[1, -1], [2, 3]], prop: "vm.calculationResult.calculationMainResult.irr", displayName: "IRR", errMessage: "IRR输入错误，小数位最多4位，最大值100%，最小值-100%", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { indexList: [[1, -1], [2, 3]], prop: "vm.calculationResult.calculationMainResult.basis", displayName: "基差", errMessage: "基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 },
                    { indexList: [[1, -1], [2, 3]], prop: "vm.calculationResult.calculationMainResult.netBasis", displayName: "净基差", errMessage: "净基差输入错误，小数位最多4位，最大值100，最小值-100", pattern: /^(-?\d{0,3})$|^(-?\d{0,3}.\d{1,4})$/, max: 100, min: -100 }
                ]


            },
            dto: {
                changeBondYieldType: {
                    GetYieldRequestDto: {
                        calculationMainRequest: {
                            calculationType: "selectedCalTgtType.value"
                        },
                        yieldType: "selectedYieldType.value",
                        futureContract: {
                            tfId: "selectedFuture.tfId",
                            tradingDate: "selectedFuture.startDate",
                            deliveryDate: "selectedFuture.maturityDate"
                        },
                        futurePrice: "selectedFuture.futurePrice",
                        capitalCost: "capitalCost",
                        bondKey: "selectedBond.bondKey",
                        bondListedMarket: "selectedBond.listedMarket"
                    },
                    MainRequestDto: {
                        irr: "calculationResult.calculationMainResult.irr"
                    }
                }
            }
        };
    }();
    
    mainModule.controller('tc.mainCtrl', [
        '$scope', '$state', '$sce', '$location', '$filter', 'httpService', 'commonService', 'delayEventService', 'tcService', 'routeConst', 'servicePathConst',
        function ($scope, $state, $sce, $location, $filter, httpService, commonService, delayEventService, tcService, routeConst, servicePathConst) {
            
            var filetimeToDatetimeFilter = $filter("filetimeToDatetime");
            
            // 获取初始化数据
            function getInitData() {
                
                httpService.postService(servicePathConst.tc_post_init_tfcalculator, undefined, function (data) {
                    $scope.viewBusy(false);
                    
                    if (!data) {
                        commonService.commonErrorDialog($scope, undefined, "没有获取到初始化数据");
                        return;
                    }
                    
                    $scope.futures = data.futures;
                    $scope.repoRates = data.repoRates;
                    
                    // <资金成本>： 默认显示FR007（7天回购定盘利率）最新价，可修改。
                    
                    $scope.vm.calculationResult = data.calculationResult;
                    
                    // 付息频率 dto -> vm
                    // 转换因子 dto -> vm
                    if ($scope.vm.calculationResult && $scope.vm.calculationResult.deliverableBonds && $scope.vm.calculationResult.deliverableBonds.length > 0 && $scope.vm.calculationResult.deliverableBonds instanceof Array) {
                        $scope.vm.calculationResult.deliverableBonds.forEach(function (item, index) {
                            var result = $scope.paymentFrequencys.findItem(function (e) { return e.value === item.paymentFrequency; });
                            if (result) item.paymentFrequency = result;
                            
                            if ($scope.vm.calculationResult.bondConvertionFactor && $scope.vm.calculationResult.bondConvertionFactor.length > 0 && $scope.vm.calculationResult.bondConvertionFactor instanceof Array) {
                                result = $scope.vm.calculationResult.bondConvertionFactor.findItem(function (e) { return e.bondKey === item.bondKey });
                                if (result) item.convertionFactor = result.convertionFactor;
                            }
                        });
                        
                        angular.merge($scope.vm.calculationResult.deliverableBonds[0], $scope.vm.calculationResult.defaultBondPrice);
                        
                        $scope.vm.selectedBond = $scope.vm.calculationResult.deliverableBonds[0];
                        
                        $scope.vm.selectedYieldType = $scope.yieldTypeButtons.findItem(function (e) { return e.value === $scope.vm.selectedBond.yieldType; });
                        
                        if ($scope.vm.calculationResult.defaultFuturePrice && $scope.futures && $scope.futures[0]) {
                            angular.merge($scope.futures[0], $scope.vm.calculationResult.defaultFuturePrice);
                        }
                    }
                    
                    if ($scope.futures && $scope.futures.length > 0 && $scope.futures instanceof Array) {
                        $scope.futures.forEach(function (item, index) {
                            item.maturityDate = filetimeToDatetimeFilter(item.maturityDate);
                            item.startDate = filetimeToDatetimeFilter(item.startDate);
                            
                            console.debug(item);
                        });
                    }
                    
                    $scope.originFutures = angular.copy($scope.futures);
                    
                    setDefaultValue();
                    
                    $scope.originVM = angular.copy($scope.vm);

                }, function (data) {
                    commonService.commonErrorDialog($scope, data, "获取初始化数据失败");
                    
                    $scope.viewBusy(false);
                });
            };
            
            // 设置画面初始值            
            function setDefaultValue() {
                // 默认“IRR/基差”
                $scope.vm.selectedCalTgtType = tcDataDefine.calTgtTypeButtons[0];
                
                if ($scope.bondTypeButtons) $scope.vm.selectedBondType = $scope.bondTypeButtons[0];
                
                // 默认值：TF当季合约（数字最小的那个）。
                if ($scope.futures) $scope.vm.selectedFuture = $scope.futures[0];
                
                // 资金成本
                $scope.vm.capitalCost = 0;

                //$scope.viewBusy(true);
                //tcService.changeFuture($scope, function () {
                //    $scope.viewBusy(false);
                //});
            };
            
            function doGetBondYieldCalculation() {
                if (!$scope.vm.selectedBond) {
                    commonService.commonErrorDialog($scope, undefined, "债券信息不能为空");
                    return;
                }
                
                var dto = commonService.getDto($scope.vm, tcDataDefine.dto.changeBondYieldType, $scope.DATE_FORMAT);
                
                $scope.viewBusy(true);
                
                httpService.postService(servicePathConst.tc_post_do_change_bond_yield_type, dto, function (data) {
                    $scope.viewBusy(false);
                    
                    if (!data) {
                        commonService.commonErrorDialog($scope, data, "获取所选债券数据失败");
                        return;
                    }
                    
                    var futurePrice = commonService.getPropertyX(data, 'calculationMainResult.futurePrice');
                    if(futurePrice) commonService.setPropertyX($scope, 'vm.selectedFuture.futurePrice', futurePrice);
                    
                    angular.merge($scope.vm.calculationResult, data);
                    angular.merge($scope.vm.selectedBond, data.bondPrice);

                }, function (data) {
                    commonService.commonErrorDialog($scope, data, "获取所选债券数据失败");
                    $scope.viewBusy(false);
                });
            };
            
            function checkValueRegexp(value, regExp, errMessage) {
                if (!value) return false;
                
                if (!regExp) return true;
                
                if (!regExp.test(value)) {
                    commonService.commonErrorDialog($scope, undefined, errMessage);
                    return false;
                } else {
                    return true;
                }
            };
            
            function checkValue(propName) {
                
                var rule = tcDataDefine.vaildRule.doCalculation_common.findItem(function (e) { return e.prop === propName; });
                
                if (!rule) return true;
                
                if (!rule.pattern && !rule.max && !rule.min) return true;
                
                var value = commonService.getPropertyX($scope, propName);
                
                if (rule.pattern && !rule.pattern.test(value)) {
                    commonService.commonErrorDialog($scope, undefined, rule.errMessage);
                    return false;
                }
                
                if (rule.max && rule.max <= +value) {
                    commonService.commonErrorDialog($scope, undefined, rule.errMessage);
                    return false;
                }
                if (rule.min && rule.min >= +value) {
                    commonService.commonErrorDialog($scope, undefined, rule.errMessage);
                    return false;
                }
                
                return true;
            };
            
            function checkVm(rule) {
                if (!rule || rule.length === 0 || !(rule instanceof Array)) return true;
                
                var result = true;
                
                rule.forEach(function (item, index) {
                    if (!result) return;
                    
                    var value = commonService.getPropertyX($scope, item.prop);
                    
                    if (!value) return;
                    
                    if (item.pattern && !item.pattern.test(value)) {
                        commonService.commonErrorDialog($scope, undefined, item.errMessage);
                        result = false;
                    }
                    
                    if ((item.max || item.max === 0) && item.max <= +value) {
                        commonService.commonErrorDialog($scope, undefined, item.errMessage);
                        result = false;
                    }
                    if ((item.min || item.min === 0) && item.min >= +value) {
                        commonService.commonErrorDialog($scope, undefined, item.errMessage);
                        result = false;
                    }
                });
                
                return result;
            }
            
            function callChangeFutureService() {
                $scope.viewBusy(true);
                tcService.changeFuture($scope, function () {
                    $scope.viewBusy(false);
                }, function () {
                    $scope.viewBusy(false);
                });
            };
            
            function callDoCalculationService(propName) {
                
                if (!$scope.vm.selectedCalTgtType) return false;
                
                var startDate = commonService.getPropertyX($scope, "vm.selectedFuture.startDate");
                var maturityDate = commonService.getPropertyX($scope, "vm.selectedFuture.maturityDate");
                
                if (startDate && maturityDate) {
                    
                    startDate = new Date(startDate);
                    maturityDate = new Date(maturityDate);
                    
                    if (startDate >= maturityDate) {
                        commonService.commonErrorDialog($scope, undefined, "交割日须晚于交易日，请重新输入。");
                        
                        maturityDate.setDate(startDate.getDate() + 1);
                        commonService.setPropertyX($scope, "vm.selectedBond.maturityDate", maturityDate);
                        return false;
                    }
                }
                
                var interestStartDate = commonService.getPropertyX($scope, "vm.selectedBond.interestStartDate");
                maturityDate = commonService.getPropertyX($scope, "vm.selectedBond.maturityDate");
                
                if (interestStartDate && maturityDate) {
                    
                    interestStartDate = new Date(interestStartDate);
                    maturityDate = new Date(maturityDate);
                    
                    if (interestStartDate > maturityDate) {
                        // commonService.commonErrorDialog($scope, undefined, "起息日须晚于到期日，请重新输入。");
                        // commonService.setPropertyX($scope, "vm.selectedBond.maturityDate", interestStartDate);
                        maturityDate.setDate(interestStartDate.getDate() + 1);
                        commonService.setPropertyX($scope, "vm.selectedBond.maturityDate", maturityDate);
                        // return false;
                    }
                }
                
                if ($scope.vm.selectedBondType && $scope.vm.selectedBondType.value === "VIRTUAL_BOND") {
                    var message = "请输入计算所需的虚拟券信息。{0}";
                    
                    if (!$scope.vm || !$scope.vm.selectedBond) {
                        commonService.commonErrorDialog($scope, undefined, message.format(""));
                        return false;
                    }
                    
                    if (!tcDataDefine.vaildRule.hasOwnProperty("doCalculation_" + $scope.vm.selectedCalTgtType.value)) return true;
                    
                    var ruleList = tcDataDefine.vaildRule["doCalculation_" + $scope.vm.selectedCalTgtType.value];
                    
                    var checkedProps = ruleList.findWhere(function (item) {
                        if (item.prop instanceof Array) {
                            var failedCheck = true;
                            item.prop.forEach(function (item1, index1) {
                                var value = commonService.getPropertyX($scope, item1);
                                if (value || value === 0) failedCheck = false;
                            });
                            return failedCheck;
                        } else {
                            var value = commonService.getPropertyX($scope, item.prop);
                            if (value === 0) return false;
                            return !commonService.getPropertyX($scope, item.prop);
                        }
                    }).map(function (item) {
                        return item.displayName;
                    }).join(",");
                    
                    if (checkedProps.length > 0) {
                        
                        // 非键盘回车触发的计算只拦截请求，不显示错误信息
                        if (propName !== "vm.selectedBond.interestStartDate" && 
                            propName !== "vm.selectedBond.maturityDate" && 
                            propName !== "vm.selectedBond.paymentFrequency")
                            commonService.commonErrorDialog($scope, undefined, message.format("\r\n" + checkedProps));
                        
                        return false;
                    }
                }
                
                switch (propName) {
                    case "vm.calculationResult.calculationMainResult.irr":
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.basis", undefined);
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.netBasis", undefined);
                        break;
                    case "vm.calculationResult.calculationMainResult.basis":
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.irr", undefined);
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.netBasis", undefined);
                        break;
                    case "vm.calculationResult.calculationMainResult.netBasis":
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.irr", undefined);
                        commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult.basis", undefined);
                        break;
                    default:
                        break;
                }
                
                $scope.viewBusy(true);
                tcService.doCalCulate($scope, function (data) {
                    $scope.viewBusy(false);
                }, function () {
                    $scope.viewBusy(false);
                });
            };
            
            $scope.DATE_FORMAT = DATE_FORMAT;
            $scope.DATETIME_FORMAT = DATETIME_FORMAT;
            
            $scope.bondTypeButtons = tcDataDefine.bondTypeButtons;
            $scope.calTgtTypeButtons = tcDataDefine.calTgtTypeButtons;
            $scope.yieldTypeButtons = tcDataDefine.yieldTypeButtons;
            $scope.paymentFrequencys = tcDataDefine.paymentFrequencys;
            
            // $scope.bondPriceType = tcDataDefine.bondPriceType;
            
            // Getter and setter for viewmodel
            $scope.__defineSetter__('vm', function (data) {
                this.val = data;
                commonService.safeApply($scope);
            });
            $scope.__defineGetter__('vm', function () { return this.val });
            
            var spin = undefined;
            $scope.viewBusy = function (bool) {
                if (!spin) {
                    spin = new Spinner({ color: '#fff', lines: 12, top: '30%' });
                }
                
                $scope.isViewBusy = bool;
                
                if (bool) {
                    spin.spin($("#tfcalculator-tc")[0]);
                } else {
                    spin.stop();
                }
            };
            
            $scope.displayWhen = function (conditions) {
                if (!conditions || conditions.length < 0 || !(conditions instanceof Array)) return false;
                
                if (!$scope.vm || !$scope.vm.selectedCalTgtType || !$scope.vm.selectedBondType) return false;
                
                var cond = [];
                
                cond.push($scope.calTgtTypeButtons.indexOfItem(function (e) { return e.value === $scope.vm.selectedCalTgtType.value; }));
                cond.push($scope.bondTypeButtons.indexOfItem(function (e) { return e.value === $scope.vm.selectedBondType.value; }));
                
                var result = conditions.containsItem(cond);
                
                if (!result) {
                    conditions.forEach(function (item, index) {
                        if (result) return;
                        
                        if (item[0] && item[0] === -1 && item[1] && item[1] === -1) {
                            result = true;
                        } else if (item[0] && item[0] === -1) {
                            result = cond[1] === item[1];
                        } else if (item[1] && item[1] === -1) {
                            result = cond[0] === item[0];
                        }
                    });
                }
                
                return result;
            };
            
            // 按钮 激活 非激活 状态切换
            $scope.onActiveButton = function (e, prop, isRaiseOnChangeEvent) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                
                if (obj && obj.nodeName === "BUTTON") {
                    // $scope[prop] = obj.value;
                    commonService.setPropertyX($scope, prop, angular.element(obj).scope().item);
                    
                    if (isRaiseOnChangeEvent && $scope.onChangePropperty) {
                        $scope.onChangePropperty(prop);
                    }
                }
            };
            
            $scope.onChangePropperty = function (propName) {
                if (!$scope.vm) return;
                
                console.debug("onChangePropperty: " + propName);
                
                var calTgtType = undefined,
                    tmp = undefined;
                
                var setIrrForCalculator = function () {
                    tmp = $scope.vm.calculationResult.calculationMainResult;
                    if (!tmp.irr) tmp.irr = 1;
                    $scope.vm.calculationResult = {};
                    $scope.vm.calculationResult.calculationMainResult = tmp;
                };
                
                var clearResult = function () {
                    calTgtType = commonService.getPropertyX($scope, 'vm.selectedCalTgtType');
                    if (calTgtType.value === $scope.calTgtTypeButtons[1].value) {
                        setIrrForCalculator();
                    } else if (calTgtType.value === $scope.calTgtTypeButtons[2].value) {
                        delete $scope.vm.selectedFuture.futurePrice;
                        setIrrForCalculator();
                    } else if (calTgtType.value === $scope.calTgtTypeButtons[3].value) {
                        $scope.vm.calculationResult = {};
                    } else {
                        $scope.vm.calculationResult = {};
                    }
                };
                
                switch (propName) {
                    case 'vm.selectedCalTgtType':
                        calTgtType = commonService.getPropertyX($scope, propName);
                        if (!calTgtType) break;
                        
                        //  清空结果列表
                        // OPM-1031 1.1 TF计算器： 切换计算目标时， 不应该清空已保存结果
                        // $scope.resultList = [];
                        
                        if (calTgtType.value === $scope.calTgtTypeButtons[0].value) {
                            commonService.setPropertyX($scope, "vm.selectedBond.yieldByDay", undefined);

                        } else if (calTgtType.value === $scope.calTgtTypeButtons[1].value) {
                            // callChangeFutureService();

                        } else if (calTgtType.value === $scope.calTgtTypeButtons[2].value) {
                            commonService.setPropertyX($scope, "vm.selectedBond.yieldByDay", undefined);

                        } else if (calTgtType.value === $scope.calTgtTypeButtons[3].value) {
                            commonService.setPropertyX($scope, "vm.selectedBond.yieldByDay", undefined);
                            
                            $scope.vm.selectedBondType = $scope.bondTypeButtons[0];
                            
                            callChangeFutureService();
                        }
                        
                        break;
                    case 'vm.selectedYieldType':
                        doGetBondYieldCalculation();
                        break;
                    case 'vm.selectedBondType':
                        var bondType = commonService.getPropertyX($scope, propName);
                        if (!bondType) break;
                        
                        if ($scope && $scope.vm && $scope.vm.selectedBond && $scope.vm.selectedBond.bondCode === "VIRTUAL_BOND")
                            $scope.vm.selectedBond.bondCode = "";
                        
                        if (bondType.value === $scope.bondTypeButtons[0].value) {
                            callChangeFutureService();
                        } else if (bondType.value === $scope.bondTypeButtons[1].value) {
                            callChangeFutureService();
                        } else if (bondType.value === $scope.bondTypeButtons[2].value) {
                            
                            delete $scope.vm.selectedBond.yieldByDay;
                            $scope.vm.selectedBond = undefined;
                            
                            clearResult();

                        } else if (bondType.value === $scope.bondTypeButtons[3].value) {
                            // 所选债券的Code设为“虚拟券” <债券代码>：显示“虚拟券”，不能修改
                            if (!$scope.vm.selectedBond) {
                                $scope.vm.selectedBond = {};
                            }
                            
                            delete $scope.vm.selectedBond.yieldByDay;
                            $scope.vm.selectedBond.bondCode = $scope.bondTypeButtons[3].value;
                            
                            // <付息频率>：默认显示“年度”，用户可“切换”
                            // if (!$scope.vm.selectedBond.paymentFrequency) $scope.vm.selectedBond.paymentFrequency = $scope.paymentFrequencys[2];
                            if (!$scope.vm.selectedBond.paymentFrequency)
                                $scope.vm.selectedBond.paymentFrequency = $scope.paymentFrequencys.findItem(function (e) { return e.value === "A"; });
                            
                            // <起息日>：默认显示为空，由用户填写
                            // <到期日>：默认显示为空，由用户填写
                            $scope.vm.selectedBond.interestStartDate = undefined;
                            $scope.vm.selectedBond.maturityDate = undefined;
                            
                            // <票息率>：默认显示为空，由用户填写
                            $scope.vm.selectedBond.fixedCoupon = undefined;
                            
                            $scope.vm.selectedBond.yield = undefined;
                            $scope.vm.selectedBond.netPrice = undefined;
                            $scope.vm.selectedBond.fullPrice = undefined;
                            
                            $scope.vm.selectedBond.convertionFactor = undefined;
                            
                            clearResult();
                            
                            $scope.searchedBondList = [];
                        }
                        
                        break;
                    default:
                        delayEventService.delayOnChangeEvent(function () {
                            console.log("onChangePropperty propName:{0}".format(propName));
                            
                            if (propName === "vm.capitalCost") {
                                var checkResult = checkVmForDoCalculation();
                                
                                if (!checkResult) return;
                            }
                            
                            var result = callDoCalculationService(propName);
                            
                            if (!result && $scope.displayWhen([[-1, 3]]))
                                commonService.setPropertyX($scope, "vm.selectedBond.convertionFactor", undefined);
                        });
                        
                        break;
                };
            };
            
            function checkVmForDoCalculation() {
                var vaildRule = tcDataDefine.vaildRule.ruleList.findWhere(function (e) {
                    return $scope.displayWhen(e.indexList);
                }).map(function (e) {
                    var temp = angular.copy(e);
                    delete temp['indexList'];
                    return temp;
                });
                
                if (!checkVm(vaildRule)) {
                    if ($scope.displayWhen([[-1, 3]]))
                        commonService.setPropertyX(scope, "vm.selectedBond.convertionFactor", undefined);
                    return false;
                }
                
                return true;
            };
            
            $scope.onKeydownChangedProperty = function (event, propName) {
                
                //bondPriceType: {
                //    "vm.selectedBond.yield": "YIELD",
                //    "vm.selectedBond.netPrice": "NET_PRICE",
                //    "vm.selectedBond.fullPrice": "FULL_PRICE"
                //},
                if (tcDataDefine.bondPriceType.hasOwnProperty(propName)) {
                    if ($scope.vm.selectedBond) $scope.vm.selectedBond.bondPriceType = tcDataDefine.bondPriceType[propName];
                    else return;
                }
                
                if (event.keyCode !== 13) return;
                
                var checkResult = checkVmForDoCalculation();
                
                if (!checkResult) {
                    if ($scope.displayWhen([[-1, 3]]))
                        commonService.setPropertyX($scope, "vm.selectedBond.convertionFactor", undefined);
                    return;
                }
                
                var result = callDoCalculationService(propName);
                
                if (!result && $scope.displayWhen([[-1, 3]]))
                    commonService.setPropertyX($scope, "vm.selectedBond.convertionFactor", undefined);
            };
            
            // 页面初始化
            var initView = function () {
                $scope.viewBusy(true);
                
                $scope.vm = {};
                
                getInitData();
                
                // commonService.safeApply($scope);
            }();
        }]);

});

