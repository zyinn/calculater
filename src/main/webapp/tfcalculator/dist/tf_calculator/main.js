define('tc.mainModule', [], function () {
    'use strict';
    var mainModule = angular.module('tfcalculator.tc', []);
    console.log('angular.module: tfcalculator.tc created.');
    return mainModule;
});
define('tcService', ['tc.mainModule'], function (mainModule) {
    'use strict';
    mainModule.service('tcService', [
        '$location',
        '$filter',
        'servicePathConst',
        'httpService',
        'commonService',
        function ($location, $filter, servicePathConst, httpService, commonService) {
        }
    ]);
});
define('tc.mainCtrl', ['tc.mainModule'], function (mainModule) {
    'use strict';
    var DATE_FORMAT = 'yyyy-MM-dd hh:mm:ss';
    var tcDataDefine = function () {
        return {
            bondTypeButtons: [
                {
                    value: 'A',
                    display: '可交割券'
                },
                {
                    value: 'B',
                    display: '未发国债'
                },
                {
                    value: 'C',
                    display: '自选券'
                },
                {
                    value: 'D',
                    display: '虚假券'
                }
            ],
            calTgtTypeButtons: [
                {
                    value: 'A',
                    display: 'IRR/基差'
                },
                {
                    value: 'B',
                    display: '债券价格'
                },
                {
                    value: 'C',
                    display: '期货价格'
                }
            ],
            yieldTypeButtons: [
                {
                    value: 'ofr',
                    display: 'Ofr'
                },
                {
                    value: 'bid',
                    display: 'Bid'
                },
                {
                    value: 'deal',
                    display: '成交'
                },
                {
                    value: 'cdc',
                    display: '中债'
                }
            ]
        };
    }();
    mainModule.controller('tc.mainCtrl', [
        '$scope',
        '$state',
        '$sce',
        '$location',
        'commonService',
        'tcService',
        'routeConst',
        function ($scope, $state, $sce, $location, commonService, tcService, routeConst) {
            $scope.bondTypeButtons = tcDataDefine.bondTypeButtons;
            $scope.calTgtTypeButtons = tcDataDefine.calTgtTypeButtons;
            $scope.yieldTypeButtons = tcDataDefine.yieldTypeButtons;
            $scope.onActiveButton = function (e, prop) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                if (obj && obj.nodeName === 'BUTTON') {
                    $scope[prop] = obj.value;
                    console.log($scope[prop]);
                }
            };
            var initView = function () {
                $scope.item = 'AAAA';
                $scope.vm = {};
                commonService.safeApply($scope);
            }();
        }
    ]);
});
define('tc.headerCtrl', ['tc.mainModule'], function (mainModule) {
    'use strict';
    var moduleDef = [
        '$scope',
        '$injector',
        '$state',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $state, $location, $filter, commonService, tcService) {
            $scope.debug = function () {
                var a = { a: true };
                var b = { a: null };
                var c = angular.merge(a, b);
                console.log(JSON.stringify(c));
            };
            var initView = function () {
                $scope.vm = {};
                commonService.safeApply($scope);
            }();
        }
    ];
    mainModule.controller('tc.headerCtrl', [
        '$scope',
        '$injector',
        '$sce',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $sce, $location, $filter, commonService, tcService) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$location': $location,
                '$filter': $filter,
                'commonService': commonService,
                'tcService': tcService
            });
        }
    ]);
});
define('tc.tabViewCtrl', ['tc.mainModule'], function (mainModule) {
    'use strict';
    var tcDataDefine = function () {
    }();
    var moduleDef = [
        '$scope',
        '$injector',
        '$state',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $state, $location, $filter, commonService, tcService) {
        }
    ];
    mainModule.controller('tc.tabViewCtrl', [
        '$scope',
        '$injector',
        '$sce',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $sce, $location, $filter, commonService, tcService) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$location': $location,
                '$filter': $filter,
                'commonService': commonService,
                'tcService': tcService
            });
        }
    ]);
});
define('tc.resultListCtrl', ['tc.mainModule'], function (mainModule) {
    'use strict';
    var tcDataDefine = function () {
    }();
    var mockData = {
        resultList: [
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ],
            [
                false,
                'TF1603',
                '150011.IB',
                '100.575',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA',
                'AAAAAA'
            ]
        ]
    };
    var moduleDef = [
        '$scope',
        '$injector',
        '$state',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $state, $location, $filter, commonService, tcService) {
            var initView = function () {
                $scope.resultList = mockData.resultList;
                commonService.safeApply($scope);
            }();
        }
    ];
    mainModule.controller('tc.resultListCtrl', [
        '$scope',
        '$injector',
        '$sce',
        '$location',
        '$filter',
        'commonService',
        'tcService',
        function ($scope, $injector, $sce, $location, $filter, commonService, tcService) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$location': $location,
                '$filter': $filter,
                'commonService': commonService,
                'tcService': tcService
            });
        }
    ]);
});
require.config({
    baseUrl: '.',
    paths: {
        'tc.mainModule': 'tf_calculator/mainModule',
        'tcService': 'tf_calculator/tcService',
        'tc.mainCtrl': 'tf_calculator/controllers/mainCtrl',
        'tc.headerCtrl': 'tf_calculator/controllers/headerCtrl',
        'tc.tabViewCtrl': 'tf_calculator/controllers/tabViewCtrl',
        'tc.resultListCtrl': 'tf_calculator/controllers/resultListCtrl'
    },
    shim: {}
});
define('tf_calculator/main', [
    'tc.mainModule',
    'tcService',
    'tc.mainCtrl',
    'tc.headerCtrl',
    'tc.tabViewCtrl',
    'tc.resultListCtrl'
], function (mainModule) {
    'use strict';
    mainModule.run([
        '$rootScope',
        '$urlRouter',
        function ($rootScope, $urlRouter) {
            $rootScope.$on('$locationChangeSuccess', function (evt) {
                evt.preventDefault();
                $urlRouter.sync();
            });
        }
    ]);
    return mainModule;
});