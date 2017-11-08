/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.filter('percentFilter', ['$filter', function ($filter) {
            var numberFilter = $filter('number');

            return function (number, symbol) {
                var value = undefined;
                
                try {
                    value = parseFloat(number);
                } catch (e) {
                    return "";
                }

                if (!value && value !== 0) return "";

                var sym = symbol ? "%" : "";
                
                return numberFilter(value * 100, 4).toString() + sym;
                
            };


        }]);
});
