// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['angular', 'app'], function (angular, appModule) {
    "use strict";
    
    appModule.service('directiveUtilService', [function () {
            
            this.check = function (name, element, targerNodeName) {
                if (!element[0] || element[0].nodeName !== targerNodeName) {
                    console.error("{0} only for element '{1}'.".format(name, targerNodeName));
                    return false;
                }
                
                if (!$) {
                    console.error("{0}: jquery is not loaded.".format(name));
                    return false;
                }
                
                if (!element.before) {
                    console.error("{0}: jquery must be loaded before angularJs.".format(name));
                    return false;
                }

                return true;
            };

        }]);
});

