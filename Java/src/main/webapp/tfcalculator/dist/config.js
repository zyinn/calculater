define(['app'], function (appModule) {
    'use strict';
    
    // servicePathConst
    appModule.constant('servicePathConst', {
        // websocket
        ws_root: "http://localhost:1337/dataPush",
        
        // 不设置： 使用页面所在域
        // common service path
        // service_api_root: 'http://172.16.87.6:8210/tfcalculator',
        // service_api_root: 'http://172.16.89.6:8202/tfcalculator',
        // service_api_root: '/tfcalculator',
        
        service_api_root: location.pathname + '/../../..',
        // service_api_root: 'service',
        
        
        //【dev-6】
        // 172.16.86.25 迁移至新的IP地址 172.16.87.6
        // 不设置： 使用页面所在域
        // auth_server: 'http://172.16.86.25:8002/apigw',
        // auth_server: 'http://172.16.87.4:8014',
        // auth_server: 'http://172.16.12.71:9090'
        
        // get_futures_contract_list : "/service/tfcalculator/futuresContractList"
        get_futures_contract_list : "/tf/calculator/initial",
        post_futures_contract_list_by_tfId : "/tf/calculator/initial/tfId",
        
        post_update_futures_price : "/tf/calculator/futurePrice",
        post_do_calculator : "/tf/calculator/param", 
        
        post_yieldType_update : "/tf/calculator/yieldType",
        
        post_bond_search : "/tf/calculator/bond",
        
        post_bond_update : "/tf/calculator/bondUpdate",
        
        // tfcalculator
        // 画面初始化
        tc_post_init_tfcalculator: "/tfcalculator/tcInit",
        
        // 选择期货合约后获取相关信息
        tc_post_future_init: "/tfcalculator/doFutureInitCalculation",
        
        // 刷新期货价格
        tc_post_get_future_price: "/tfcalculator/doGetFuturePriceCalculation",
        
        // 一般计算接口
        tc_post_do_calculation: "/tfcalculator/doCalculation",
        
        // 选择债券类型后获取债券列表
        tc_post_do_change_bond_yield_type: "/tfcalculator/doGetBondYieldCalculation",
        
        // 自选券搜索接口
        tc_post_find_fix_interest_bonds: "/tfcalculator/findFixInterestBondsByPrefix",
        
        // 选择债券后获取债券信息
        tc_post_bond_changed: "/tfcalculator/bondChanged",

        // 导出Excel
        tc_post_excel_export: "/tfcalculator/excelExports"
    });
    
    // cssDependencyMap
    appModule.constant('cssDependencyMap', {
        
        //basis_pndl_analysis: [
        //    "../bower_components/bootstrap/dist/css/bootstrap.css",
        //    "../bower_components/jquery-ui/themes/base/datepicker.css",
        //    "../bower_components/metro/build/css/metro-icons.css",
        //    "../bower_components/ng-dialog/css/ngDialog.css",
        //    "../bower_components/ng-dialog/css/ngDialog-theme-default.css",
    
        //    "common/styles/dialog.css",
        //    "common/styles/common-color.css",
        //    "common/styles/common-layout.css",
        //    "common/styles/common-element.css",
    
        //    "basis_pndl_analysis/styles/color.css",
        //    "basis_pndl_analysis/styles/layout.css"
        //],
        
        basis_pndl_analysis: [
            "lib/style/basis_pndl_analysis/bower_components_style.min.css",
            "style/common.min.css",
            "basis_pndl_analysis/style/bplaStyle.min.css"
        ],
        
        //tf_calculator: [
        //    "../bower_components/bootstrap/dist/css/bootstrap.css",
        //    "../bower_components/jquery-ui/themes/base/datepicker.css",
        //    "../bower_components/metro/build/css/metro-icons.css",
        //    "../bower_components/ng-dialog/css/ngDialog.css",
        //    "../bower_components/ng-dialog/css/ngDialog-theme-default.css",
    
        //    "common/styles/dialog.css",
        //    "common/styles/common-color.css",
        //    "common/styles/common-layout.css",
        //    "common/styles/common-element.css",
        //],

        tf_calculator: [
            "lib/style/tf_calculator/bower_components_style.min.css",
            "style/common.min.css",
            "tf_calculator/style/tcStyle.min.css"
        ],

    });
    
    return appModule;
});

