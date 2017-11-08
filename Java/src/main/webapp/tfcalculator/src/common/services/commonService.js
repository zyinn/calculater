// ReSharper disable once UseOfImplicitGlobalInFunctionScope
define(['angular', 'app'], function (angular, appModule) {
    "use strict";
    
    appModule.service('commonService', 
        ['$rootScope', '$location', 'servicePathConst', 'ngDialog', 
        function ($rootScope, $location, servicePathConst, ngDialog) {
            
            var thisService = this;
            
            this.commonConfirmDialog = function (scope, message, title, onClickOkBtnHandler, onClickCancelBtnHandler, hideBtns) {
                var dialogScope = scope.$new(true);
                
                dialogScope.message = message;
                dialogScope.title = title ? title: "提示";
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
            
            this.commonErrorDialog = function (scope, data, defaultMessage) {
                var dialogScope = scope.$new(true);
                
                dialogScope.message = "";
                dialogScope.title = "提示";
                
                if (data && data.return_message) {
                    // if (data.return_message.exceptionCode) dialogScope.title = data.return_message.exceptionCode;
                    
                    if (data.return_message.exceptionCode) console.debug("Service error code：" + data.return_message.exceptionCode);
                    
                    dialogScope.title = "提示";
                    
                    // if (data.return_message.exceptionName) dialogScope.message += data.return_message.exceptionName + "\r\n";
                    
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
            
            /**
             * Updated by weilai on 06/12/2016
             * @param {} scope 
             * @param {} vaildRule 
             * @returns {} 
             */
            this.checkViewModel = function (scope, vaildRule) {
                if (!vaildRule) return true;

                function onErrorFunction() {
                    thisService.commonErrorDialog(scope, undefined, vaildRule.errorMessage);
                    console.debug("Vaild failed on checkViewModel " + JSON.stringify(vaildRule));
                };

                if (vaildRule.length === 0 || !(vaildRule instanceof Array)) {
                    var value = undefined;
                    
                    switch (vaildRule.rule) {
                        case "required":
                            value = thisService.getPropertyX(scope, vaildRule.prop);
                            if (!value) {
                                onErrorFunction();
                                return false;
                            }
                            break;
                        case "requiredAny":
                            value = vaildRule.prop.map(function (e) { return thisService.getPropertyX(scope, e); });
                            if (!value || value.length === 0 || !(value instanceof Array)) break;
                            if (!value.findItem(function (e) { return e; })) {
                                onErrorFunction();
                                return false;
                            }
                            break;
                        case "regexp":
                            value = thisService.getPropertyX(scope, vaildRule.prop);
                            if (!value || !vaildRule.param || !vaildRule.param.pattern || vaildRule.param.pattern.constructor.name !== "RegExp") break;
                            if (!vaildRule.param.pattern.test(value)) {
                                onErrorFunction();
                                return false;
                            }
                            break;
                        case "range":
                        case "rangeOpen":
                            value = thisService.getPropertyX(scope, vaildRule.prop);
                            if (!value || !vaildRule.param || !vaildRule.param.pattern || !vaildRule.param.max || !vaildRule.param.min) break;
                            if ((+value) <= (+vaildRule.param.min) || (+value) >= (+vaildRule.param.max)) {
                                onErrorFunction();
                                return false;
                            }
                            break;
                        case "rangeClose":
                            value = thisService.getPropertyX(scope, vaildRule.prop);
                            if (!value || !vaildRule.param || !vaildRule.param.pattern || !vaildRule.param.max || !vaildRule.param.min) break;
                            if ((+value) < (+vaildRule.param.min) || (+value) > (+vaildRule.param.max)) {
                                onErrorFunction();
                                return false;
                            }
                            break;
                        default:
                            break;
                    };
                    
                    return true;
                }
                
                var result = true;
                
                vaildRule.forEach(function (item, index) {
                    
                    if (!result) return;
                    
                    result = thisService.checkViewModel(scope, item);
                });
                
                return result;
            };
            
            /**
             * viewmodel -> dto
             * Updated on 08/31/2016
             * @param {} vm 
             * @param {} define 
             * @returns {} 
             */
            this.getDto = getDtoFunc;
            
            function getDtoFunc(vm, define, dateFormat) {
                var target = undefined;
                
                if (!define) return target;
                
                target = {};
                
                for (var prop in define) {
                    if (!define.hasOwnProperty(prop)) continue;
                    
                    var vmProp = define[prop];
                    
                    if (typeof (vmProp) === "object") {
                        setPropertyXFunc(target, prop, getDtoFunc(vm, vmProp, dateFormat));
                    } else {
                        var val = getPropertyXFunc(vm, vmProp);
                        
                        if (val && val.constructor.name === "Date" && dateFormat) {
                            setPropertyXFunc(target, prop, val.formatDate(dateFormat));
                        } else {
                            setPropertyXFunc(target, prop, val);
                        }
                    }
                }
                
                return target;
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
            
            this.vaildateInputDom = function (element, value) {
                debugger;
                
                var pattern = target.attributes.getNamedItem("pattern");
                var max = target.attributes.getNamedItem("max");
                var min = target.attributes.getNamedItem("max");
                
                if (!pattern && !max && !min) return true;
                
                if (pattern && !new RegExp(pattern.nodeValue).match(value)) return false;
                
                if (max && (+max.nodeValue) < +value) return false;
                if (min && (+min.nodeValue) < +value) return false;
                
                return true;
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
            
            /**
             * getPropertyX
             * Updated on 08/31/2016
             * @param {} obj 
             * @param {} prop 
             * @returns {} 
             */
            function getPropertyXFunc(obj, prop) {
                if (!obj || !prop) return undefined;
                
                if (typeof (prop) === "object") return undefined;
                
                if (prop.indexOf(".") < 0) return obj[prop];
                
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
            
            /**
             * setPropertyX
             * Updated on 08/31/2016
             * @param {} obj 
             * @param {} prop 
             * @param {} value 
             * @returns {} 
             */            
            function setPropertyXFunc(obj, prop, value) {
                if (!obj) obj = {};
                
                if (!prop || typeof (prop) === "object") return undefined;
                
                if (prop.indexOf(".") < 0) return obj[prop] = value;
                
                var arr = prop.split(".");
                var firstProp = arr.shift();
                
                if (!obj[firstProp]) obj[firstProp] = {};
                
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
            
            this.setPropertyX = setPropertyXFunc;
        }]);
});

