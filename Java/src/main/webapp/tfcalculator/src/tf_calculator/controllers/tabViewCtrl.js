/**
 * Create by WeiLai on 05/27/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'tc.mainModule'
], function (mainModule) {
    'use strict';
    
    var tcDataDefine = function () {
        return {
            dto: {
                changeBond: {
                    bondChangedRequestDto: {
                        futureContract: {
                            tfId: "selectedFuture.tfId",
                            tradingDate: "selectedFuture.startDate",
                            deliveryDate: "selectedFuture.maturityDate"
                        },
                        capitalCost: "capitalCost",
                        bondKey: "selectedBond.bondKey",
                        listedMarket: "selectedBond.listedMarket",
                        
                        bondId: "selectedBond.id",
                        targetBonds: "selectedBondType.value",
                        
                        calculationMainRequest: {
                            calculationType: "selectedCalTgtType.value"
                        }
                    },
                    IRRMainRequestDto: {
                        futurePrice: "selectedFuture.futurePrice"
                    }
                },
                
                changeBond_bondPrice: {
                    BPMainRequestDto: {
                        futurePrice: "selectedFuture.futurePrice",
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                },
                
                changeBond_futureAnalysis: {
                    FSAMainRequestDto: {
                        irr: "calculationResult.calculationMainResult.irr",
                        basis: "calculationResult.calculationMainResult.basis",
                        netBasis: "calculationResult.calculationMainResult.netBasis"
                    }
                }
            }
        };
    }();
    
    var moduleDef = ['$scope', '$injector', '$state', '$location', '$filter', 'httpService', 'commonService', 'tcService', 'servicePathConst',
        function ($scope, $injector, $state, $location, $filter, httpService, commonService, tcService, servicePathConst) {
            
            // 保存结果        
            $scope.onClickSaveResult = function (event) {
                if (!$scope.resultList) $scope.resultList = [];
                
                if ($scope.resultList.length === 100) {
                    commonService.commonErrorDialog($scope, undefined, "计算结果不能超过100条，请删除后再保存。");
                    return;
                }
                
                var tempVm = angular.copy($scope.vm);
                
                if (tempVm.selectedBondType) {
                    switch (tempVm.selectedBondType.value) {
                        case $scope.bondTypeButtons[0].value: break;
                        case $scope.bondTypeButtons[1].value:
                            if (tempVm.selectedBond && tempVm.selectedBond.issueStartDate)
                                tempVm.selectedBond.bondCode = $scope.bondTypeButtons[1].display + tempVm.selectedBond.issueStartDate.formatDate("yy/MM");
                            break;
                        case $scope.bondTypeButtons[2].value: break;
                        case $scope.bondTypeButtons[3].value: break;
                        
                        default:
                    }
                }
                
                console.debug(tempVm);
                
                tempVm.addedDatetime = new Date();
                
                if (tempVm && tempVm.selectedFuture) {
                    if (tempVm.selectedFuture.startDate.constructor.name === "Date")
                        tempVm.selectedFuture.startDate = tempVm.selectedFuture.startDate.formatDate($scope.DATE_FORMAT);
                    if (tempVm.selectedFuture.maturityDate.constructor.name === "Date")
                        tempVm.selectedFuture.maturityDate = tempVm.selectedFuture.maturityDate.formatDate($scope.DATE_FORMAT);
                }
                
                $scope.resultList.push(tempVm);
            };
            
            // 修改债券代码
            $scope.onChangeBond = function (selectedBond) {
                if (!$scope.vm) return;

                if (selectedBond) $scope.vm.selectedBond = selectedBond;

                if (!$scope.vm.selectedBond) return;
                
                var dto = commonService.getDto($scope.vm, tcDataDefine.dto.changeBond, $scope.DATE_FORMAT);
                
                if ($scope.vm && $scope.vm.selectedCalTgtType) {
                    if ($scope.vm.selectedCalTgtType.value === $scope.calTgtTypeButtons[1].value) {
                        angular.merge(dto, commonService.getDto($scope.vm, tcDataDefine.dto.changeBond_bondPrice, $scope.DATE_FORMAT));
                    } else if ($scope.vm.selectedCalTgtType.value === $scope.calTgtTypeButtons[2].value ||
                                $scope.vm.selectedCalTgtType.value === $scope.calTgtTypeButtons[3].value) {
                        angular.merge(dto, commonService.getDto($scope.vm, tcDataDefine.dto.changeBond_futureAnalysis, $scope.DATE_FORMAT));
                        delete dto.IRRMainRequestDto;
                    }
                }
                
                $scope.viewBusy(true);
                httpService.postService(servicePathConst.tc_post_bond_changed, dto, function (data) {
                    $scope.viewBusy(false);
                    
                    if (!data) {
                        commonService.commonErrorDialog($scope, undefined, "获取所选债券数据失败");
                        return;
                    }

                    if (!$scope.vm.calculationResult) $scope.vm.calculationResult = {};
                    
                    angular.merge($scope.vm.calculationResult, data);
                    angular.merge($scope.vm.selectedBond, data.bondPrice);
                    angular.merge($scope.vm.selectedBond, data.calculationMainResult);
                    
                    $scope.vm.selectedYieldType = $scope.yieldTypeButtons.findItem(function (e) {
                        return e.value === $scope.vm.selectedBond.yieldType;
                    });

                    var futurePrice = commonService.getPropertyX(data, "calculationMainResult.futurePrice");
                    if(futurePrice) commonService.setPropertyX($scope, "vm.selectedFuture.futurePrice", futurePrice);                    
                    
                    if (data.bondConvertionFactor) $scope.vm.selectedBond.convertionFactor = data.bondConvertionFactor;
                    
                    $scope.originVM = angular.copy($scope.vm);
                }, function (data) {
                    
                    $scope.viewBusy(false);
                    
                    if ($scope.displayWhen) {
                        if ($scope.displayWhen([[1, 2]])) return;
                    }
                    
                    commonService.commonErrorDialog($scope, data, "获取所选债券数据失败");
                });
            };
            
            $scope.onChangeBondForSearch = function (args) {
                console.debug("onChangeBondForSearch");
                
                if (!args || !args.newValue) return;
                
                $scope.onChangeBond(args.newValue);
            };
            
            // 自选券搜索
            $scope.onChangeBondSearchKeyword = function (event, cosEvent) {
                if (!cosEvent) return;
                
                if (!cosEvent.newValue || cosEvent.newValue.length !== 4) return;
                
                // var dto = commonService.getDto($scope.vm, tcDataDefine.dto.bondSearchDto);
                
                console.debug("onChangeBondSearchKeyword");
                
                $scope.viewBusy(true);
                httpService.postService(servicePathConst.tc_post_find_fix_interest_bonds, cosEvent.newValue, function (data) {
                    
                    if (!data || data.length < 0 || !(data instanceof Array)) return;
                    
                    data.forEach(function (item, index) {
                        item.paymentFrequency = angular.copy($scope.paymentFrequencys.findItem(function (e) {
                            return e.value === item.paymentFrequency;
                        }));
                    });
                    
                    $scope.searchedBondList = data;

                    setTimeout(function() {
                        cosEvent.target.focus();
                    }, 500);
                    
                    $scope.viewBusy(false);
                }, function (data) {
                    $scope.viewBusy(false);
                });
            };
            
            $scope.selectedBondListFormatter = function (item) {
                return "{0}    {1}".format(item.bondCode, item.shortName);
            };
        }];
    
    
    mainModule.controller('tc.tabViewCtrl', [
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