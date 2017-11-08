// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['angular', 'app'], function (angular, appModule) {
    "use strict";
    
    appModule.service('commonService', 
        ['$rootScope', '$location', 'servicePathConst', 'ngDialog', 
        function ($rootScope, $location, servicePathConst, ngDialog) {
            
            this.commonConfirmDialog = function (scope, message, title, onClickOkBtnHandler, onClickCancelBtnHandler, hideBtns) {
                var dialogScope = scope.$new(true);
                
                dialogScope.message = message;
                dialogScope.title = title ? title: " ";
                dialogScope.onClickOkBtnHandler = onClickOkBtnHandler;
                dialogScope.onClickCancelBtnHandler = onClickCancelBtnHandler;
                
                if (hideBtns) {
                    if (hideBtns.hideOk && hideBtns.hideOk === true) dialogScope.hideOkBtn = true;
                    if (hideBtns.hideCancel && hideBtns.hideCancel === true) dialogScope.hideCancelBtn = true;
                }
                
                dialogScope.confirmDialog = ngDialog.open({
                    template: 'common/templates/confirm_dialog.html',
                    controller: 'confirmDialogCtrl',
                    scope: dialogScope
                });
                
                return dialogScope.confirmDialog;
            };

            this.commonErrorDialog = function(scope, data, defaultMessage) {
                var dialogScope = scope.$new(true);
                
                dialogScope.message = "";
                dialogScope.title = " ";
                
                if (data.return_message) {
                    if (data.return_message.exceptionCode) dialogScope.title = data.return_message.exceptionCode;
                    
                    if (data.return_message.exceptionName) dialogScope.message += data.return_message.exceptionName + "\r\n";
                    
                    if (data.return_message.exceptionMessage) dialogScope.message += data.return_message.exceptionMessage + "\r\n";
                    
                    if (data.return_message.stacktrace) dialogScope.stacktrace = data.return_message.stacktrace;
                }                
                
                if (dialogScope.message === "") dialogScope.message = defaultMessage;
                
                dialogScope.hideCancelBtn = true;
                
                dialogScope.confirmDialog = ngDialog.open({
                    template: 'common/templates/confirm_dialog.html',
                    controller: 'confirmDialogCtrl',
                    scope: dialogScope
                });
                
                return dialogScope.confirmDialog;
            };

            // 获取容器滚动条的宽度
            this.getScrollBarWidth = function (element) {
                var width = 0;
                if (element) {
                    width = element.scrollWidth || element.scrollBarWidth;
                }
                
                return width;
            };
            
            // Convert Data to ViewModel
            this.convertDataToVM = function (e, dataDefine, convertor) {
                dataDefine.forEach(function (item, index) {
                    if (!item.sourceField || item.sourceField.length === "") {
                        return;
                    }
                    
                    e[item.field] = e[item.sourceField];
                });
                
                if (convertor) e = convertor(e);
                
                return e;
            };
            
            /*
            * 获得时间差,时间格式为 年-月-日 小时:分钟:秒 或者 年/月/日 小时：分钟：秒
            * 其中，年月日为全格式，例如 ： 2010-10-12 01:00:00
            * 返回精度为：秒，分，小时，天
            */
            this.getDateDiff = function (startTime, endTime, diffType) {
                try {
                    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式
                    startTime = startTime ? startTime.replace(/\-/g, "/") : undefined;
                    endTime = endTime ? endTime.replace(/\-/g, "/") : undefined;
                    
                    //将计算间隔类性字符转换为小写
                    diffType = diffType.toLowerCase();
                    
                    var sTime = startTime ? new Date(startTime) : new Date(); //开始时间
                    var eTime = endTime ? new Date(endTime) : new Date(); //结束时间
                    //作为除数的数字
                    var divNum = 1;
                    switch (diffType) {
                        case "second":
                            divNum = 1000;
                            break;
                        case "minute":
                            divNum = 1000 * 60;
                            break;
                        case "hour":
                            divNum = 1000 * 3600;
                            break;
                        case "day":
                            divNum = 1000 * 3600 * 24;
                            break;
                        default:
                            break;
                    }
                    
                    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
                } catch (ex) {
                    return "--";
                }
            };
            
            this.findFromArrayBy = function (array, id, prop) {
                if (!array || !(array instanceof Array) || !id) return undefined;
                
                if (!prop) prop = "id";
                
                for (var i = 0; i < array.length; i++) {
                    if (array[i][prop] === id) return array[i];
                }
                
                return undefined;
            };
            
            this.overwriteObject = function (sourceObj, targetObj) {
                if (!targetObj) targetObj = {};
                
                if (!sourceObj) return targetObj;
                
                for (var prop in sourceObj) {
                    if (sourceObj.hasOwnProperty(prop)) {
                        targetObj[prop] = sourceObj[prop];
                    }
                }
                
                return targetObj;
            };
            
            this.safeApply = function (scope, fn) {
                var phase = scope.$root.$$phase;
                if (phase === '$apply' || phase === '$digest') {
                    if (fn && (typeof (fn) === 'function')) {
                        fn();
                    }
                } else {
                    try {
                        scope.$apply(fn);
                    } catch (e) {

                    } 
                }
            };
            
            
            // cloneObj
            function cloneFunc(myObj) {
                if (typeof (myObj) != 'object') return myObj;
                if (myObj == null) return myObj;
                var myNewObj = new Object();
                for (var i in myObj)
                    if (myObj.hasOwnProperty(i)) myNewObj[i] = clone(myObj[i]);
                return myNewObj;
            };
            
            this.cloneObj = cloneFunc;
            
            // getPropertyX
            function getPropertyXFunc(obj, prop) {
                if (!obj) return undefined;
                
                if (!prop || prop.indexOf(".") < 0) return obj[prop];
                
                var arr = prop.split(".");
                var firstProp = arr.shift();
                
                if (!obj.hasOwnProperty(firstProp)) return undefined;
                
                return getPropertyXFuncArr(obj[firstProp], arr);
            };
            
            function getPropertyXFuncArr(obj, propArr) {
                if (!propArr || !obj) return obj;
                
                var firstProp = propArr.shift();
                
                if (propArr.length === 0) return obj[firstProp];
                
                return getPropertyXFuncArr(obj[firstProp], propArr);
            };
            
            this.getPropertyX = getPropertyXFunc;
            
            // setProppertyX
            function setPropertyXFunc(obj, prop, value) {
                if (!obj) obj = {};
                
                if (prop.indexOf(".") < 0) return obj[prop] = value;
                
                var arr = prop.split(".");
                var firstProp = arr.shift();
                
                if(!obj[firstProp]) obj[firstProp] = {};
                
                setPropertyXFuncArr(obj[firstProp], arr, value);
                
                return obj;
            };

            function setPropertyXFuncArr(obj, propArr, value) {
                if (!propArr) return;
                
                var firstProp = propArr.shift();

                if (propArr.length === 0) obj[firstProp] = value;
                else {
                    if (!obj[firstProp]) obj[firstProp] = {};
                    setPropertyXFuncArr(obj[firstProp], propArr, value);
                }
            };
            
            this.setProppertyX = setPropertyXFunc;
        }]);
});

