(function(){
    'use strict';

    angular.module('cswap_app', ['ngRoute']).config(config);

    config.$inject = ['$routeProvider'];

    function config($routeProvider){
        $routeProvider.when('/login', {
            controller: 'LoginController',
            templateUrl: './login/login.view.html',
            controllerAs: 'vm'
        }).when('/view', {
            templateUrl: './view.html',
            controllerAs: 'vm'
        });
    }
})();