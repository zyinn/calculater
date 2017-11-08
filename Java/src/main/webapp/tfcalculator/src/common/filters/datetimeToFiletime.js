/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.filter('datetimeToFiletime', [function () {
            
            return function (datetime) {
                if (!datetime) return undefined;
                try {
                    if (datetime.constructor === Date) {
                        return datetime.UTC();
                    } else {
                        return datetime;
                    }
                } catch (e) {
                    return undefined;
                }
            };
        }]);
});
