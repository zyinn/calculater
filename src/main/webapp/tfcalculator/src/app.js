define(
    [
        'angular', 

        'bootstrap',

        'ngAnimate',
        'uiRoute',
        'ngDialog',
        'ngSelect',
        'ui-bootstrap-tpls',

        'ngNumberic'
    ], function (angular) {
        'use strict';
        
        var module = angular.module('tfcalculator', [
            'ngAnimate',
            'ui.router',
            'ngDialog',
            'ui.select',
            'ui.bootstrap',

            'purplefox.numeric'
        ]).constant('servicePathConst', {
            // websocket
            ws_root: "http://localhost:1337/dataPush",
            
            // 不设置： 使用页面所在域
            // common service path
            service_api_root: 'http://172.16.87.6:8210/tfcalculator/tf/calculator',
            // service_api_root: '/tfcalculator/tf/calculator',
            
            
            // service_api_root: location.pathname + '/../../../tf/calculator',
            // service_api_root: 'service',
            
            
            //【dev-6】
            // 172.16.86.25 迁移至新的IP地址 172.16.87.6
            // 不设置： 使用页面所在域
            // auth_server: 'http://172.16.86.25:8002/apigw',
            // auth_server: 'http://172.16.87.4:8014',
            // auth_server: 'http://172.16.12.71:9090'
            
            // get_futures_contract_list : "/service/tfcalculator/futuresContractList"
            get_futures_contract_list : "/initial",
            post_futures_contract_list_by_tfId : "/initial/tfId",
            
            post_update_futures_price : "/futurePrice",
            post_do_calculator : "/param", 
            
            post_yieldType_update : "/yieldType",
            
            post_bond_search : "/bond",
            
            
            post_bond_update : "/bondUpdate"


     
        });
        
        console.log("angular.module: tfcalculator created.");
        
        return module;
    });

