/**
 * Create by WeiLai on 01/29/2016
 * 
 */
require.config({
    baseUrl: ".",
    paths: {
        //jquery: "../bower_components/jquery/dist/jquery",
        
        //angular: "../bower_components/angular/angular",
        
        echarts: "common/scripts/echarts",
        spin: "common/scripts/spin",
        
        //percentFilter: "common/filters/percentFilter",
        
        //customSelect0Directive: "common/directives/customSelect0Directive",
        //datePickerDirective: "common/directives/datePickerDirective",
        //percentValueDirective: "common/directives/percentValueDirective",
        
        //directiveUtilService: "common/services/directiveUtilService",
        //commonService: "common/services/commonService",
        //httpService: "common/services/httpService",
        
        "basis_pndl_analysis.mainModule": "basis_pndl_analysis/mainModule",
        
        "basis_pndl_analysis.bplaService": "basis_pndl_analysis/bplaService",
        
        "basis_pndl_analysis.mainCtrl": "basis_pndl_analysis/controllers/mainCtrl",
        "basis_pndl_analysis.bplaFormCtrl": "basis_pndl_analysis/controllers/bplaFormCtrl",
        "basis_pndl_analysis.bplaChartCtrl": "basis_pndl_analysis/controllers/bplaChartCtrl"
    }, 
    shim: {
        //spin: { deps: ['jquery'] },

        //"basis_pndl_analysis.mainCtrl": { deps: ['angular', 'jquery', 'require', 'spin'], exports: "basis_pndl_analysis.mainCtrl" },
        "basis_pndl_analysis.bplaFormCtrl": { deps: ['basis_pndl_analysis.mainCtrl', 'spin'], exports: "basis_pndl_analysis.bplaFormCtrl" },
        "basis_pndl_analysis.bplaChartCtrl": { deps: ['basis_pndl_analysis.mainCtrl'], exports: "basis_pndl_analysis.bplaChartCtrl" }
    }
});

define([
    //'angular', 
    //'require',

    'basis_pndl_analysis.mainModule',

    'basis_pndl_analysis.bplaService',

    'basis_pndl_analysis.mainCtrl',
    'basis_pndl_analysis.bplaFormCtrl',
    'basis_pndl_analysis.bplaChartCtrl'
], function (mainModule) {
    'use strict';
    
    //mainModule.constant('routeConst', {
    //    basis_pndl_analysis: 'tab_view.basis_pndl_analysis'
    //});
    
    // 路由
    mainModule
//.config([
//        //'$stateProvider', '$urlRouterProvider', 'routeConst', function ($stateProvider, $urlRouterProvider, routeConst) {
//        //    $stateProvider.state(routeConst.basis_pndl_analysis, {
//        //        url: 'basis_pndl_analysis/:tfId',
//        //        templateUrl: 'basis_pndl_analysis/main.html'
//        //    });

//        //    // console.log("angular.module: tfcalculator.bpla config created.");
//        //}
//    ])

.run(['$rootScope', '$urlRouter', function ($rootScope, $urlRouter) {
            $rootScope.$on('$locationChangeSuccess', function (evt) {
                // Halt state change from even starting
                evt.preventDefault();
                // Perform custom logic
                
                // Continue with the update and state transition if logic allows
                $urlRouter.sync();
            });
        }
    ]);
    
    // Controller
    //mainModule.controller('basis_pndl_analysis.mainCtrl', ['$injector', '$scope', '$state', '$sce', '$location', 'commonService', 'bplaService', 'routeConst',
    //    function ($injector, $scope, $state, $sce, $location, commonService, bplaService, routeConst) {
    //        require(['basis_pndl_analysis.mainCtrl'], function (mainCtrl) {
    //            $injector.invoke(mainCtrl, this, {
    //                '$scope': $scope, '$state': $state, '$sce': $sce, '$location': $location, 'commonService': commonService, 'bplaService': bplaService, 'routeConst': routeConst
    //            });
    //        });
            
    //       //  console.log("angular.module: tfcalculator.bpla mainCtrl created.");
    //    }]).controller('basis_pndl_analysis.bplaFormCtrl', ['$scope', '$injector', '$sce', '$state', '$filter', 'commonService', 'bplaService', 'routeConst',
    //    function ($scope, $injector, $sce, $state, $filter, commonService, bplaService, routeConst) {
    //        require(['basis_pndl_analysis.bplaFormCtrl'], function (bplaFormCtrl) {
    //            $injector.invoke(bplaFormCtrl, this, {
    //                '$scope': $scope.$parent, '$sce': $sce, '$state': $state, '$filter': $filter, 'commonService': commonService, 'bplaService': bplaService, 'routeConst': routeConst
    //            });
    //        });
            
    //       //  console.log("angular.module: tfcalculator.bpla bplaFormCtrl created.");
    //    }]).controller('basis_pndl_analysis.bplaChartCtrl', ['$scope', '$injector', '$sce', '$location', '$filter', 'commonService', 'bplaService',
    //    function ($scope, $injector, $sce, $location, $filter, commonService, bplaService) {
    //        require(['basis_pndl_analysis.bplaChartCtrl'], function (bplaChartCtrl) {
    //            $injector.invoke(bplaChartCtrl, this, {
    //                '$scope': $scope.$parent, '$sce': $sce, '$location': $location, '$filter': $filter, 'commonService': commonService, 'bplaService': bplaService
    //            });
    //        });
            
    //       //  console.log("angular.module: tfcalculator.bpla bplaChartCtrl created.");
    //    }])
    //;
    
    //angular.element(document.getElementById('basis_pndl_analysis'));
    //angular.element().ready(function () {
    //    angular.bootstrap(document, ['basis_pndl_analysis']);
    //});
    
    return mainModule;
});
