define(
    [
        'angular', 

        'bootstrap',

        'ngAnimate',
        'ngDialog',
        'ngCss',

        'uiSelect',
        'uiRoute',

        'uib',
        'uibTpls',

        'ngNumberic'
    ], function (angular) {
        'use strict';
        
        var module = angular.module('tfcalculator', [
            'ngAnimate',
            'ngDialog',
            'ui.select',
            'ui.router',

            'ui.bootstrap',
            'ui.bootstrap.tpls',

            'angularCSS',
            'purplefox.numeric'
        ]);
        
        console.log("angular.module: tfcalculator created.");
        
        return module;
    });

