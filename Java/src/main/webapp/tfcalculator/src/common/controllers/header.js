'use strict';
/**
 * @ngdoc function
 * @name cswap_web_app.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the cswap_web_app
 */

// ReSharper disable InconsistentNaming
angular.module('cswap_web_app')
  .controller('HeaderCtrl', function ($scope, $location, SessionService, TService, AuthenticationService, CookieService) {
    var token = JSON.parse(SessionService.get("current_user"));
    if (token) {
        var user = token.user;
        
        // $scope.displayName = user.displayname;
        // Changed by WeiLai on 01/15/2016 error when user is null
        // 
        $scope.displayName = user?user.displayname: undefined;
    }
    $scope.states = {};
    $scope.states.activeItem = '';
    $scope.items = [{
            id: 'sys_setting',
            name: TService.T('sys_setting')
        }, {
            id: 'logout',
            name: TService.T('logout')
        }];
    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };
    $scope.setting_click = function (item) {
        
        //$scope.states.activeItem = item;
        //var ticket = { ticket: token.tokenID };
        //if (item === "logout") {
        //    AuthenticationService.logout(ticket).success(function (resp) {
        //        console.log(resp);
        //        if (resp.result == "success") {
        //            AuthenticationService.unCacheCookie("token");
        //            AuthenticationService.unCacheSession();
        //            $location.path('/login');
        //        } else {
        //            alert("Invalid logout!");
        //        }
        //    }).error(function () {
        //        alert("Invalid logout!");
        //    });
        //} 
        // Changed by WeiLai on 01/15/2016       
        // Bug: token == null 时无法返回登录页面
        $scope.states.activeItem = item;
        
        if (item === "logout" && token) {
            var ticket = { ticket: token.tokenID };
            
            AuthenticationService.logout(ticket).success(function (resp) {
                console.log(resp);
                if (resp.result === "success") {
                    AuthenticationService.unCacheCookie("token");
                    AuthenticationService.unCacheSession();
                    
                } else {
                    // alert("Invalid logout!");
                    console.log("Warning: Invalid logout!");
                }

                $location.path('/login');
            }).error(function () {
                alert("Invalid logout!");

                $location.path('/login');
            });
        } else if (item === "sys_setting" && token) {

        } else {
            $location.path('/login');
        }
    }
});
