/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('percentValueDirective', ['$filter', function ($filter) {
            var percentFilter = $filter('percentFilter'), 
                number = $filter('number');

            return {
                require: 'ngModel',  
                link: function (scope, elm, attrs, ctrl) {

                    function formatter(value) {
                        return +percentFilter(value, false); //format  
                    }
                    
                    function parser() {
                        return number(ctrl.$viewValue / 100, 6);
                    }
                    
                    ctrl.$formatters.push(formatter);
                    ctrl.$parsers.unshift(parser);
  
                }
            };
        }]);
});
