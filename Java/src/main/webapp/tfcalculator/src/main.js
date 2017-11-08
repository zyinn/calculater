/**
 * @ngdoc overview
 * @name tfcalculator
 * @description
 * # tfcalculator
 *
 * Main module of the application.
 */

require.config({
    baseUrl: ".",
    paths: {
        jquery: "../bower_components/jquery/dist/jquery",
        'jquery-ui/core': "lib/jquery-ui/ui/core",
        'jquery-ui/datepicker': "lib/jquery-ui/ui/datepicker",
        'jquery-ui/datepicker/zh-CN': "lib/jquery-ui/ui/i18n/datepicker-zh-CN",
        
        metro: "../bower_components/metro/build/js/metro",
        // widget: "../bower_components/metro/js/widget",
        // 'widgets.hits': "common/scripts/widgets/hits",
        
        bootstrap: "../bower_components/bootstrap/dist/js/bootstrap",
        
        echarts: "common/scripts/echarts",
        spin: "common/scripts/spin",
        
        angular: "../bower_components/angular/angular",

        ngAnimate: "../bower_components/angular-animate/angular-animate",
        ngDialog: "../bower_components/ng-dialog/js/ngDialog",
        ngDate: "../bower_components/angular-ui-date/dist/date",
        ngNumberic: "../bower_components/angular-numeric-directive/src/numeric-directive",

        ngCss: "../bower_components/angular-css/angular-css",
        uiRoute: "../bower_components/angular-ui-router/release/angular-ui-router",
        uiSelect: "../bower_components/ui-select/dist/select",
        uib: "../bower_components/angular-bootstrap/ui-bootstrap",
        uibTpls: "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
        
        DateUtils: "common/scripts/DateUtils", 
        StringUtils: "common/scripts/StringUtils", 
        NumberUtils: "common/scripts/NumberUtils", 
        ArrayUtils: "common/scripts/ArrayUtils", 
        FileSaver: "common/scripts/FileSaver", 
        
        // config: "config",
        app: "app",
        
        percentFilter: "common/filters/percentFilter",
        currencyUnitFilter: "common/filters/currencyUnitFilter",
        filetimeToDatetime: "common/filters/filetimeToDatetime",
        
        customSelect0Directive: "common/directives/customSelect0Directive",
        datePickerDirective: "common/directives/datePickerDirective",
        percentValueDirective: "common/directives/percentValueDirective",
        currencyValueDirective: "common/directives/currencyValueDirective",
        repoRateDirective: "common/directives/repoRateDirective",

        searchBoxDirective: "common/directives/searchBoxDirective",
        
        directiveUtilService: "common/services/directiveUtilService",
        httpService: "common/services/httpService",
        commonService: "common/services/commonService",
        delayEventService: "common/services/delayEventService",
        
        confirmDialogCtrl: "common/controllers/confirmDialogCtrl",
        tabViewCtrl: "common/controllers/tabViewCtrl"
    }, 
    shim: {
        jquery: { exports: 'jquery' },
        
        spin: { deps: ['jquery'] },
        
        bootstrap: { deps: ['jquery'] },
        
        angular: { deps: ['jquery'], exports: 'angular' },

        ngAnimate: { deps: ['angular'] },
        ngUiBootstrap: { deps: ['angular'] },
        ngDialog: { deps: ['angular'] },
        ngDate: { deps: ['angular', 'jquery-ui/datepicker/zh-CN'] },
        ngNumberic: { deps: ['angular'] },

        ngCss: { deps: ['angular'] },
        uiRoute: { deps: ['angular'] }, 
        uiSelect: { deps: ['angular'] },
        uib: { deps: ['angular'] },
        uibTpls: { deps: ['angular'] },
        
        customSelect0Directive: { deps: ['StringUtils'] },
        
        // config: { deps: ['angular'], exports: 'config' },
        
        // commonService: { deps: ['config'] }
    },
    priority: [
        'angular'
    ]
});

require([

    // 依赖库
    'angular', 
    'require', 

    'metro',

    'echarts',
    'spin',

    'ngDate',
    // 'ObjectUtils',

    'DateUtils',
    'NumberUtils',
    'ArrayUtils',
    'FileSaver',

    'config',

    'percentFilter',
    'currencyUnitFilter',
    'filetimeToDatetime',

    'customSelect0Directive',
    'datePickerDirective',
    'percentValueDirective',
    'currencyValueDirective',
    'repoRateDirective',

    'searchBoxDirective',

    'commonService',
    'httpService',
    'delayEventService',

    'confirmDialogCtrl',
    'tabViewCtrl'
], function (angular, require) {
    'use strict';
    
    var dataDefine = {
        mainModule: { name: 'tfcalculator' },
        subModule: [
            { name: 'tfcalculator.basis_pndl_analysis', path: 'basis_pndl_analysis/main' },
            { name: 'tfcalculator.tc', path: 'tf_calculator/main' }
        ],
        routeConst: {
            basis_pndl_analysis: 'tab_view.basis_pndl_analysis',
            tf_calculator: 'tab_view.tf_calculator'
        }
    };
    
    // define route
    var mainModule = angular.module(dataDefine.mainModule.name);
    
    mainModule.constant('routeConst', dataDefine.routeConst);
    
    // Config
    mainModule.config([
        '$httpProvider', function ($httpProvider) {
            $httpProvider.defaults.useXDomain = true;
            delete $httpProvider.defaults.headers.common['X-Requested-With'];
        }
    ]).config([
        '$stateProvider', '$urlRouterProvider', 'cssDependencyMap', 'routeConst', function ($stateProvider, $urlRouterProvider, cssDependencyMap, routeConst) {
            //$stateProvider.state('contacts', {
            //    url: '/contacts',
            //    template: '<h1>My Contacts</h1>'
            //});
            
            $stateProvider.state('tab_view', {
                abstract: true,
                url: '/',
                templateUrl: 'common/views/tabView.html'
            }).state(routeConst.basis_pndl_analysis, {
                url: 'basis_pndl_analysis/:tfId',
                templateUrl: 'basis_pndl_analysis/main.html',
                css: cssDependencyMap.basis_pndl_analysis
            }).state(routeConst.tf_calculator, {
                url: 'tf_calculator/:tfId',
                templateUrl: 'tf_calculator/main.html',
                css: cssDependencyMap.tf_calculator
            }).state("tab_view.test", {
                url: 'test',
                templateUrl: 'html1.html',
                controller: ['$scope', function ($scope) {
                    }]
            });
            
            // console.log("angular.module: tfcalculator config created.");
            // catch all route
            // send users to the form page 
            // $urlRouterProvider.otherwise('/basis_pndl_analysis/');
        }
    ]).config(['uiSelectConfig', function (uiSelectConfig) {
            uiSelectConfig.theme = 'select2';
            uiSelectConfig.resetSearchInput = false;
            uiSelectConfig.appendToBody = true;
        }]);
    
    // ReSharper disable once InconsistentNaming
    mainModule.run(['$rootScope', '$urlRouter', function ($rootScope, $urlRouter) {
            $rootScope.$on('$locationChangeSuccess', function (event, next, current) {
                // require(['basis_pndl_analysis/main']);
                // console.log("$locationChangeSuccess ");
            });
            
            $rootScope.$on('$routeChangeSuccess', function () {
                // console.log("$routeChangeSuccess ");
            });
        }])
    ;
    
    var modulesLoaded = false;
    var domLoaded = false;
    
    require(dataDefine.subModule.map(function (x) { return x.path; }), function (mainModule) {
        modulesLoaded = true;
        
        if (domLoaded) {
            angular.bootstrap(document, dataDefine.subModule.map(function (x) { return x.name; }).concat([dataDefine.mainModule.name]));
        }
    });
    
    // bootstrap the app manually
    angular.element(document.getElementsByTagName('html')[0]).ready(function () {
        domLoaded = true;
        
        if (modulesLoaded) {
            angular.bootstrap(document, dataDefine.subModule.map(function (x) { return x.name; }).concat([dataDefine.mainModule.name]));
        }

        // console.log("bootstrap the app manually.");
    });
});




