/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('currencyValueDirective', ['$filter', function ($filter) {
            var currency = $filter('currency');
            
            return {
                require: 'ngModel',  
                link: function (scope, elm, attrs, ctrl) {
                    
                    function formatter(value) {
                        // return +value.replace(/\,/g, value);

                        return currency(value, "", 0);
                    }
                    
                    function parser() {
                        // return currency(ctrl.$viewValue, "");

                        return +ctrl.$viewValue.replace(/\,/g, "");
                    }
                    
                    ctrl.$formatters.push(formatter);
                    ctrl.$parsers.unshift(parser);
                }
            };
        }]);
});
