/**
 * Create by WeiLai on 05/27/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'tc.mainModule'
], function (mainModule) {
    'use strict';

    var dataDefine = function () {
        return {
            dto: {
                postFutureInitCalculation: {
                    targetBonds: {},
                    bondPrice: {},
                    bondInfo: {},

                    calculationMainRequest: {},

                    futureContract: {
                        tfId: "selectedFuture.tfId"
                    },

                    capitalCost: "capitalCost"
                },

                refreshFuturePrice: {
                    GetFuturePriceRequestDto: {
                        calculationMainRequest: {
                            calculationType: "selectedCalTgtType.value"
                        },
                        targetBonds: "selectedBondType.value",
                        futureContract: {
                            tfId: "selectedFuture.tfId",
                            tfKey: "selectedFuture.tfKey",
                            tradingDate: "selectedFuture.startDate",
                            deliveryDate: "selectedFuture.maturityDate"
                        },
                        capitalCost: "capitalCost",

                        scheduleBondId: "selectedBond.id",
                        bondYield: "selectedBond.yield",

                        bondInfo: {
                            bondKey: "selectedBond.bondKey",

                            listedMarket: "selectedBond.listedMarket",
                            // convertionFactor: "selectedBond.convertionFactor",

                            fixedCoupon: "selectedBond.fixedCoupon",
                            paymentFrequency: "selectedBond.paymentFrequency.value",
                            interestStartDate: "selectedBond.interestStartDate",
                            maturityDate: "selectedBond.maturityDate"
                        },
                        fixedCoupon: "selectedBond.fixedCoupon"
                    },

                    BPMainRequestDto: {
                        irr: "calculationResult.calculationMainResult.irr"
                    }
                },

                temp: {
                    "GetFuturePriceRequestDto": {},
                    "BPMainRequestDto": { "irr": 9.36 }
                }
            },



            vaildRule: {
                refreshFuturePrice: [
                    { prop: "vm.selectedBond", rule: "required", errorMessage: "债券信息不能为空" },

                    { prop: "vm.selectedBond.yield", rule: "required", displayName: "收益率", errorMessage: "请输入收益率" },
                    { prop: "vm.selectedBond.yield", rule: "regexp", displayName: "收益率", errorMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", param: { pattern: /^(-?\d{0,2})$|^(-?\d{0,2}.\d{1,4})$/ } },
                    { prop: "vm.selectedBond.yield", rule: "rangeOpen", displayName: "收益率", errorMessage: "收益率输入错误，正确格式： 00.0000, 最大值100%，最小值-100%", param: { max: 100, min: -100 } },

                    {
                        prop: [
                            "vm.calculationResult.calculationMainResult.irr",
                            "vm.calculationResult.calculationMainResult.basis",
                            "vm.calculationResult.calculationMainResult.netBasis"
                        ], rule: "requiredAny", errorMessage: "IRR、基差、净基差信息不能全部为空"
                    }
                ]
            },
        };

    }();


    var moduleDef = ['$scope', '$injector', '$state', '$location', '$filter', 'commonService', 'httpService', 'tcService', 'servicePathConst',
        function ($scope, $injector, $state, $location, $filter, commonService, httpService, tcService, servicePathConst) {

            function callChangeFutureService() {
                $scope.viewBusy(true);

                $scope.vm.selectedBondType = $scope.bondTypeButtons[0];
                tcService.changeFuture($scope, function () {

                    $scope.viewBusy(false);
                }, function () {
                    $scope.viewBusy(false);
                });
            };

            $scope.getDateDiff = function (startTime, endTime, diffType) {
                if (startTime && startTime.constructor.name === "Date") startTime = startTime.formatDate($scope.DATE_FORMAT);
                if (endTime && endTime.constructor.name === "Date") endTime = endTime.formatDate($scope.DATE_FORMAT);

                return commonService.getDateDiff(startTime, endTime, diffType);
            }

            $scope.isWarnDatetime = function (value) {
                return commonService.getDateDiff(value, new Date().formatDate($scope.DATE_FORMAT), "minute") >= 60;
            };

            // 期货改变
            $scope.onChangeFuture = function () {
                if ($scope.originFutures) {

                    var originFuture = $scope.originFutures.findItem(function (e) { return e.tfKey === $scope.vm.selectedFuture.tfKey; });

                    if (originFuture) {
                        $scope.vm.selectedFuture.maturityDate = originFuture.maturityDate;
                        $scope.vm.selectedFuture.startDate = originFuture.startDate;
                    }
                }

                callChangeFutureService();
            };

            // 恢复默认
            $scope.resetViewModel = function (event) {
                if (!$scope.originVM) {
                    location.reload();
                    return;
                }

                if (!$scope.vm || !$scope.vm.selectedCalTgtType) return;

                // <期货合约>：用户选择的合约

                // <交易日> = 今天，若今天不是交易日，则往后顺延
                // <交割日> = 期货合约交割日，从数据库取
                if ($scope.originVM.futures && $scope.originVM.futures.length > 0 && $scope.originVM.futures instanceof Array && $scope.vm.selectedFuture) {
                    var originFuture = $scope.originVM.futures.findItem(function (e) {
                        return e.tfKey === $scope.vm.selectedFuture.tfKey;
                    });

                    if (originFuture) angular.merge($scope.vm.selectedFuture, originFuture);
                }

                function loadOriginFuture() {
                    if (!$scope.vm.selectedFuture || !$scope.originVM.selectedFuture) return;

                    angular.merge($scope.vm.selectedFuture, angular.copy($scope.originVM.selectedFuture));
                };

                function loadOriginBond() {
                    if (!$scope.vm.selectedBond || !$scope.originVM.selectedBond) return;

                    delete $scope.originVM.selectedBond.paymentFrequency;

                    angular.merge($scope.vm.selectedBond, angular.copy($scope.originVM.selectedBond));
                };

                // <资金成本>：用户设置的资金成本
                switch ($scope.vm.selectedCalTgtType.value) {
                    case "IRR_BASE":
                        // <期货价格>：<期货合约>的最新价，取last, 若收盘则取close
                        // <最后更新>：<期货合约>的最新价的时间戳
                        loadOriginFuture();

                        // <债券代码>：<期货合约>的CTD，CTD的计算方法请见本文档3.2.1
                        // <收益率>：计算CTD时所用到的那个Ofr收益率
                        // <债券净价>：根据<收益率>计算，QDP计算
                        // <债券全价>：根据<收益率>计算，QDP计算
                        loadOriginBond();
                        break;
                    case "BOND_PRICE":
                        // <期货价格>：<期货合约>的最新价，取last, 若收盘则取close
                        // <最后更新>：<期货合约>的最新价的时间戳
                        loadOriginFuture();

                        // <债券代码>：<期货合约>的CTD，CTD的计算方法请见本文档3.2.1
                        // <IRR>：<期货合约>CTD的IRR，CTD的计算方法请见本文档3.2.1
                        // <基差>：<IRR>对应的基差
                        // <净基差>：<IRR>对应的净基差
                        loadOriginBond();
                        break;
                    case "FUTURE_THEORETICAL_PRICE":
                        loadOriginFuture();
                        // <最后更新>：<期货合约>的最新价的时间戳

                        // <收益率>：计算CTD时所用到的那个Ofr收益率
                        // <债券净价>：根据<收益率>计算，QDP计算
                        // <债券全价>：根据<收益率>计算，QDP计算

                        // <CTD>：<期货合约>的CTD，CTD的计算方法请见本文档3.2.1
                        loadOriginBond();
                        break;
                    case "FUTURE_ANALYSIS":
                        loadOriginFuture();
                        // <债券代码>：<期货合约>的CTD，CTD的计算方法请见本文档3.2.1

                        // <收益率>：计算CTD时所用到的那个Ofr收益率
                        // <债券净价>：根据<收益率>计算，QDP计算
                        // <债券全价>：根据<收益率>计算，QDP计算

                        // <IRR>：<期货合约>CTD的IRR，CTD的计算方法请见本文档3.2.1
                        // <基差>：<IRR>对应的基差
                        // <净基差>：<IRR>对应的净基差
                        loadOriginBond();
                        break;
                    default:
                        break;
                }

                if ($scope.vm.selectedBondType) {
                    switch ($scope.vm.selectedBondType.value) {
                        case "DELIVABLE_BONDS": break;
                        case "SCHEDULED_BONDS": break;
                        case "SELECTED_BOND": break;
                        case "VIRTUAL_BOND": {

                            [
                                "vm.selectedBond.interestStartDate",
                                "vm.selectedBond.maturityDate",
                                "vm.selectedBond.fixedCoupon",
                                "vm.selectedBond.convertionFactor",
                                "vm.selectedBond.yield",
                                "vm.selectedBond.netPrice",
                                "vm.selectedBond.fullPrice",
                                "vm.selectedBond.yieldByDay"
                            ].forEach(function (item) {
                                commonService.setPropertyX($scope, item, undefined);
                            });
                            return;
                        }
                        default:

                            break;
                    }
                }

                if (!$scope.vm.selectedYieldType) $scope.vm.selectedYieldType = $scope.yieldTypeButtons[0];

                commonService.setPropertyX($scope, "vm.calculationResult.calculationMainResult", angular.copy(commonService.getPropertyX($scope, "originVM.calculationResult.calculationMainResult")));

                $scope.viewBusy(true);
                tcService.doCalCulate($scope, function () {
                    // if ($scope.onClickRefreshFuturePrice) $scope.onClickRefreshFuturePrice();

                    $scope.viewBusy(false);
                }, function () {
                    $scope.viewBusy(false);
                });
            };

            $scope.onClickRefreshFuturePrice = function (event) {
                if (!$scope.vm) return;

                if (!commonService.checkViewModel($scope, dataDefine.vaildRule.refreshFuturePrice)) return;

                $scope.viewBusy(true);

                var dto = commonService.getDto($scope.vm, dataDefine.dto.refreshFuturePrice, $scope.DATE_FORMAT);

                httpService.postService(servicePathConst.tc_post_get_future_price, dto, function (data) {
                    $scope.viewBusy(false);

                    angular.merge($scope.vm.calculationResult, data);

                    if (!$scope.vm.selectedFuture) $scope.vm.selectedFuture = {};
                    $scope.vm.selectedFuture.futurePrice = data.futurePrice;
                    $scope.vm.selectedFuture.lastUpdateDatetime = data.lastFutureUpdateDate;

                    if ($scope.displayWhen) {
                        if ($scope.displayWhen([[1, 2]])) {
                            if ($scope.onChangeBond) $scope.onChangeBond();
                        }
                    }

                }, function (data) {
                    $scope.viewBusy(false);

                    commonService.commonErrorDialog($scope, data, "获取最新期货价格失败");
                });
            };
        }];


    mainModule.controller('tc.headerCtrl', [
        '$scope', '$injector', '$sce', '$location', '$filter', 'commonService', 'tcService',
        function ($scope, $injector, $sce, $location, $filter, commonService, tcService) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$location': $location,
                '$filter': $filter,
                'commonService': commonService,
                'tcService': tcService
            });
        }
    ]);
});