/**
 * Created by weilai on 2016/08/01.
 */

define(['angular', 'app'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('inputNumberOnChangeDirective', ['delayEventService', function (delayEventService) {
            
            var injectedCtrl = ['$scope', function ($scope) {
                    
                    var lastKeyCode = undefined;
                    
                    $scope.onChange = function () {
                        console.debug("$scope.onChangeBpValue");
                    };
                    
                    $scope.onKeydown = function () {
                        console.debug("$scope.onChangeBpValue");
                    };
                    
                    var initView = function () {
                        
                    }();
                }];
            
            return {
                restrict: 'A',
                replace: false,
                controller: injectedCtrl,
                scope: {
                    updateModel: "&inputNumberOnChangeDirective"
                },
                link: function (scope, element, attrs, ctrl) {
                    
                    // directive('ngChange', 'onChange').directive('ngKeydown', 'onKeydown').directive(element[0]);
                    
                    debugger;
                },
                compile: function (element, attrs) {
                    debugger;
                    
                    attrs.$set('ngChange', 'onChange()');
                }
            };

        }]);
});
