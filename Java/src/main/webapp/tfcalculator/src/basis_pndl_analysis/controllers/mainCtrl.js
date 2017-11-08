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
    var DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // ReSharper restore InconsistentNaming
    
    
    mainModule.controller('basis_pndl_analysis.mainCtrl', ['$scope', '$state', '$filter', '$location', 'commonService', 'bplaService', 'routeConst',
        function ($scope, $state, $filter, $location, commonService, bplaService, routeConst) {
            var currencyFilter = $filter('currency'),
                numberFilter = $filter('number');
            
            var bplaDataDefine = {
                dataFields: [
                    { sourceField: "futureContracts", header: "", field: "futureContracts" },
                    { sourceField: "repoRateTypes", header: "", field: "repoRateTypes" },
                    { sourceField: "responseDto", header: "", field: "responseDto" }
                ],
                filter: {
                
                },
                hedgeMap: {
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
                },
                actionMap: {
                    whenDoCalCulateCb: {
                        yieldType: function (data) {
                            if (!data) {
                                commonService.commonConfirmDialog($scope, "当前所选债券没有最新收益率信息。", undefined, undefined, undefined, { hideCancel: true });
                                return;
                            }
                        },
                        bondCode: function (data) {
                            if (!data) {
                                commonService.commonConfirmDialog($scope, "当前所选债券没有最新收益率信息。", undefined, undefined, undefined, { hideCancel: true });
                                return;
                            }
                            
                            if ($scope.vm) {
                                // 现券与CTD相同时，禁止修改收益率及相关信息
                                $scope.vm.isYieldEditable = (data.bondCode !== data.ctdBondCode);
                            }
                            
                            if (data.isCdcYieldLastUpdateTime) {
                                data.yieldType = "cdc";
                                $scope.vm.yieldType = "cdc";
                            } else
                                data.yieldType = "ofr";
                            
                            updateVaildRule(data);
                        },
                        'vm.responseDto.yield': function (data) {
                            $scope.vm.yieldType = undefined;
                        },
                        'vm.responseDto.bondNetPrice': function (data) {
                            $scope.vm.yieldType = undefined;
                        },
                        'vm.responseDto.bondFullPrice': function (data) {
                            $scope.vm.yieldType = undefined;
                        }

                    }
                },
                vaildRule: {
                    'vm.responseDto.yield': { type: 'range', param: [], message: "收益率: 必须大于原始值的75%，小于原始值的1000%。" },
                    'vm.responseDto.bondNetPrice': { type: 'range', param: [], message: "净价: 必须大于原始值的75%，小于原始值的1000%。" },
                    'vm.responseDto.bondFullPrice': { type: 'range', param: [], message: "全价: 必须大于原始值的75%，小于原始值的1000%。" }
                },
                
                newVaildRule: {
                    post_update_futures_price: [
                        { prop: "vm.responseDto.yield", rule: "required", displayName: "收益率", errorMessage: "请输入收益率" },
                        { prop: "vm.responseDto.yield", rule: "regexp", displayName: "收益率", errorMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", param: { pattern: /^(-?\d{0,2})$|^(-?\d{0,2}.\d{1,4})$/ } },
                        { prop: "vm.responseDto.yield", rule: "rangeOpen", displayName: "收益率", errorMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", param: { max: 100, min: -100 } }]
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
                
                $scope.isDetailShow = false;
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
                return commonService.getDateDiff(value, new Date().formatDate(DATE_FORMAT), "minute") >= 60;
            };
            
            function updateVaildRule(data) {
                if (!data) return;
                
                //if (data.yield && bplaDataDefine.vaildRule['vm.responseDto.yield']) $scope.vm.vaildYieldRange = [data.yield * 0.75, data.yield * 10];
                //if (data.bondNetPrice) $scope.vm.vaildBondNetPriceRange = [data.bondNetPrice * 0.75, data.bondNetPrice * 10];
                //if (data.bondFullPrice) $scope.vm.vaildBondFullPriceRange = [data.bondFullPrice * 0.75, data.bondFullPrice * 10];
                
                if (data.yield && bplaDataDefine.vaildRule['vm.responseDto.yield'])
                    bplaDataDefine.vaildRule['vm.responseDto.yield'].param = [data.yield * 0.75, data.yield * 10];
                
                if (data.bondNetPrice && bplaDataDefine.vaildRule['vm.responseDto.bondNetPrice'])
                    bplaDataDefine.vaildRule['vm.responseDto.bondNetPrice'].param = [data.bondNetPrice * 0.75, data.bondNetPrice * 10];
                
                if (data.bondFullPrice && bplaDataDefine.vaildRule['vm.responseDto.bondFullPrice'])
                    bplaDataDefine.vaildRule['vm.responseDto.bondFullPrice'].param = [data.bondFullPrice * 0.75, data.bondFullPrice * 10];
            };
            
            function mergeDto(data) {
                // Only one point
                if (data && data.profitLossByYield) {
                    if (data.profitLossByYield.length === 1) {
                        
                        var targetPointIndex = undefined;
                        
                        $scope.vm.responseDto.profitLossByYield.forEach(function (item, index) {
                            if (item.yield === data.profitLossByYield[0].yield) {
                                targetPointIndex = index;
                            }
                        });
                        
                        if (targetPointIndex >= 0) {
                            $scope.vm.responseDto.profitLossByYield[targetPointIndex] = data.profitLossByYield[0];
                            
                            $scope.vm.selectChartPoint = data.profitLossByYield[0];
                            $scope.vm.selectChartPointIndex = targetPointIndex;
                            
                            $scope.vm.selectChartPoint.alterColumn1 = $scope.vm.responseDto.bondCode;
                            $scope.vm.selectChartPoint.alterColumn2 = $scope.vm.selectedFutureContract && $scope.vm.selectedFutureContract.tfId;
                        }
                        
                        delete data.profitLossByYield;
                    } else {
                        angular.merge($scope.originVM.responseDto, data);
                    }
                }
                
                angular.merge($scope.vm.responseDto, data);
            };
            
            function changeFuturePrice(isRefresh) {
                
                // if (!$scope.vm.responseDto.futurePrice) return;
                
                for (var prop in $scope.vm.responseDto) {
                    if ($scope.vm.responseDto.hasOwnProperty(prop)) {
                        if (checkViewModel(prop)) continue;
                        else return;
                    }
                }
                
                if (!checkViewModel("impactCost")) return;
                if (!checkViewModel("bondNominalPrincipal")) return;
                
                if (!commonService.checkViewModel($scope, bplaDataDefine.newVaildRule.post_update_futures_price)) return;
                
                $scope.viewBusy(true);
                
                var dataDefine = angular.copy(bplaDataDefine.requestDto);
                
                //dataDefine.bondCode = "responseDto.bondCode";
                //dataDefine.ctdBondCode = "responseDto.ctdBondCode";
                bplaService.changeFuturePrice(dataDefine, bplaDataDefine.requestDtoVaildRule.post_update_futures_price, $scope.vm, function (data) {
                    
                    mergeDto(data);
                    
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
            
            function checkViewModel(property) {
                
                function checkRange(value, range) {
                    if (!range) return true;
                    
                    if (value <= range[0] || value >= range[1]) {
                        return false;
                    }
                    
                    return true;
                };
                
                var vaildRule = bplaDataDefine.vaildRule[property];
                if (vaildRule) {
                    if (vaildRule.type === 'range') {
                        if (!checkRange(commonService.getPropertyX($scope, property), vaildRule.param)) {
                            commonService.setPropertyX($scope, property, commonService.getPropertyX($scope, property.replace('vm.', 'originVM.')));
                            
                            commonService.commonConfirmDialog($scope, vaildRule.message, undefined, undefined, undefined, { hideCancel: true });
                            
                            return false;
                        }
                    }
                }
                
                switch (property) {
                    case "impactCost":
                        if ($scope.vm.impactCost !== NaN && $scope.vm.impactCost !== undefined) {
                            if ($scope.vm.impactCost > ($scope.vm.responseDto.yield * 100)) {
                                $scope.vm.impactCost = +numberFilter($scope.vm.responseDto.yield * 100, 3);
                                commonService.commonConfirmDialog($scope, "冲击成本须小于当前收益率， 请重新输入。", undefined, undefined, undefined, { hideCancel: true });
                                return false;
                            }
                            
                            if ($scope.vm.impactCost < 0) {
                                commonService.commonConfirmDialog($scope, "冲击成本须大于或等于零。", undefined, undefined, undefined, { hideCancel: true });
                                return false;
                            }
                        } else {
                            $scope.vm.impactCost = 0;
                            commonService.commonConfirmDialog($scope, "冲击成本须大于或等于零。", undefined, undefined, undefined, { hideCancel: true });
                            return false;
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
                            return false;
                        }
                    default:
                        break;
                }
                
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
                    case 'impactCost':
                        if (!$scope.vm.impactCost || $scope.vm.impactCost < 0) {
                            $scope.vm.impactCost = 0;
                        }
                    default:
                        if (e && e.keyCode !== 13) return;
                        
                        if (!checkViewModel(funcName)) return;
                        break;
                }
                
                $scope.doCalCulate(funcName);
                
                saveViewModel("onPropForCalculateChange");
            };
            
            var pendingChangingHandler = undefined;
            
            $scope.applyCurrencyFormat = function (prop) {
                if (pendingChangingHandler) clearTimeout(pendingChangingHandler);
                
                pendingChangingHandler = setTimeout(function () {
                    // 本金格式转换
                    var v = commonService.getPropertyX($scope, prop);
                    
                    commonService.setPropertyX($scope, prop, v + 1);
                    commonService.safeApply($scope);
                    commonService.setPropertyX($scope, prop, v);
                    commonService.safeApply($scope);
                }, 1000);
            };
            
            // 重置画面数据 恢复默认
            $scope.resetViewModel = function () {
                // $scope.vm = angular.copy($scope.originVM);
                if ($scope.vm.responseDto) {
                    if (!commonService.checkViewModel($scope, bplaDataDefine.newVaildRule.post_update_futures_price)) return;
                    
                    $scope.vm.responseDto.profitLossByYield = angular.copy($scope.originVM.responseDto.profitLossByYield);
                    
                    // 恢复冲击成本额                    
                    $scope.vm.responseDto.profitLossByYield.forEach(function (item, index) {
                        $scope.vm.responseDto.profitLossByYield[index].impactAmount = 0;
                    });
                    
                    if ($scope.vm.selectChartPointIndex >= 0) {
                        $scope.vm.selectChartPoint = $scope.vm.responseDto.profitLossByYield[$scope.vm.selectChartPointIndex];
                    } else {
                        $scope.vm.selectChartPoint = undefined;
                    }
                    
                    $scope.vm.responseDto.futureNumber = angular.copy($scope.originVM.responseDto.futureNumber);
                    
                    // 设置右侧画面初始值
                    // $scope.vm.yieldType = "ofr";
                    $scope.vm.selectedHedge = "SHORT_BASIS";
                    $scope.vm.impactCost = 0;
                    $scope.vm.bondNominalPrincipal = 10000000;
                }
                
                $scope.doCalCulate("reset");
                // $scope.onVmChanged4Chart && $scope.onVmChanged4Chart();
                
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
                
                //var value = commonService.setPropertyX($scope.test, "responseDto.yield.a.b.c", 123456);
                
                //console.log("Value: " + JSON.stringify(value));
                
                $scope.vm.selectChartPoint = undefined;
                
                $scope.viewBusy(true);
                
                bplaService.doCalCulate(bplaDataDefine.requestDto, bplaDataDefine.requestDtoVaildRule.post_do_calculator, $scope.vm, function (data) {
                    
                    if (bplaDataDefine.actionMap.whenDoCalCulateCb[updateProp]) {
                        bplaDataDefine.actionMap.whenDoCalCulateCb[updateProp](data);
                    }
                    
                    if (!data) return;
                    
                    // 期货手数只反映到画面，不保存为原始值
                    $scope.vm.responseDto.futureNumber = data.futureNumber;
                    delete data.futureNumber;
                    
                    // 不处理期货价格的最后更新时间
                    delete data.futureLastUpdateTime;
                    
                    mergeDto(data);
                    
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
                    
                    mergeDto(data);
                    
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
                
                if (!$scope.vm.impactCost) $scope.vm.impactCost = 0;
                
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
                        
                        if (!data.selectedFutureContract) {
                            $state.go(routeConst.basis_pndl_analysis, { tfId: undefined });
                            $scope.viewBusy(false);
                            return;
                        }
                        
                        // 设置默认值
                        if (!data.openPositionDate) data.openPositionDate = new Date().formatDate("yyyy-MM-dd");
                        if (!data.closePositionDate) data.closePositionDate = data.selectedFutureContract.maturityDate;
                        
                        if (!data.selectedRepoRateType && data.repoRateDto) {
                            data.selectedRepoRateType = commonService.findFromArrayBy(data.repoRateTypes, data.repoRateDto.code, "code");
                            data.selectedRepoRateType.capitalCost = bplaService.getCapitalCost(data.selectedRepoRateType);
                        }
                        
                        // 设置右侧画面初始值
                        if (!data.yieldType) data.yieldType = "ofr";
                        if (!data.selectedHedge) data.selectedHedge = "SHORT_BASIS";
                        if (!data.impactCost) data.impactCost = 0;
                        if (!data.bondNominalPrincipal) data.bondNominalPrincipal = 10000000;
                        
                        console.log("mainCtrl $scope.id: " + $scope.$id);
                    } else {
                        if (data.responseDto && data.responseDto.futureContract)
                            $state.go(routeConst.basis_pndl_analysis, { tfId: data.responseDto.futureContract.tfId });
                        return;
                    }
                    
                    $scope.originVM = data;
                    
                    $scope.vm = angular.copy(data);
                    
                    if (data && data.responseDto) {
                        // 现券与CTD相同时，禁止修改收益率及相关信息
                        $scope.vm.isYieldEditable = (data.responseDto.bondCode !== data.responseDto.ctdBondCode);
                        
                        updateVaildRule(data.responseDto);
                    }
                    
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
                            
                            //if ($scope.vm && $scope.vm.futureContracts && $scope.vm.futureContracts instanceof Array) {
                            //    if (!$scope.vm.futureContracts.ifAll(function(item) {
                            //        return new Date(item.deliveryDate) >= new Date();
                            //    })) {
                            //        $state.go(routeConst.basis_pndl_analysis, { tfId: undefined });
                            //        return;
                            //    }
                            //} else {
                            //    $state.go(routeConst.basis_pndl_analysis, { tfId: undefined });
                            //    return;
                            //}
                            
                            // Apply Viewmodel
                            $scope.vm.selectedFutureContract = commonService.findFromArrayBy($scope.vm.futureContracts, $state.params.tfId, "tfId");

                            var yesterday = new Date();
                            yesterday.setDate(yesterday.getDate() - 1);
                            
                            if (!$scope.vm.selectedFutureContract || new Date($scope.vm.selectedFutureContract) < yesterday) {
                                $state.go(routeConst.basis_pndl_analysis, { tfId: undefined });
                                return;
                            }
                            
                            updateVaildRule($scope.originVM.responseDto);
                            
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

