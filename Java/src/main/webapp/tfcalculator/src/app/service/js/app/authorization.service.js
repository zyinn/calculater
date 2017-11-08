(function(){
    angular.module('cswap_app').service('AuthorizationService', AuthorizationService);
    AuthorizationService.$inject = ['$http', '$rootScope', '$timeout'];

    function AuthorizationService($http, $rootScope, $timeout){
        var service = this;

        service.login = function(user, successHandler, failureHandler){
            var request = {
                method: 'GET',
                url: 'http://localhost:8080/user/login',
                params: user
            };

            $http(request).then(successHandler, failureHandler);
        }
    }
})();