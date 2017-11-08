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
        
        metro:"../bower_components/metro/build/js/metro",
        // widget: "../bower_components/metro/js/widget",
        // 'widgets.hits': "common/scripts/widgets/hits",
        
        bootstrap: "../bower_components/bootstrap/dist/js/bootstrap",
        
        echarts: "common/scripts/echarts",
        spin: "common/scripts/spin",
        
        angular: "../bower_components/angular/angular",
        uiRoute: "../bower_components/angular-ui-router/release/angular-ui-router",
        ngAnimate: "../bower_components/angular-animate/angular-animate",
        ngUiBootstrap: "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
        ngDialog: "../bower_components/ng-dialog/js/ngDialog",
        
        ngSelect: "../bower_components/ui-select/dist/select",
        ngDate: "../bower_components/angular-ui-date/dist/date",
        ngNumberic: "../bower_components/angular-numeric-directive/src/numeric-directive",
        'ui-bootstrap-tpls': "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
        
        DateUtils: "common/scripts/DateUtils", 
        StringUtils: "common/scripts/StringUtils", 
        
        app: "app",
        
        percentFilter: "common/filters/percentFilter",
        currencyUnitFilter: "common/filters/currencyUnitFilter",
        
        customSelect0Directive: "common/directives/customSelect0Directive",
        datePickerDirective: "common/directives/datePickerDirective",
        percentValueDirective: "common/directives/percentValueDirective",
        currencyValueDirective: "common/directives/currencyValueDirective",
        
        directiveUtilService: "common/services/directiveUtilService",
        httpService: "common/services/httpService",
        commonService: "common/services/commonService",
        
        confirmDialogCtrl: "common/controllers/confirmDialogCtrl",
        tabViewCtrl: "common/controllers/tabViewCtrl"
    }, 
    shim: {
        jquery: {exports: 'jquery' },
        
        spin: { deps: ['jquery'] },
        
        bootstrap: { deps: ['jquery'] },
        
        angular: { deps: ['jquery'], exports: 'angular' },

        uiRoute: { deps: ['angular'] }, 
        ngAnimate: { deps: ['angular'] },
        ngUiBootstrap: { deps: ['angular'] },
        ngDialog: { deps: ['angular'] },
        
        ngSelect: { deps: ['angular'] },
        ngDate: { deps: ['angular', 'jquery-ui/datepicker/zh-CN'] },
        ngNumberic: { deps: ['angular'] },
        'ui-bootstrap-tpls': { deps: ['angular'] },
        
        customSelect0Directive: { deps: ['StringUtils'] },
        
        app: { deps: ['angular'], exports: 'app' },
        
        commonService: { deps: ['app'] }
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
    'ui-bootstrap-tpls',

    // 'ObjectUtils',

    'DateUtils',

    'app',

    'percentFilter',
    'currencyUnitFilter',

    'customSelect0Directive',
    'datePickerDirective',
    'percentValueDirective',
    'currencyValueDirective',

    'commonService',
    'httpService',

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
        '$stateProvider', '$urlRouterProvider', 'routeConst', function ($stateProvider, $urlRouterProvider, routeConst) {
            //$stateProvider.state('contacts', {
            //    url: '/contacts',
            //    template: '<h1>My Contacts</h1>'
            //});
            
            $stateProvider.state('tab_view', {
                url: '/',
                templateUrl: 'common/views/tabView.html'
            }).state(routeConst.basis_pndl_analysis, {
                url: 'basis_pndl_analysis/:tfId',
                templateUrl: 'basis_pndl_analysis/main.html'
            }).state(routeConst.tf_calculator, {
                url: 'tf_calculator/:tfId',
                templateUrl: 'tf_calculator/main.html'
            })
            ;
            
            // console.log("angular.module: tfcalculator config created.");
            // catch all route
            // send users to the form page 
            $urlRouterProvider.otherwise('/basis_pndl_analysis/');
        }
    ]).config(function (uiSelectConfig) {
        uiSelectConfig.theme = 'select2';
        uiSelectConfig.resetSearchInput = false;
        uiSelectConfig.appendToBody = true;
    });
    
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




