/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.filter('filetimeToDatetime', [function () {
            
            return function (filetime) {
                try {
                    return new Date(+filetime);
                } catch (e) {
                    return undefined;
                }
            };


        }]);
});
