/**
 * Created by weilai on 2016/08/01.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('repoRateDirective', ['$sce', function ($sce) {
            
            var injectedCtrl = ['$scope', function ($scope) {
                    
                    function calculateCapitalCost() {
                        if (!$scope.selectedRepoRateType) {
                            $scope.isNotCustom = false;
                            return;
                        }
                        
                        $scope.isNotCustom = true;
                        
                        $scope.capitalCost = +$scope.selectedRepoRateType.price + $scope.bpValue * 0.010;
                        
                        $scope.capitalCost = $scope.capitalCost.toFixed(3);
                    };
                    
                    $scope.onKeydown = function (event) {
                        
                        if (event.keyCode === 13) {
                            calculateCapitalCost();
                            
                            if ($scope.updateModel) $scope.updateModel();
                        }
                    };
                    
                    $scope.onChangeBpValue = function () {
                        calculateCapitalCost();
                    };
                    
                    $scope.onChangeRepoRateType = function () {
                        $scope.bpValue = 0;
                        
                        calculateCapitalCost();
                        
                        if ($scope.updateModel) $scope.updateModel();
                    };
                    
                    $scope.$watch(function () {
                        return $scope.dataSource;
                    }, function (newValue, oldValue) {
                        if (newValue && newValue.length > 0 && newValue instanceof Array) {
                            if ($scope.defaultCode) {
                                var result = newValue.findItem(function (e) { return e.code === $scope.defaultCode; });
                                
                                if (result) $scope.selectedRepoRateType = result;
                                else $scope.selectedRepoRateType = newValue[0];
                            } else {
                                $scope.selectedRepoRateType = newValue[0];
                            }
                            
                            calculateCapitalCost();
                        }
                    });
                    
                    var initView = function () {
                        $scope.isNotCustom = false;
                        $scope.bpValue = 0;
                        
                        if (!$scope.vm) $scope.vm = {};
                        
                        if ($scope.dataSource && $scope.dataSource.length > 0 && $scope.dataSource instanceof Array) {
                            if ($scope.defaultCode) {
                                var result = $scope.dataSource.findItem(function (e) { return e.code === $scope.defaultCode; });
                                
                                if (result) $scope.selectedRepoRateType = result;
                                else $scope.selectedRepoRateType = $scope.dataSource[0];
                            } else {
                                $scope.selectedRepoRateType = $scope.dataSource[0];
                            }
                            
                            calculateCapitalCost();
                        }
                    }();
                }];
            
            return {
                restrict: 'C',
                replace: true,
                templateUrl : 'common/directives/repoRateTemplate.html',
                controller: injectedCtrl,
                scope: {
                    dataSource: "=repoRateList",
                    capitalCost: "=ngModel",
                    updateModel: "&ngChange",
                    defaultCode: "@defaultCode"
                },
                link: function (scope, element, attrs, ctrl) {
                    
                    scope.$watch(function () {
                        return scope.capitalCost;
                    }, function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            scope.capitalCost = newValue;
                        }
                        
                        if (!newValue) {
                            scope.capitalCost = 0;
                            scope.isNotCustom = false;
                            scope.selectedRepoRateType = undefined;
                        }
                    });
                }
            };

        }]);
});
