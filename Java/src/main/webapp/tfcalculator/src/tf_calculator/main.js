/**
 * Create by WeiLai on 01/29/2016
 * 
 */
require.config({
    baseUrl: ".",
    paths: {
        
        spin: "common/scripts/spin",
        
        'tc.mainModule': "tf_calculator/mainModule",

        'tcService': "tf_calculator/tcService",
        
        'tc.mainCtrl': "tf_calculator/controllers/mainCtrl",
        'tc.headerCtrl': "tf_calculator/controllers/headerCtrl",
        'tc.tabViewCtrl': "tf_calculator/controllers/tabViewCtrl",
        'tc.resultListCtrl': "tf_calculator/controllers/resultListCtrl"
    }, 
    shim: {
        // mainCtrl: { deps: ['angular', 'jquery', 'require', 'spin'], exports: "mainCtrl" },
    }
});

define([
    'tc.mainModule',

    'tcService',

    'tc.mainCtrl',
    'tc.headerCtrl',
    'tc.tabViewCtrl',
    'tc.resultListCtrl'
], function (mainModule) {
    'use strict';
    
    //mainModule.constant('routeConst', {
    //    tf_calculator: 'tab_view.tf_calculator'
    //});
    
    // 路由
    mainModule
//.config([
//        //'$stateProvider', '$urlRouterProvider', 'routeConst', function ($stateProvider, $urlRouterProvider, routeConst) {
//        //    $stateProvider.state(routeConst.tf_calculator, {
//        //        url: 'tf_calculator/:tfId',
//        //        templateUrl: 'tf_calculator/main.html'
//        //    });

//        //    // console.log("angular.module: tfcalculator.bpla config created.");
//        //}
//    ])
        .run([
            '$rootScope', '$urlRouter', function($rootScope, $urlRouter) {
                $rootScope.$on('$locationChangeSuccess', function(evt) {
                    // Halt state change from even starting
                    evt.preventDefault();
                    // Perform custom logic

                    // Continue with the update and state transition if logic allows
                    $urlRouter.sync();
                });
            }
        ]);
    
    //// Controller
    //mainModule.controller('mainCtrl', ['$injector', '$scope', '$state', '$sce', '$location', 'commonService', 'bplaService', 'routeConst',
    //    function ($injector, $scope, $state, $sce, $location, commonService, bplaService, routeConst) {
    //        require(['tf_calculator/controllers/mainCtrl'], function (mainCtrl) {
    //            $injector.invoke(mainCtrl, this, {
    //                '$scope': $scope, '$state': $state, '$sce': $sce, '$location': $location, 'commonService': commonService, 'bplaService': bplaService, 'routeConst': routeConst
    //            });
    //        });
    
    //       //  console.log("angular.module: tfcalculator.bpla mainCtrl created.");
    //    }])
    //;
    
    //angular.element(document.getElementById('basis_pndl_analysis'));
    //angular.element().ready(function () {
    //    angular.bootstrap(document, ['basis_pndl_analysis']);
    //});
    
    return mainModule;
});
