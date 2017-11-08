(function(){
    'use strict';

    angular.module('cswap_app').controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$scope', '$timeout', '$location', '$window', 'AuthorizationService'];

    function LoginController($rootScope, $scope, $timeout, $location, $window, AuthorizationService){
        var vm = $scope;

        var getUserInput = function(){
            return vm.user;
        };

        var successHandler = function(response){
            console.log(response);

            $location.url("/view");
        };

        var failureHandler = function(response){
            console.log(response);
        };

        vm.login = function(){
            var user = getUserInput();

            AuthorizationService.login(user, successHandler, failureHandler);

        }
    }
})();