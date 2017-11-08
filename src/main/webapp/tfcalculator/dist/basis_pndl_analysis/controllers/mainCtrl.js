/**
 * Create by WeiLai on 03/14/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'basis_pndl_analysis.mainModule', 'spin'
], function (mainModule, Spinner) {
    'use strict';
    
    // ReSharper disable InconsistentNaming
    var DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    // ReSharper restore InconsistentNaming
    
    
    mainModule.controller('basis_pndl_analysis.mainCtrl', ['$scope', '$state', '$filter', '$location', 'commonService', 'bplaService', 'routeConst',
        function ($scope, $state, $filter, $location, commonService, bplaService, routeConst) {
            var currencyFilter = $filter('currency'),
                numberFilter = $filter('number');
            
            var bplaDataDefine = {
                dataFields: [
                    { sourceField: "futureContracts", header: "", field: "futureContracts" },

                    { sourceField: "repoRateTypes", header: "", field: "repoRateTypes" },
                    { sourceField: "responseDto", header: "", field: "responseDto" },

                   // { "sourceField": "repoRateTypes", "header": "", "field": "repoRateTypes" },
                   // { "sourceField": "yieldTypes", "header": "", "field": "yieldTypes" },
                   // { "sourceField": "longShortSymbols", "header": "", "field": "longShortSymbols" },
                   // { "sourceField": "longShortSymbols.", "header": "", "field": "longShortSymbols" },

                   // { "sourceField": "ctdCode", "header": "", "field": "ctdCode" },
                   // { "sourceField": "ctdDuration", "header": "", "field": "ctdDuration" },
                   // { "sourceField": "cdcDuration", "header": "", "field": "cdcDuration" },
                   // { "sourceField": "irr", "header": "", "field": "irr" },
                   // { "sourceField": "basis", "header": "", "field": "basis" },

                   // { "sourceField": "netBasis", "header": "", "field": "netBasis" },
                   // { "sourceField": "futureNumber", "header": "", "field": "futureNumber" },
                   // { "sourceField": "futurePrice", "header": "", "field": "futurePrice" },
                   // { "sourceField": "ctdFullPrice", "header": "", "field": "ctdFullPrice" },
                   // { "sourceField": "bondCode", "header": "", "field": "bondCode" },

                   // { "sourceField": "yield", "header": "", "field": "yield" },
                   // { "sourceField": "bondNetPrice", "header": "", "field": "bondNetPrice" },
                   // { "sourceField": "bondFullPrice", "header": "", "field": "bondFullPrice" },
                   // { "sourceField": "conversionFactor", "header": "", "field": "conversionFactor" },
                   // { "sourceField": "futureLastUpdateTime", "header": "", "field": "futureLastUpdateTime" },

                   // { "sourceField": "yieldLastUpdateTime", "header": "", "field": "yieldLastUpdateTime" },
                   // { "sourceField": "holdingPeriod", "header": "", "field": "holdingPeriod" }
                ],
                filter: {
    
                },
                hedgeMap : {
                    LONG_BASIS: "做多基差",
                    SHORT_BASIS: "做空基差"
                },
                bondMap: {
                    ofr: "Ofr",
                    bid: "Bid",
                    deal: "成交",
                    cdc: "中债"
                },
                requestDto: {
                    futurePrice: "responseDto.futurePrice",
                    ctdFullPrice: "responseDto.ctdFullPrice",
                    ctdBondKey: "responseDto.ctdBondKey",
                    ctdListedMarket: "responseDto.ctdListedMarket",
                    bondKey: "responseDto.bondKey",
                    
                    bondListedMarket: "responseDto.bondListedMarket",
                    'yield': "responseDto.yield",
                    bondNetPrice: "responseDto.bondNetPrice",
                    bondFullPrice: "responseDto.bondFullPrice",
                    futureContract: "selectedFutureContract",
                    
                    openPositionDate: "openPositionDate",
                    closePositionDate: "closePositionDate",
                    capitalCost: "selectedRepoRateType.capitalCost",
                    bondNominalPrincipal: "bondNominalPrincipal",
                    impactCost: "impactCost",
                    
                    yieldType: "yieldType",
                    longShortSymbol: "selectedHedge",
                    curveYtm: "selectChartPoint.yield",
                    curveFuturesPrice: "selectChartPoint.futurePrice"
                },
                requestDtoVaildMessage: {
                    futurePrice: "期货价格",
                    ctdFullPrice: "CTD全价",
                    ctdBondKey: "CTD bond_key",
                    ctdListedMarket: "CTD listed_market",
                    bondKey: "现卷 bond_key",
                    
                    bondListedMarket: "现卷 listed_market",
                    'yield': "现卷收益率",
                    bondNetPrice: "现卷净价",
                    bondFullPrice: "现卷全价",
                    futureContract: "期货信息",
                    
                    openPositionDate: "期货开仓日",
                    closePositionDate: "期货平仓日",
                    capitalCost: "资金成本",
                    bondNominalPrincipal: "现卷名义本金",
                    // impactCost: "冲击成本",
                    
                    yieldType: "获取何种最新价格：ofr？bid？成交？中债？",
                    longShortSymbol: "做多 or 做空",
                    // curveYtm: "selectChartPoint.yield",
                    // curveFuturesPrice: "selectChartPoint.alterData22"
                },
                requestDtoVaildRule: {
                    post_update_futures_price: {
                        futureContract: "期货信息", 
                        openPositionDate: "期货开仓日",
                        closePositionDate: "期货平仓日",
                        capitalCost: "资金成本"
                    },
                    post_do_calculator: {
                        futurePrice: "期货价格",
                        ctdFullPrice: "CTD全价",
                        ctdBondKey: "CTD bond_key",
                        bondKey: "现卷 bond_key",
                        bondListedMarket: "现卷 listed_market", 
                        futureContract: "期货信息", 
                        openPositionDate: "期货开仓日",
                        closePositionDate: "期货平仓日",
                        capitalCost: "资金成本"
                    }
                }
            };
            
            var spin = undefined;
            $scope.viewBusy = function (bool) {
                if (!spin) {
                    spin = new Spinner({ color: '#fff', lines: 12, top: '30%' });
                }
                
                $scope.isViewBusy = bool;
                
                if (bool) {
                    spin.spin($("#tfcalculator-bpla")[0]);
                } else {
                    spin.stop();
                }
            };
            
            $scope.__defineSetter__('vm', function (data) {
                
                this.val = data;
                
                if ($scope.onVmChanged4Form) $scope.onVmChanged4Form();
                if ($scope.onVmChanged4Chart) $scope.onVmChanged4Chart();
                
                commonService.safeApply($scope);
            });
            
            $scope.__defineGetter__('vm', function () { return this.val });
            
            $scope.vm = {};
            
            // 按钮 激活 非激活 状态切换
            $scope.onActiveButton = function (e, prop) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                
                if (obj && obj.nodeName === "BUTTON") {
                    $scope.vm[prop] = obj.value;
                    
                    $scope.doCalCulate(prop);
                    
                    saveViewModel("onActiveButton");
                    // console.log($scope.vm.toString());
                }
            };
            
            $scope.isWarnDatetime = function (value) {
                // return true;
                return commonService.getDateDiff(value, new Date().format(DATE_FORMAT), "minute") >= 60;
            };
            
            function changeFuturePrice(isRefresh) {
                if (!$scope.vm.responseDto.futurePrice) return;
                
                $scope.viewBusy(true);
                
                var dataDefine = angular.copy(bplaDataDefine.requestDto);
                
                //dataDefine.bondCode = "responseDto.bondCode";
                //dataDefine.ctdBondCode = "responseDto.ctdBondCode";
                bplaService.changeFuturePrice(dataDefine, bplaDataDefine.requestDtoVaildRule.post_update_futures_price, $scope.vm, function (data) {
                    // Only one point
                    if (data && $scope.vm.responseDto) {

                        var targetPointIndex = undefined;

                        $scope.vm.responseDto.profitLossByYield.forEach(function(item, index) {
                            if (item.yield === data.profitLossByYield.yield) {
                                targetPointIndex = index;
                            }
                        });

                        if (targetPointIndex >= 0) {
                            $scope.vm.responseDto.profitLossByYield[targetPointIndex] = data.profitLossByYield[0];
                        }
                        
                        delete data.profitLossByYield;
                    }


                    angular.merge($scope.vm.responseDto, data);
                    
                    if ($scope.onVmChanged4Form) $scope.onVmChanged4Form();
                    if ($scope.onVmChanged4Chart) $scope.onVmChanged4Chart();
                    
                    $scope.viewBusy(false);
                    
                    saveViewModel("changeFuturePrice");
                }, function (data) {
                    
                    commonService.commonErrorDialog($scope, data, "修改期货价格失败");
                    
                    $scope.viewBusy(false);
                    
                    console.log("修改期货价格 Failed: " + data);
                });
            };
            
            function checkValueAvailability(newValue, oldValue) {
                console.log("checkValueAvailability newValue:%s oldValue:%s", newValue, oldValue);
                
                if (newValue < 0.75 * oldValue) return false;
                
                if (newValue > 10 * oldValue) return false;
                
                return true;
            };
            
            // 计算相关字段修改时触发
            $scope.onPropForCalculateChange = function (e, funcName) {
                //var ex = e || window.event;
                //var obj = ex.target || ex.srcElement;
                
                // 计算持有期
                $scope.vm.responseDto.holdingPeriod = function () {
                    if (!$scope.vm.openPositionDate || !$scope.vm.closePositionDate) return 0;
                    
                    return commonService.getDateDiff($scope.vm.openPositionDate, $scope.vm.closePositionDate, "day");
                }();
                
                // console.log("onAnyPropChange" + JSON.stringify(obj));
                
                switch (funcName) {
                    case 'openPositionDate':
                        if ($scope.vm.responseDto.holdingPeriod <= 0) {
                            commonService.commonConfirmDialog($scope, "开仓日必须早于（小于）平仓日。", undefined, undefined, undefined, { hideCancel: true });
                            return;
                        }
                        break;
                    case 'closePositionDate':
                        if ($scope.vm.responseDto.holdingPeriod <= 0) {
                            commonService.commonConfirmDialog($scope, "开仓日必须早于（小于）平仓日。", undefined, undefined, undefined, { hideCancel: true });
                            return;
                        }
                        break;
                    default:
                        if (e && e.keyCode !== 13) return;
                        else {
                            switch (funcName) {
                                case "yield":
                                    if (!checkValueAvailability(+$scope.vm.responseDto.yield, +$scope.originVM.responseDto.yield)) {
                                        commonService.commonConfirmDialog($scope, "收益率: 必须大于原始值的75%，小于原始值的1000%。", undefined, undefined, undefined, { hideCancel: true });
                                        return;
                                    }
                                    break;
                                case "bondNetPrice":
                                    if (!checkValueAvailability(+$scope.vm.responseDto.bondNetPrice, +$scope.originVM.responseDto.bondNetPrice)) {
                                        commonService.commonConfirmDialog($scope, "净价: 必须大于原始值的75%，小于原始值的1000%。", undefined, undefined, undefined, { hideCancel: true });
                                        return;
                                    }
                                    break;
                                case "bondFullPrice":
                                    if (!checkValueAvailability(+$scope.vm.responseDto.bondFullPrice, +$scope.originVM.responseDto.bondFullPrice)) {
                                        commonService.commonConfirmDialog($scope, "全价: 必须大于原始值的75%，小于原始值的1000%。", undefined, undefined, undefined, { hideCancel: true });
                                        return;
                                    }
                                    break;
                                case "impactCost":
                                    if ($scope.vm.impactCost !== NaN && $scope.vm.impactCost !== undefined) {
                                        if ($scope.vm.impactCost > ($scope.vm.responseDto.yield * 100)) {
                                            $scope.vm.impactCost = +numberFilter($scope.vm.responseDto.yield * 100, 3);
                                            commonService.commonConfirmDialog($scope, "冲击成本须小于当前收益率， 请重新输入。", undefined, undefined, undefined, { hideCancel: true });
                                            return;
                                        }
                                        
                                        if ($scope.vm.impactCost < 0) {
                                            commonService.commonConfirmDialog($scope, "冲击成本须大于或等于零。", undefined, undefined, undefined, { hideCancel: true });
                                            return;
                                        }
                                    } else {
                                        $scope.vm.impactCost = 0;
                                        commonService.commonConfirmDialog($scope, "冲击成本须大于或等于零。", undefined, undefined, undefined, { hideCancel: true });
                                        return;
                                    }
                                    break;
                                case "bondNominalPrincipal":
                                    var checkResult = function () {
                                        if ($scope.vm.bondNominalPrincipal < 1000000) {
                                            $scope.vm.bondNominalPrincipal = 1000000;
                                            return false;
                                        }
                                        
                                        if ($scope.vm.bondNominalPrincipal > 1000000000000) {
                                            $scope.vm.bondNominalPrincipal = 1000000000000;
                                            return false;
                                        }
                                        
                                        return true;
                                    }();
                                    
                                    if (!checkResult) {
                                        commonService.commonConfirmDialog($scope, "名义本金须大于一百万元，小于等于一万亿，请重新输入。", undefined, undefined, undefined, { hideCancel: true });
                                        return;
                                    }
                                default:
                                    break;
                            }
                        }
                        break;
                }
                
                switch (funcName) {
                    case "futurePrice":
                        changeFuturePrice();
                        break;
                    default:
                        $scope.doCalCulate(funcName);
                        break;
                }
                
                saveViewModel("onPropForCalculateChange");
            };
            
            var pendingChangingHandler = undefined;
            
            $scope.applyCurrencyFormat = function (prop) {
                if (pendingChangingHandler) clearTimeout(pendingChangingHandler);
                
                pendingChangingHandler = setTimeout(function () {
                    // 本金格式转换
                    var v = commonService.getPropertyX($scope, prop);
                    
                    commonService.setProppertyX($scope, prop, v + 1);
                    commonService.safeApply($scope);
                    commonService.setProppertyX($scope, prop, v);
                    commonService.safeApply($scope);
                }, 1000);
            };
            
            // 重置画面数据
            $scope.resetViewModel = function () {
                // $scope.vm = angular.copy($scope.originVM);
                if ($scope.vm.responseDto) {
                    $scope.vm.responseDto.profitLossByYield = angular.copy($scope.originVM.responseDto.profitLossByYield);
                    
                    $scope.vm.impactCost = $scope.originVM.impactCost;
                    $scope.vm.bondNominalPrincipal = $scope.originVM.bondNominalPrincipal;
                    $scope.vm.responseDto.futureNumber = $scope.originVM.responseDto.futureNumber;
                }
                
                $scope.onVmChanged4Chart && $scope.onVmChanged4Chart();
                
                commonService.safeApply($scope);
            };
            
            $scope.getEnumButtonName = function (mapName, key) {
                if (!bplaDataDefine.hasOwnProperty(mapName)) return undefined;
                
                return bplaDataDefine[mapName][key];
            };
            
            // 计算
            $scope.doCalCulate = function (updateProp) {
                
                //var value = commonService.getPropertyX($scope.vm, "responseDto.yield");
                
                //console.log("Value: "+ value);
                
                //var value = commonService.setProppertyX($scope.test, "responseDto.yield.a.b.c", 123456);
                
                //console.log("Value: " + JSON.stringify(value));
                
                $scope.vm.selectChartPoint = undefined;
                
                $scope.viewBusy(true);
                
                bplaService.doCalCulate(bplaDataDefine.requestDto, bplaDataDefine.requestDtoVaildRule.post_do_calculator, $scope.vm, function (data) {
                    
                    //if (updateProp === "yieldType") {
                    //    if (!data) {
                    //        commonService.commonConfirmDialog($scope, "当前所选债券没有最新收益率信息。", undefined, undefined, undefined, { hideCancel: true });
                    //        return;
                    //    }
                    
                    //    if ($scope.vm) {
                    //        // 现券与CTD相同时，禁止修改收益率及相关信息
                    //        $scope.vm.isYieldEditable = ($scope.vm.responseDto.bondCode !== $scope.vm.responseDto.ctdBondCode);
                    //    }
                    //}
                    
                    switch (updateProp) {
                        case "yieldType":
                            if (!data) {
                                commonService.commonConfirmDialog($scope, "当前所选债券没有最新收益率信息。", undefined, undefined, undefined, { hideCancel: true });
                                return;
                            }
                            
                            if ($scope.vm) {
                                // 现券与CTD相同时，禁止修改收益率及相关信息
                                $scope.vm.isYieldEditable = ($scope.vm.responseDto.bondCode !== $scope.vm.responseDto.ctdBondCode);
                            }
                            break;
                        case "bondCode":
                            if (!data) {
                                commonService.commonConfirmDialog($scope, "当前所选债券没有最新收益率信息。", undefined, undefined, undefined, { hideCancel: true });
                                return;
                            }
                            
                            if ($scope.vm) {
                                // 现券与CTD相同时，禁止修改收益率及相关信息
                                $scope.vm.isYieldEditable = ($scope.vm.responseDto.bondCode !== $scope.vm.responseDto.ctdBondCode);
                            }
                            
                            if ($scope.vm.responseDto && $scope.vm.responseDto.isCdcYieldLastUpdateTime)
                                $scope.vm.yieldType = "cdc";
                            else
                                $scope.vm.yieldType = "ofr";
                            break;
                        case "yield":
                            $scope.vm.yieldType = undefined;
                            break;
                        case "bondNetPrice":
                            $scope.vm.yieldType = undefined;
                            break;
                        case "bondFullPrice":
                            $scope.vm.yieldType = undefined;
                            break;
                        default:
                            break;
                    }
                    
                    angular.merge($scope.originVM.responseDto, data);
                    angular.merge($scope.vm.responseDto, data);
                    
                    if ($scope.onVmChanged4Form) $scope.onVmChanged4Form();
                    if ($scope.onVmChanged4Chart) $scope.onVmChanged4Chart();
                    
                    $scope.viewBusy(false);
                    
                    saveViewModel("doCalCulate");
                }, function (data) {
                    // commonService.commonErrorDialog($scope, "计算失败", undefined, JSON.stringify(data));
                    
                    commonService.commonErrorDialog($scope, data, "计算失败");
                    
                    $scope.viewBusy(false);
                    console.log("计算 Failed: " + data);
                }, updateProp);
            };
            
            $scope.doCalCulatePoint = function () {
                
                $scope.vm.selectChartPoint.futurePrice = (+$scope.vm.selectChartPoint.futurePrice);
                
                bplaService.doCalCulate(bplaDataDefine.requestDto, bplaDataDefine.requestDtoVaildRule.post_do_calculator, $scope.vm, function (data) {
                    
                    var resultPoint = data.profitLossByYield[0];
                    
                    $scope.vm.selectChartPoint = angular.merge($scope.vm.selectChartPoint, resultPoint);
                    
                    if (resultPoint) {
                        commonService.findFromArrayBy($scope.vm.responseDto.profitLossByYield, resultPoint.yield, "yield");
                        
                        $scope.vm.responseDto.profitLossByYield.forEach(function (item, index) {
                            if (item.yield === resultPoint.yield) {
                                $scope.vm.responseDto.profitLossByYield[index] = resultPoint;
                            }
                        });
                        
                        console.log("delete data.profitLossByYield: " + JSON.stringify(resultPoint));
                        delete data.profitLossByYield;
                    }
                    
                    angular.merge($scope.vm.responseDto, data);
                    
                    if ($scope.onVmChanged4Form) $scope.onVmChanged4Form();
                    if ($scope.onVmChanged4Chart) $scope.onVmChanged4Chart();
                    
                    saveViewModel("doCalCulatePoint");
                }, function (data) {
                    
                    // commonService.commonErrorDialog($scope, "计算失败", undefined, JSON.stringify(data));
                    
                    commonService.commonErrorDialog($scope, data, "计算失败");
                    console.log("计算Point Failed: " + data);
                });
            };
            
            // 修改期货价格
            $scope.onClickRefreshFuturePrice = function () {
                if (!$state.params.tfId) commonService.commonConfirmDialog($scope, "请选择期货合约", undefined, undefined, undefined, { hideCancel: true });
                
                // initByTfId($state.params.tfId);
                
                changeFuturePrice();
                saveViewModel("onPropForCalculateChange");
            };
            
            // 重置图表点
            $scope.refreshProfitLossByYieldPoint = function (index) {
                if ($scope.originVM && $scope.originVM.responseDto && $scope.originVM.responseDto.profitLossByYield) {
                    $scope.vm.responseDto.profitLossByYield[index] = angular.copy($scope.originVM.responseDto.profitLossByYield[index]);
                }
                
                saveViewModel("refreshProfitLossByYieldPoint");
            };
            
            function saveViewModel(message) {
                console.log("Saved viewmodel into localStorage. Called by %s.", message);
                
                localStorage.setItem("vm", JSON.stringify($scope.vm));
                localStorage.setItem("originVM", JSON.stringify($scope.originVM));
            };
            
            function loadViewModel() {
                console.log("Loaded viewmodel tfId from localStorage.");
                
                $scope.vm = JSON.parse(localStorage.getItem("vm"));
                $scope.originVM = JSON.parse(localStorage.getItem("originVM"));
            };
            
            function initByTfId(tfId) {
                $scope.viewBusy(true);
                
                bplaService.getFuturesContractList(bplaDataDefine.dataFields, tfId, function (data) {
                    if ($state.params.tfId) {
                        data.selectedFutureContract = commonService.findFromArrayBy(data.futureContracts, $state.params.tfId, "tfId");
                        
                        // 设置默认值
                        if (!data.openPositionDate) data.openPositionDate = new Date().format("yyyy-MM-dd");
                        if (!data.closePositionDate) data.closePositionDate = data.selectedFutureContract.maturityDate;
                        
                        if (!data.selectedRepoRateType && data.repoRateDto) {
                            data.selectedRepoRateType = commonService.findFromArrayBy(data.repoRateTypes, data.repoRateDto.code, "code");
                            data.selectedRepoRateType.capitalCost = bplaService.getCapitalCost(data.selectedRepoRateType);
                        }
                        
                        // if (!data.yieldType) data.yieldType = "ofr";
                        if (!data.selectedHedge) data.selectedHedge = "SHORT_BASIS";
                        if (!data.impactCost) data.impactCost = 0;
                        if (!data.bondNominalPrincipal) data.bondNominalPrincipal = 10000000;
                        
                        console.log("mainCtrl $scope.id: " + $scope.$id);
                    } else {
                        if (data.responseDto && data.responseDto.futureContract)
                            $state.go(routeConst.basis_pndl_analysis, { tfId: data.responseDto.futureContract.tfId });
                        return;
                    }
                    
                    if (data && data.vm) {
                        // 现券与CTD相同时，禁止修改收益率及相关信息
                        data.vm.isYieldEditable = (data.vm.responseDto.bondCode !== data.vm.responseDto.ctdBondCode);
                    }
                    
                    $scope.originVM = data;
                    
                    $scope.vm = angular.copy(data);
                    
                    saveViewModel("initByTfId");
                    
                    commonService.safeApply($scope);
                    
                    $scope.viewBusy(false);

                }, function (data) {
                    // commonService.commonConfirmDialog($scope, "获取初始化数据失败", undefined, undefined, undefined, { hideCancel: true });
                    
                    // commonService.commonErrorDialog($scope, "获取初始化数据失败", undefined, JSON.stringify(data));
                    
                    commonService.commonErrorDialog($scope, data, "获取初始化数据失败");
                    
                    $scope.viewBusy(false);
                });
            };
            
            // 页面初始化
            var initView = function () {
                
                loadViewModel();
                
                if (!$scope.vm) {
                    initByTfId();
                    return;
                }
                
                if ($scope.vm.selectedFutureContract) {
                    if ($state.params.tfId) {
                        if ($scope.vm.selectedFutureContract.tfId === $state.params.tfId) {
                            // Apply Viewmodel
                            if ($state.params.tfId) $scope.vm.selectedFutureContract = commonService.findFromArrayBy($scope.vm.futureContracts, $state.params.tfId, "tfId");
                            commonService.safeApply($scope);
                        } else {
                            $scope.vm = {};
                            $scope.originVM = {};
                            
                            // 清空
                            saveViewModel("initView");
                            
                            initByTfId($state.params.tfId);
                        }
                    } else {
                        $state.go(routeConst.basis_pndl_analysis, { tfId: $scope.vm.selectedFutureContract.tfId });
                    }
                } else {
                    initByTfId();
                }
            }();
        }]);

});

