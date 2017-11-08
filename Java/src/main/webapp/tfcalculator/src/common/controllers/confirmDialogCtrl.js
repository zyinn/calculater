// ReSharper disable once InconsistentNaming
define([
    'angular', 

    'app'
], function (angular, appModule) {
    'use strict';
    
    appModule.controller('confirmDialogCtrl', ['$scope', 'ngDialog', function ($scope, ngDialog) {

            $scope.onOkButtonClick = function () {
                if ($scope.onClickOkBtnHandler) {
                    $scope.onClickOkBtnHandler();
                }
                ngDialog.close($scope.confirmDialog);
            };

            $scope.onCancelButtonClick = function () {
                if ($scope.onClickCancelBtnHandler) {
                    $scope.onClickCancelBtnHandler();
                }
                ngDialog.close($scope.confirmDialog);
            };

            $scope.onCopyStacktraceToClipboard = function () {

                if ($scope.stacktrace) {

                    document.oncopy = function(e) {
                        e.clipboardData.setData('text/plain', $scope.stacktrace);
                        e.preventDefault();
                    };

                    document.execCommand("copy");
                }
            };

    }]);

});