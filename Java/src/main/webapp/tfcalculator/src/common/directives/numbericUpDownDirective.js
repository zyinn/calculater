/**
 * Created by weilai on 2016/08/01.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('searchBoxDirective', ['$sce', '$filter', function ($sce, $filter) {
            
            var injectedCtrl = ['$scope', function ($scope) {
                    
                   
                    
                    var initView = function () {
                        $scope.idSeq = 0;
                        
                        while (true) {
                            if ($("dropdownMenu" + $scope.idSeq).length > 0) $scope.idSeq++;
                            else break;
                        }
                     
                    }();
                }];
            
            return {
                restrict: 'C',
                replace: true,
                templateUrl : 'common/directives/numbericUpDownTemplate.html',
                controller: injectedCtrl,
                scope: {
                    modelValue: "=ngModel",

                    
                    
                    listMemberFormatter: "=listMemberFormatter",
                    
                    onChangeModel: "&ngChange",
                    
                    step: "@step",
                },
                link: function (scope, element, attrs, ctrl) {

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
