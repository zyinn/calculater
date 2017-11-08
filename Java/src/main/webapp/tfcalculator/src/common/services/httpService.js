/**
 * Created by weilai on 2016/02/16.
 * 2016/05/25 Updated 报文结构标准化
 */

define(['angular', 'app'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    var HTTP_SUCCESS = "success";
    
    appModule.service('httpService', ['$http', '$location', 'servicePathConst', 
        function ($http, $location, servicePathConst) {
            
            // Http
            this.http = function (method, url, params, succeedCallback, failedCallback) {
                var fullUrl = servicePathConst.service_api_root ? servicePathConst.service_api_root + url : url;
                
                $http({ method: method, url: fullUrl, dataType: 'JSON', params: params }).then(function (data, status, headers, config) {
                    if (!data) {
                        if (failedCallback) failedCallback(data, status);
                        return;
                    }

                    if (data.return_code === -1) {
                        console.log(JSON.stringify(data));
                        console.log(JSON.stringify(config));
                        
                        if (failedCallback) {
                            failedCallback(data, status);
                        }

                        return;
                    }

                    if (succeedCallback) {
                        if (data.result) {
                            if (data.result instanceof Array && data.result.length === 1) {
                                succeedCallback(data.result[0], status, headers, config);
                            } else {
                                succeedCallback(data.result, status, headers, config);
                            }
                        } else {
                            succeedCallback(data, status, headers, config);
                        }
                    }

                }, function (data, status, headers, config) {
                    console.log(JSON.stringify(data));
                    console.log(JSON.stringify(config));
                    
                    if (failedCallback) {
                        failedCallback(data, status);
                    }
                });
            };
            
            // Get Method
            this.getService = function (url, params, succeedCallback, failedCallback) {
                var config = { params : params };
                
                var fullUrl = servicePathConst.service_api_root? servicePathConst.service_api_root + url : url;
                
                $http.get(fullUrl, config).success(function (data, status, headers, config) {
                    if (!data) {
                        if (failedCallback) failedCallback(data, status);
                        return;
                    }
                    
                    if (data.return_code === -1) {
                        console.log(JSON.stringify(data));
                        console.log(JSON.stringify(config));
                        
                        if (failedCallback) {
                            failedCallback(data, status);
                        }
                        
                        return;
                    }
                    
                    if (succeedCallback) {
                        if (data.result) {
                            if (data.result instanceof Array && data.result.length === 1) {
                                succeedCallback(data.result[0], status, headers, config);
                            } else {
                                succeedCallback(data.result, status, headers, config);
                            }
                        } else {
                            succeedCallback(data, status, headers, config);
                        }
                    }

                }).error(function (data, status, headers, config) {
                    console.log(JSON.stringify(data));
                    console.log(JSON.stringify(config));
                    
                    if (failedCallback)
                        failedCallback(data, status);
                });
            };
            
            // Post Method
            this.postService = function (url, params, succeedCallback, failedCallback) {
                var fullUrl = servicePathConst.service_api_root ? servicePathConst.service_api_root + url : url;
                
                $http.post(fullUrl, params).success(function (data, status, headers, config) {
                    if (!data) {
                        if (failedCallback) failedCallback(data, status);
                        return;
                    }
                    
                    if (data.return_code === -1) {
                        console.log(JSON.stringify(data));
                        console.log(JSON.stringify(config));
                        
                        if (failedCallback) {
                            failedCallback(data, status);
                        }
                        
                        return;
                    }
                    
                    if (succeedCallback) {
                        if (data.result) {
                            if (data.result instanceof Array && data.result.length === 1) {
                                succeedCallback(data.result[0], status, headers, config);
                            } else {
                                succeedCallback(data.result, status, headers, config);
                            }
                        } else {
                            succeedCallback(data, status, headers, config);
                        }
                    }
                }).error(function (data, status, headers, config) {
                    console.log(JSON.stringify(data));
                    console.log(JSON.stringify(config));
                    
                    if (failedCallback)
                        failedCallback(data, status);
                });
            };
            
            

        }]);
});
