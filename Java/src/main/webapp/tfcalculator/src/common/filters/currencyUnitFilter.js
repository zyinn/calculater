/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.filter('currencyUnitFilter', ['$filter', function ($filter) {
            var currencyFilter = $filter('currency');
            
            return function (number) {
                var value = +number;
                
                if (!value && value !== 0) return '0 元';
                
                if (Math.abs(value) > Math.pow(10, 8)) return (number * Math.pow(10, -8)).toFixed(2) + ' 亿元';
                if (Math.abs(value) > Math.pow(10, 4)) return (number * Math.pow(10, -4)).toFixed(2) + ' 万元';

                return number + ' 元';
            };


        }]);
});
