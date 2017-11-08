// ReSharper disable once InconsistentNaming
define([
    'angular', 

    'app'
], function (angular, appModule) {
    'use strict';
    
    appModule.controller('tabViewCtrl', [
        
        '$scope', '$location', '$state', '$urlRouter', 'routeConst', function ($scope, $location, $state, $urlRouter, routeConst) {

            $scope.basis_pndl_analysis = routeConst.basis_pndl_analysis;
            $scope.tf_calculator = routeConst.tf_calculator;

            $scope.isActive = function(name) {
                return $state.current.name === name;
            };

            $scope.onbClickTab = function(e) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;

                if (obj && obj.tagName === "A") {
                    
                }

                // debugger;
            };

        }]);
    

});