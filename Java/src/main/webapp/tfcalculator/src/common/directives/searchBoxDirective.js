/**
 * Created by weilai on 2016/08/01.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('searchBoxDirective', ['$sce', '$filter', function ($sce, $filter) {
            
            var injectedCtrl = ['$scope', function ($scope) {
                    
                    function openDropdown() {
                        if (!$scope.itemSource || $scope.itemSource.length === 0 || !($scope.itemSource instanceof Array)) return;
                        
                        var resultArr = $filter("filter")($scope.itemSource, $scope.filterString);
                        if (!resultArr || resultArr.length === 0) return;
                        
                        if ($(".bondCodeDropdown.open").length === 0) {
                            $("#dropdownMenu{0}-toggle-button".format($scope.idSeq)).click();
                            
                            setTimeout(function () {
                                $("#dropdownMenu" + $scope.idSeq).focus();
                            }, 100);
                        }
                    };
                    
                    function closeDropdown() {
                        if ($(".bondCodeDropdown.open").length !== 0) $("#dropdownMenu{0}-toggle-button".format($scope.idSeq)).click();
                    };
                    
                    function updateSelectedValue(val, target) {
                        $scope.selectedValue = val;
                        
                        $scope.filterString = $scope.formatItem($scope.selectedValue);
                        
                        $scope.oldSelectedValue = val;
                        
                        var arg = {
                            newValue: val, 
                            oldValue: $scope.oldSelectedValue, 
                            target: target
                        };
                        
                        if ($scope.onChangeModel) $scope.onChangeModel({ $event: arg });
                    };
                    
                    $scope.onMouseenterDropdown = function (e) {
                        var active = $(e.currentTarget).parent().find("li.active");
                        if (active.length > 0) active.removeClass("active");
                    };
                    
                    $scope.onChangeFilterString = function () {
                        var arg = {
                            oldValue: $scope.filterStringOldValue,
                            newValue: $scope.filterString,
                            target: $("#dropdownMenu" + $scope.idSeq)[0]
                        };
                        
                        if ($scope.onChangeSearchKeyword) $scope.onChangeSearchKeyword({ $event: arg, $cosEvent: arg });
                        
                        $scope.filterStringOldValue = $scope.filterString;
                    };
                    
                    $scope.formatItem = function (item) {
                        if ($scope.listMemberFormatter) {
                            return $scope.listMemberFormatter(item);
                        } else {
                            return item.toString();
                        }
                    };
                    
                    $scope.onFocus = function () {
                        if (!$scope.itemSource || $scope.itemSource.length === 0) return;
                        
                        if ($scope.itemSource.findItem(function (e) { return $scope.formatItem(e) === $scope.filterString; })) return;
                        
                        openDropdown();
                    };
                    
                    $scope.onKeydown = function (event) {
                        
                        if (!$scope.itemSource || $scope.itemSource.length < 1 || !($scope.itemSource instanceof Array)) return;
                        
                        var resultArr = $filter("filter")($scope.itemSource, $scope.filterString);
                        
                        if (!resultArr || resultArr.length < 1 || !(resultArr instanceof Array)) return;
                        
                        var current = $scope.selectedValue ? resultArr.findIndex(function (e) { return e.$id === $scope.selectedValue.$id; }) : undefined;
                        
                        if (event) {
                            switch (event.keyCode) {
                                case 13:
                                    closeDropdown();
                                    if ($scope.selectedValue) updateSelectedValue($scope.selectedValue, event.target);
                                    break;
                                case 40:
                                    if ((!current && current !== 0) || current >= resultArr.length) {
                                        $scope.selectedValue = resultArr[0];
                                    } else {
                                        $scope.selectedValue = resultArr[current + 1];
                                    }
                                    break;
                                case 38:
                                    if ((!current && current !== 0) || current <= 0) {
                                        $scope.selectedValue = resultArr[resultArr.length - 1];
                                    } else {
                                        $scope.selectedValue = $scope.selectedValue = resultArr[current - 1];
                                    }
                                    break;
                                default:
                            }
                        }
                    };
                    
                    $scope.onClickDropdownItem = function (e) {
                        var ex = e || window.event;
                        var obj = ex.target || ex.srcElement;
                        
                        if (obj.nodeName === "LI") {
                            var selectedItem = angular.element(obj).scope().item;
                            
                            if (selectedItem) {
                                closeDropdown();
                                updateSelectedValue(selectedItem, ex.target);
                            }
                        }
                    };
                    
                    $scope.onClickInput = function (event) {
                        $scope.filterString = "";
                        
                        openDropdown();
                    };
                    
                    $scope.onClickDoSearchButton = function (event) {
                        openDropdown();
                    };
                    
                    var initView = function () {
                        $scope.idSeq = 0;
                        
                        while (true) {
                            if ($("dropdownMenu" + $scope.idSeq).length > 0) $scope.idSeq++;
                            else break;
                        }
                        
                        $scope.$watch(function () {
                            return $scope.itemSource;
                        }, function (newValue, oldValue) {
                            if (newValue && newValue.length > 0) {
                                if (newValue instanceof Array) newValue.forEach(function (item, index) { item.$id = index; });
                                openDropdown();
                            }
                            
                            $("dropdownMenu" + $scope.idSeq).focus();
                        });

                    }();
                }];
            
            return {
                restrict: 'C',
                replace: true,
                templateUrl: 'common/directives/searchBoxTemplate.html',
                controller: injectedCtrl,
                scope: {
                    itemSource: "=itemSource",
                    selectedValue: "=ngModel",
                    isViewBusy: "=ngDisabled",
                    
                    listMemberFormatter: "=listMemberFormatter",
                    
                    onChangeModel: "&ngChange",
                    onChangeSearchKeyword: "&onChangeSearchKeyword"
                },
                link: function (scope, element, attrs, ctrl) {
                    
                    var inputEle = element.find("input");
                    
                    var oldStyle = inputEle.attr("style");
                    
                    inputEle.attr("style", oldStyle + ";" + attrs.style);

                    //scope.$watch(function () {
                    //    return scope.capitalCost;
                    //}, function (newValue, oldValue) {
                    //    if (newValue !== oldValue) {
                    //        scope.capitalCost = newValue;
                    //    }

                    //    if (!newValue) {
                    //        scope.capitalCost = 0;
                    //        scope.isNotCustom = false;
                    //        scope.selectedRepoRateType = undefined;
                    //    }
                    //});
                }
            };

        }]);
});
