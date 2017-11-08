/**
 * Create by WeiLai on 03/14/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'basis_pndl_analysis.mainModule'
], function (mainModule) {
    'use strict';
    
    var moduleDef = [
        '$scope', '$injector', '$sce', '$state', '$filter', 'commonService', 'bplaService', 'routeConst',
        function ($scope, $injector, $sce, $state, $filter, commonService, bplaService, routeConst) {
            
            var numberFilter = $filter('number');
            
            // $scope.vm.selectedRepoRateType.capitalCost = getCapitalCost(+$scope.vm.selectedRepoRateType.price, +$scope.vm.selectedRepoRateType.bpValue);
            function getCapitalCost(val1, val2) {
                //if (val1 && val2) {
                //    return +numberFilter(val1 / 100 + val2, 6);
                //} else if (!val1 && val2) {
                //    return val2;
                //} else if (val1 && !val2) {
                //    return +numberFilter(val1 / 100, 6);
                //} else {
                //    return undefined;
                //}
                
                if (!val1) val1 = 0;
                if (!val2) val2 = 0;
                
                return +numberFilter(val1 / 100 + val2, 6);
            };
            
            // 资金成本选择
            $scope.onSelectedRepoRateTypeChange = function () {
                if ($scope.vm.selectedRepoRateType) {
                    $scope.vm.selectedRepoRateType.bpValue = 0;
                    
                    $scope.vm.selectedRepoRateType.capitalCost = bplaService.getCapitalCost($scope.vm.selectedRepoRateType);
                }
                
                $scope.onPropForCalculateChange && $scope.onPropForCalculateChange();
            };
            
            var paddingCalculateHandler = undefined;
            
            // 资金成本： others BP值变更
            $scope.onBpValueChange = function () {
                if ($scope.vm.selectedRepoRateType) {
                    $scope.vm.selectedRepoRateType.capitalCost = bplaService.getCapitalCost($scope.vm.selectedRepoRateType);
                }
                
                if (paddingCalculateHandler) clearTimeout(paddingCalculateHandler);
                
                paddingCalculateHandler = setTimeout(function () {
                    
                    $scope.onPropForCalculateChange && $scope.onPropForCalculateChange();
                }, 1000);
            };
            
            $scope.onFutureContract = function () {
                if ($scope.updateChart2) $scope.updateChart2({
                    key : "futureContract", value: $scope.vm.selectedFutureContract.tfId
                });
            };
            
            // 期货合约 Change
            $scope.onFutureContractChange = function () {
                // commonService.setProppertyX($scope, "vm.selectChartPoint.alterColumn2", $scope.vm.selectedFutureContract.tfId);
                
                // location.href = "#/basis_pndl_analysis/" + $scope.vm.selectedFutureContract.tfId;
                
                // $location.url("/basis_pndl_analysis/" + $scope.vm.selectedFutureContract.tfId);
                
                $state.go(routeConst.basis_pndl_analysis, { tfId: $scope.vm.selectedFutureContract.tfId });
            };
            
            // 现券 Change
            $scope.onBondCodeChange = function () {
                // $scope.showBondCodeDropdown = true;

                if($("#bondCodeDropdown.open").length === 0 && $scope.vm.responseDto.bondList && $scope.vm.responseDto.bondList.length > 0) $("#bondCodeDropdown input.dropdown-toggle").click();
                
                if (!$scope.vm || !$scope.vm.responseDto || !$scope.vm.responseDto.bondCode) return;
                
                if ($scope.vm.responseDto.bondCode.length === 4) {
                    $scope.vm.responseDto.bondList = function () {
                        bplaService.getBondList($scope.vm.responseDto.bondCode, function (data) {
                            $scope.vm.responseDto.bondList = data;
                            
                            // 自动打开下拉列表
                            if ($("#bondCodeDropdown.open").length === 0) $("#bondCodeDropdown input.dropdown-toggle").click();
                        });
                    }();
                    
                    return;
                }
                
                var selected = commonService.findFromArrayBy($scope.vm.responseDto.bondList, $scope.vm.responseDto.bondCode, "bondCode");
                
                if (selected) {
                    $scope.vm.responseDto.bondKey = selected.bondKey;
                    $scope.vm.responseDto.bondCode = selected.bondCode;
                    $scope.vm.responseDto.bondListedMarket = selected.listedMarket;
                    
                    // ofr bid 按钮事件相同
                    if ($scope.doCalCulate) $scope.doCalCulate("bondCode");
                }

                // commonService.setProppertyX($scope, "vm.selectChartPoint.alterColumn1", $scope.vm.responseDto.bondCode);

                //if ($scope.updateChart2) $scope.updateChart2({
                //    key : "bondCode", value: $scope.vm.responseDto.bondCode
                //});
            };
            
            $scope.onBondCodeBlur = function () {
                // $scope.showBondCodeDropdown = false;
                
            };
            
            $scope.onBondCodeDropdownSelected = function (e) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                
                if (obj.nodeName === "LI" || obj.nodeName === "SPAN") {
                    var attr = obj.getAttribute("value");
                    
                    var selected = commonService.findFromArrayBy($scope.vm.responseDto.bondList, attr, "bondCode");
                    if (selected) {
                        $scope.vm.responseDto.bondKey = selected.bondKey;
                        $scope.vm.responseDto.bondCode = selected.bondCode;
                        $scope.vm.responseDto.bondListedMarket = selected.listedMarket;
                        
                        // ofr bid 按钮事件相同
                        if ($scope.doCalCulate) $scope.doCalCulate("bondCode");
                    }
                }

                // $scope.showBondCodeDropdown = false;
            };
            
            $scope.onBondCodeKeydown = function (e) {
                var dd = $(e.currentTarget).parent();
                var current = dd.find("li.active");

                if (e) {
                    switch (e.keyCode) {
                        case 13:
                            if (current.length > 0) {
                                var attr = current[0].getAttribute("value");
                                
                                var selected = commonService.findFromArrayBy($scope.vm.responseDto.bondList, attr, "bondCode");
                                if (selected) {
                                    $scope.vm.responseDto.bondKey = selected.bondKey;
                                    $scope.vm.responseDto.bondCode = selected.bondCode;
                                    $scope.vm.responseDto.bondListedMarket = selected.listedMarket;
                                    
                                    // ofr bid 按钮事件相同
                                    if ($scope.doCalCulate) $scope.doCalCulate("bondCode");
                                }

                                current.removeClass("active");
                            }
                            if ($("#bondCodeDropdown.open").length !== 0) $("#bondCodeDropdown input.dropdown-toggle").click();
                            // $scope.showBondCodeDropdown = false;
                            break;
                        case 38:
                            if (current.length === 0) {
                                dd.find("li:last-child").toggleClass("active");
                            } else {
                                current = dd.find("li.active");

                                current.toggleClass("active");
                                current.prev().toggleClass("active");
                            }
                            break;
                        case 40:
                            if (current.length === 0) {
                                dd.find("li:first-child").toggleClass("active");
                            } else {
                                current = dd.find("li.active");
                                
                                current.toggleClass("active");
                                current.next().toggleClass("active");
                            }
                            break;
                        default:
                    }
                }
            };

            $scope.onBondCodeDropdownMouseenter = function (e) {
                var active = $(e.currentTarget).parent().find("li.active");
                if (active.length > 0) active.removeClass("active");
            };

            // 页面初始化
            var init = function () {
                
                $scope.debug = function () {
                    
                    var vm = $scope.vm;
                    
                    console.log("bplaFormCtrl vm: " + vm);
                };
                
                console.log("bplaFormCtrl $scope.id: " + $scope.$id);
            }();
           
        }];
    
    mainModule.controller('basis_pndl_analysis.bplaFormCtrl', [
        '$scope', '$injector', '$sce', '$state', '$filter', 'commonService', 'bplaService', 'routeConst',
        function ($scope, $injector, $sce, $state, $filter, commonService, bplaService, routeConst) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$state': $state,
                '$filter': $filter,
                'commonService': commonService,
                'bplaService': bplaService,
                'routeConst': routeConst
            });
        }
    ]);
});

