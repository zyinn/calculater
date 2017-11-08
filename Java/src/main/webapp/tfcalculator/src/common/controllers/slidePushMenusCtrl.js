// ReSharper disable once InconsistentNaming
define([
    'angular', 

    'common/directive/slide_push_menus',
    'common/services/tService'
], function (angular) {
    'use strict';
    
    angular.module('cswap_web_app').controller('slidePushMenusCtrl', ['$rootScope', '$scope', '$timeout', '$location', 'tService', 'sessionService', 'authenticationService', function ($rootScope, $scope, $timeout, $location, tService, sessionService, authenticationService) {
            var onClickSearchItem = function (obj) {
                var checkItem = function (element) {
                    if (!element) return false;
                    
                    var anyCheckedFlag = false;
                    
                    if (element.className.indexOf("active") > -1) {
                        element.className = element.className.replace(" active", "");
                    } else {
                        element.className += " active";
                        anyCheckedFlag = true;
                    }
                    
                    return anyCheckedFlag;
                };
                
                if (obj.attributes["code"].nodeValue === "all") {
                    var anyCheckedFlag = false;
                    
                    var searchItems = $(obj).parentsUntil("div.form-group").find("a.searchItem");
                    
                    searchItems.each(function (index, item) {
                        if (item.className.indexOf("active") < 0) {
                            item.className += " active";
                            anyCheckedFlag = true;
                        }
                    });
                    
                    if (!anyCheckedFlag) {
                        searchItems.each(function (index, item) {
                            item.className = item.className.replace(" active", "");
                        });
                    }
                } else {
                    checkItem(obj);
                }
            };
            
            var onClickShowAll = function (obj) {
                $("form.form-search a").each(function (index, item) {
                    if (item.className.indexOf("btn-all") > -1) return;
                    item.className = item.className.replace(" active", "");
                });
            };
            
            $scope.ngClick = function (e) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                
                switch (obj.nodeName) {
                    case "A":
                        if (obj.className.indexOf("showAll") > -1) {
                            onClickShowAll(obj);
                        } else {
                            onClickSearchItem(obj);
                        }
                        break;
                    default:
                        break;
                }
            };
            
            $scope.searchAll = function () {
                if ($scope.doSearch) {
                    $scope.doSearch();
                } else {
                    console.log("Error while click 'show all' : need provide $scope.doSearch.");
                }
            };
            
            $scope.onOpenSearchCriteriaBar = function () {
                var slideLeft = new Menu({
                    wrapper: '#o-wrapper',
                    type: 'slide-right',
                    menuOpenerClass: '.c-button',
                    maskId: '#c-mask'
                });

                slideLeft.open();
            };

        }]).directive('slidepushmenus', function () {
        return {
            restrict: 'A',
            templateUrl: 'common/directive/slide_push_menus.html',
            controller: 'slidePushMenusCtrl'
        };
    });

});