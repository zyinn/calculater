// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['angular', 'app'], function (angular, appModule) {
    "use strict";
    
    appModule.service('delayEventService', [function () {
            
            var timeoutPoint = undefined;
            
            this.delayOnChangeEvent = function (func) {
                if (timeoutPoint) {
                    clearTimeout(timeoutPoint);
                }
                
                timeoutPoint = setTimeout(func, 200);
            };

        }]);
});

