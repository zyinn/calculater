// ReSharper disable once InconsistentNaming
define(['angular', 'common/services/tService'], function (angular) {
    'use strict';
    
    angular.module('cswap_web_app').controller('toggleButtonCtrl', [
        
        '$rootScope', '$scope', '$timeout', '$location', 'tService', 'sessionService', 'sessionKeyConst', 'authenticationService', 
        function ($rootScope, $scope, $timeout, $location, tService, sessionService, sessionKeyConst, authenticationService) {
            

        }]).directive('togglebutton0', function () {
            return {
                restrict: 'A',
                replace: false,
                scope: true,
                templateUrl: 'common/directive/toggle_button_0.html',
                link: function (scope, element, attrs) {
                    scope.buttonContent = attrs.content;

                    if (attrs.url) {
                        scope.onClick = function() {
                            // window.location.href = attrs.url;
                            window.open(attrs.url);
                        };
                    }
                }
        };
    });

});