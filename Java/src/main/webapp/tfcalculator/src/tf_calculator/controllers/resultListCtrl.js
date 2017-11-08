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
                exportExcel: {
                    tfID: "selectedFuture.tfId",
                    bondCode: "selectedBond.bondCode",
                    futurePrice: "selectedFuture.futurePrice",
                    bondRate: "selectedBond.yield",
                    bondRateByAddDay: "selectedBond.yieldByDay",

                    bondNetPrice: "selectedBond.netPrice",
                    convertionFactor: "selectedBond.convertionFactor",
                    irr: "calculationResult.calculationMainResult.irr",
                    basis: "calculationResult.calculationMainResult.basis",
                    netBasis: "calculationResult.calculationMainResult.netBasis",
                    trialTime: "addedDatetime",

                    tradingDate: "selectedFuture.startDate",
                    deliveryDate: "selectedFuture.maturityDate",
                    capitalCost: "capitalCost",
                    bondFullPrice: "selectedBond.fullPrice",
                    carry: "calculationResult.carry",
                    receiptPrice: "calculationResult.receiptPrice",
                    futureSpotSpread: "calculationResult.futureSpotSpread",
                    interestsRateSpread: "calculationResult.interestsRateSpread",
                    interestOnSettlementDate: "calculationResult.interestOnSettlementDate",
                    interestOnTradingDate: "calculationResult.interestOnTradingDate",
                    interestDuringHolding: "calculationResult.interestDuringHolding",
                    weightedAverageInterest: "calculationResult.weightedAverageInterest"
    }
            }
        };
    }();
    
    var moduleDef = ['$scope', '$injector', '$state', '$location', '$filter', '$http', 'httpService', 'commonService', 'tcService', 'servicePathConst',
        function ($scope, $injector, $state, $location, $filter, $http, httpService, commonService, tcService, servicePathConst) {
            
            // 选择全部
            $scope.onChangeResultListSelectedAll = function () {
                if (!$scope.resultList || $scope.resultList.length <= 0 || !($scope.resultList instanceof Array)) return;
                
                $scope.resultList.forEach(function (item, index) {
                    item.isSelected = $scope.vm.isResultListSelectedAll;
                });
            };
            
            // 删除所选
            $scope.onClickResultListDeleteItem = function (event) {
                if (!$scope.resultList || $scope.resultList.length === 0 || !($scope.resultList instanceof Array)) return;

                var result = $scope.resultList.findWhere(function (item, index) {
                    return item.isSelected;
                });
                
                if (!result || result.length === 0 || !(result instanceof Array)) {
                    commonService.commonErrorDialog($scope, undefined, "请勾选至少一条记录再執行刪除。");
                    return;
                }

                $scope.resultList = $scope.resultList.findWhere(function (item, index) {
                    return !item.isSelected;
                });
                
                $scope.vm.isResultListSelectedAll = false;
            };
            
            // Export Excel
            $scope.onClickExportExcel = function (event) {
                if (!$scope.resultList || $scope.resultList.length <= 0 || !($scope.resultList instanceof Array)) {
                    commonService.commonErrorDialog($scope, undefined, "列表中没有保存的计算结果");
                    return;
                }

                var selectItems = $scope.resultList.findWhere(function(item) {
                    return item.isSelected;
                });
                
                if (!selectItems || selectItems.length <= 0 || !(selectItems instanceof Array)) {
                    commonService.commonErrorDialog($scope, undefined, "请勾选至少一条记录再导出。");
                    return;
                }
                
                var dto = selectItems.map(function (item) {
                    var dtoDefine = angular.copy(tcDataDefine.dto.exportExcel);

                    // if ($scope.vm.selectedCalTgtType.value !== $scope.calTgtTypeButtons[1].value) delete dtoDefine.bondRateByAddDay;         

                    var itemDto = commonService.getDto(item, dtoDefine);
                    if (itemDto && itemDto.trialTime && itemDto.trialTime.constructor.name === "Date") itemDto.trialTime = itemDto.trialTime.formatDate($scope.DATETIME_FORMAT);
                    return itemDto;
                }).findWhere(function (item) {
                    return item !== undefined;
                });
                
                $scope.viewBusy(true);
                
                var fullUrl = servicePathConst.service_api_root ? servicePathConst.service_api_root + servicePathConst.tc_post_excel_export : servicePathConst.tc_post_excel_export;
                
                $http({
                    url: fullUrl,
                    method: 'post',
                    responseType: 'arraybuffer', 
                    data: JSON.stringify(dto)
                }).success(function (data, status, headers) {
                    $scope.viewBusy(false);
                    
                    if (status !== 200) {
                        commonService.commonErrorDialog($scope, undefined, "导出Excel文件失败");
                        return;
                    }                    

                    // application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
                    // window.open(servicePathConst.service_api_root + '/' + data);

                    var blob = new Blob([data], { type: "application/vnd.ms-excel;charset=UTF-8" }, true);
                    // saveAs(blob, "Excel.xls");
                    saveAs(blob, "TF计算器{0}.xls".format(new Date().formatDate('yyyyMMdd')));

                    commonService.commonErrorDialog($scope, undefined, "导出Excel文件成功，请于下载文件夹下查看。");
                }).error(function (data, status) {
                    $scope.viewBusy(false);
                    commonService.commonErrorDialog($scope, undefined, "导出Excel文件失败");
                });
                
            };

        }];
    
    
    mainModule.controller('tc.resultListCtrl', [
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