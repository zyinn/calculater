/*** @ngdoc function
 * 
 * @name cswap_web_app.controller:MainCtrl
 * 
 * @description
 * # MainCtrl
 * Controller of the cswap_web_app
 * */
// ReSharper disable InconsistentNaming
// ReSharper disable once PossiblyUnassignedProperty

angular.module('cswap_web_app').controller('SelectCtrl', function ($scope, TService) {
    'use strict';
    
    var remove_selctedByType = function (type) {
        for (var i = 0; i < $scope.selected_items.length; i++) {
            if ($scope.selected_items[i].type === type) {
                $scope.selected_items.splice(i, 1);
                i--;
            }
        }
    };

    var removeSearchCriteriaItem = function (type, targetArr, selectItem) {
        var hasSelected = false;
        var isFound = false;
        targetArr.forEach(function(e, index) {
            if (e.selected) {
                if (e === selectItem) {
                    isFound = true;
                    e.selected = false;
                } else {
                    hasSelected = e.selected;
                }
            }
        });
        if (!hasSelected) {
            targetArr[0].selected = true;
        }
    };

    var onClickSearchCriteriaItem = function (type, targetArr, selectItem) {
        var addOrRmSearchCriteria = function (sourceArr, item) {
            var result = [];
            sourceArr.forEach(function (e) {
                if (e !== item) {
                    result.push(e);
                }
            });
            if (sourceArr.length === result.length) {
                result.push(item);
            }
            return result;
        };

        if (selectItem.id === type) {
            targetArr[0].selected = true;
            for (var i = 1; i < targetArr.length; i++) {
                var otherItem = targetArr[i];
                otherItem.selected = false;
            }
            remove_selctedByType(selectItem.type);
        } else {
            var selectedCount = 0;
            $scope.selected_items.forEach(function (e1) {
                targetArr.forEach(function (e2) {
                    if (e1 === e2) {
                        selectedCount++;
                    }
                });
            });
            var newArr = addOrRmSearchCriteria($scope.selected_items, selectItem);
            selectItem.selected = newArr.length > $scope.selected_items.length;
            targetArr[0].selected = (newArr.length <= $scope.selected_items.length && selectedCount <= 1);
            $scope.selected_items = newArr;
        }

        // send event
        $scope.$emit("SearchCriteriaChenged", $scope.selected_items);
    };
    
    
    $scope.selected_items = [];
    
    //类型
    $scope.type_items = [
        { id: 'type_all', name: TService.T('show_all'), type: 'type', selected: true }, 
        { id: 'iron_ore_swap', type: 'type', name: TService.T('iron_ore_swap'), selected: false },
        { id: 'steam_coal_swap', type: 'type', name: TService.T('steam_coal_swap'), selected: false },
        { id: 'copper_premium_swap', type: 'type', name: TService.T('copper_premium_swap'), selected: false },
        { id: 'styrene_swap', name: TService.T('styrene_swap'), type: 'type', selected: false },
        { id: 'ethylene_glycol_swap', name: TService.T('ethylene_glycol_swap'), type: 'type', selected: false },
        { id: 'cape_FFA', name: TService.T('cape_FFA'), type: 'type', selected: false },
        { id: 'panama_FFA', name: TService.T('panama_FFA'), type: 'type', selected: false },
        { id: 'supramax_FFA', name: TService.T('supramax_FFA'), type: 'type', selected: false }];
    //跨度
    $scope.span_items = [
        { id: 'span_all', name: TService.T('show_all'), type: 'span', selected: true }, 
        { id: 'monthly', name: TService.T('monthly'), type: 'span', selected: false },
        { id: 'quarterly', name: TService.T('quarterly'), type: 'span', selected: false },
        { id: 'yearly', name: TService.T('yearly'), type: 'span', selected: false }];
    //到期
    $scope.maturity_items = [{ id: 'maturity_all', name: TService.T('show_all'), type: 'maturity', selected: true }, 
        { id: 'january', name: TService.T('january'), type: 'maturity', selected: false },
        { id: 'february', name: TService.T('february'), type: 'maturity', selected: false },
        { id: 'march', name: TService.T('march'), type: 'maturity', selected: false }, 
        { id: 'april', name: TService.T('april'), type: 'maturity', selected: false },
        { id: 'may', name: TService.T('may'), type: 'maturity', selected: false },
        { id: 'june', name: TService.T('june'), type: 'maturity', selected: false }, 
        { id: 'july', name: TService.T('july'), type: 'maturity', selected: false },
        { id: 'august', name: TService.T('august'), type: 'maturity', selected: false },
        { id: 'september', name: TService.T('september'), type: 'maturity', selected: false }, 
        { id: 'october', name: TService.T('october'), type: 'maturity', selected: false },
        { id: 'november', name: TService.T('november'), type: 'maturity', selected: false },
        { id: 'december', name: TService.T('december'), type: 'maturity', selected: false }];
    
    $scope.type_selectItem = function (item) {
        onClickSearchCriteriaItem("type_all", $scope.type_items, item);
    };
    
    $scope.span_selectItem = function (item) {
        onClickSearchCriteriaItem("span_all", $scope.span_items, item);
    };
    
    $scope.maturity_selectItem = function (item) {
        onClickSearchCriteriaItem("maturity_all", $scope.maturity_items, item);
    };
    
    $scope.removeSelected = function (item) {
        for (var i = 0; i < $scope.selected_items.length; i++) {
            if ($scope.selected_items[i].id === item.id) {
                $scope.selected_items.splice(i, 1);
                
                
                if (item.type === "type") {
                    removeSearchCriteriaItem(item.type, $scope.type_items, item);
                } else if (item.type === "span") {
                    removeSearchCriteriaItem(item.type, $scope.span_items, item);
                } else if (item.type === "maturity") {
                    removeSearchCriteriaItem(item.type, $scope.maturity_items, item);
                }
                break;
            }
        }

        // send event
        $scope.$emit("SearchCriteriaChengedEmit", $scope.selected_items); 
    };

    
});
