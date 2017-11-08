// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['basis_pndl_analysis.mainModule'], function (mainModule) {
    "use strict";
    
    mainModule.service('bplaService', ['$rootScope', '$location', '$filter', 'servicePathConst', 'httpService', 'commonService',
        function ($rootScope, $location, $filter, servicePathConst, httpService, commonService) {
            
            var percentFilter = $filter('percentFilter'),
                numberFilter = $filter('number'),
                currencyFilter = $filter('currency');
            
            var thisService = this;
            
            function profitLossToArray(byYieldObj) {
                var target = [];
                
                if (!byYieldObj) return target;
                
                if (byYieldObj instanceof Array) return byYieldObj;
                
                for (var prop in byYieldObj) {
                    if (!byYieldObj.hasOwnProperty(prop)) continue;
                    
                    target.push(byYieldObj[prop]);
                }
                
                return target.sort(function (x1, x2) {
                    var v1 = parseFloat(x1.yield);
                    var v2 = parseFloat(x2.yield);
                    
                    if (!v1) return -1;
                    if (!v2) return 1;
                    
                    return v1 - v2;
                });

            };
            
            // Internal Functions
            var getRequestDto = function (dataDefine, viewModel) {
                if (!dataDefine || !viewModel) return undefined;
                
                var target = angular.copy(dataDefine);
                
                for (var prop in target) {
                    if (!target.hasOwnProperty(prop)) continue;
                    
                    commonService.setProppertyX(target, prop, commonService.getPropertyX(viewModel, target[prop]));
                }
                
                target && target.futureContract && target.futureContract.$$hashKey && delete target.futureContract.$$hashKey;
                
                if (!target.curveYtm || !target.curveFuturesPrice) {
                    delete target.curveYtm;
                    delete target.curveFuturesPrice;
                }
                
                return target;
            };
            
            // 页面初始化 1 2
            this.getFuturesContractList = function (dataDefine, params, successCallback, failedCallback) {
                
                var url = params ? servicePathConst.post_futures_contract_list_by_tfId : servicePathConst.get_futures_contract_list;
                
                var func = params ? httpService.postService : httpService.getService;
                
                func(url, params, function (data) {
                    
                    data = commonService.convertDataToVM(data, dataDefine, undefined);
                    
                    if (data.futureContracts && data.futureContracts instanceof Array) {
                        data.futureContracts.forEach(function (item, index) {
                            if (item.openPositionDate) {
                                try {
                                    item.openPositionDate = new Date(item.openPositionDate);
                                } catch (e) {

                                }
                            }
                            
                            if (item.closePositionDate) {
                                try {
                                    item.closePositionDate = new Date(item.closePositionDate);
                                } catch (e) {

                                }
                            }
                        });
                    }
                    
                    //if (data.repoRateTypes && data.repoRateTypes instanceof Array) {
                    //    data.repoRateTypes.forEach(function (item, index) {
                    //        item.hide = true;
                    //    });
                    //}
                    
                    data.responseDto.profitLossByYield = profitLossToArray(data.responseDto.profitLossByYield);
                    
                    // data.responseDto.yieldString = $filter("number")(data.responseDto.yield * 100, 3);
                    
                    successCallback(data);
                }, failedCallback);
            };
            
            // 修改期货价格 4
            this.changeFuturePrice = function (dataDefine, vaildMessage, viewModel, successCallback, failedCallback) {
                
                var dto = getRequestDto(dataDefine, viewModel);
                
                // 默认资金成本为 0
                if (!dto.capitalCost) dto.capitalCost = 0;
                
                var invaildProp = [];
                
                if (vaildMessage) {
                    for (var prop in vaildMessage) {
                        if (vaildMessage.hasOwnProperty(prop)) {
                            if (!dto[prop] && dto[prop] !== 0) invaildProp.push(vaildMessage[prop]);
                        }
                    }
                }
                
                console.log("debug getNewFuturePrice dto:" + JSON.stringify(dto));
                
                if (failedCallback && invaildProp.length > 0) {
                    failedCallback("没有指定数值： \r\n" + invaildProp.join("\r\n"));
                } else {
                    httpService.postService(servicePathConst.post_update_futures_price, dto, function (data) {
                        for (var prop in data) {
                            if (!data.hasOwnProperty(prop)) continue;
                            
                            if (data[prop] === null || data[prop] === undefined) delete data[prop];
                        }                        

                        data.profitLossByYield = profitLossToArray(data.profitLossByYield);
                        successCallback(data);
                    }, failedCallback);
                }
            };
            
            // 计算 6
            this.doCalCulate = function (dataDefine, vaildMessage, viewModel, successCallback, failedCallback, updateProp) {
                
                var dto = getRequestDto(dataDefine, viewModel);
                
                // 默认资金成本为 0
                if (!dto.capitalCost) dto.capitalCost = 0;
                
                var invaildProp = [];
                
                if (vaildMessage) {
                    for (var prop in vaildMessage) {
                        if (vaildMessage.hasOwnProperty(prop)) {
                            if (!dto[prop] && dto[prop] !== 0) invaildProp.push(vaildMessage[prop]);
                        }
                    }
                }
                
                console.log("计算Api DTO：%s", JSON.stringify(dto));
                
                if (invaildProp.length > 0) {
                    console.warn("计算Api 没有指定数值： %s", invaildProp.join("; "));
                }
                
                // 冲击成本转换
                if (dto.impactCost) dto.impactCost = dto.impactCost / 10000;
                
                var url = servicePathConst.post_do_calculator;
                
                // if (updateProp === "yieldType")
                
                switch (updateProp) {
                    case "yieldType":
                        url = servicePathConst.post_yieldType_update;
                        break;
                    case "bondCode":
                        url = servicePathConst.post_yieldType_update;
                        break;
                    case "yield":
                        delete dto.bondNetPrice;
                        delete dto.bondFullPrice;
                        break;
                    case "bondNetPrice":
                        delete dto.yield;
                        delete dto.bondFullPrice;
                        break;
                    case "bondFullPrice":
                        delete dto.yield;
                        delete dto.bondNetPrice;
                        break;
                    default:
                }
                
                httpService.postService(url, dto, function (data) {
                    // console.log("计算Api Result：%s", JSON.stringify(data));
                    
                    for (var prop in data) {
                        if (!data.hasOwnProperty(prop)) continue;
                        
                        if (data[prop] === null || data[prop] === undefined) delete data[prop];
                    }
                    
                    data.profitLossByYield = profitLossToArray(data.profitLossByYield);
                    
                    successCallback(data);
                }, failedCallback);

            };
            
            //this.updateBond = function (dataDefine, vaildMessage, viewModel, successCallback, failedCallback) {
            //    thisService.doCalCulate(dataDefine, vaildMessage, viewModel, successCallback, failedCallback, true);
            //};
            
            // 取得现券列表 7
            this.getBondList = function (keyword, successCallback, failedCallback) {
                
                httpService.postService(servicePathConst.post_bond_search, keyword, function (data) {
                    successCallback(data);
                }, failedCallback);
        
            };
            
            // 通过repoRateType中选择的price和bpValue计算并取得资金成本
            this.getCapitalCost = function (repoRateType) {
                if (!repoRateType) return 0;
                
                if (repoRateType.bpValue) repoRateType.bpValue = +repoRateType.bpValue;
                else repoRateType.bpValue = 0;
                
                if (repoRateType.price) repoRateType.price = +repoRateType.price;
                else repoRateType.price = 0;

                // return repoRateType.price + repoRateType.bpValue / 100;
                return +numberFilter(repoRateType.price + repoRateType.bpValue / 100, 2) ;
            };

        }]);

});

