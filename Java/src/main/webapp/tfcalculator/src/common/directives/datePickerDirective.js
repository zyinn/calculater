/**
 * Created by weilai on 2016/02/16.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('datePickerDirective', ['$sce', '$filter', 'directiveUtilService', function ($sce, $filter, directiveUtilService) {
            
            var dateFilter = $filter('date');
            return {
                restrict : 'A',
                replace : false,
                require: 'ngModel',  
                link: function (scope, elm, attrs, ctrl) {
                    
                    
  
                }, 
                compile: function (el, attr) {
                    return {
                        pre: function preLink(scope, element, attributes, ctrl) {
                            
                            function formatter(value) {
                                if (!value) return value;
                                
                                if (typeof (value) === "object" && value.constructor.name === "Date") return dateFilter(value, attributes.dateFormat);
                                else if (typeof (value) === "string") return dateFilter(new Date(value), attributes.dateFormat);
                                else return dateFilter(+value, attributes.dateFormat);
                            }
                            
                            function parser() {
                                // return ctrl.$modelValue;
                                return ctrl.$viewValue;
                            }
                            
                            ctrl.$formatters.push(formatter);
                            ctrl.$parsers.unshift(parser);
                            
                            if (!directiveUtilService.check("datePickerDirective", element, "INPUT")) return;
                            
                            var datepicker = element.datepicker();
                            
                            element.before(
                                $("<div style='white-space: nowrap; width:auto;'/>").attr("class", element.attr("class").replace("hasDatepicker", ""))
                            ).attr("class", "hasDatepicker").prev().append(element).append(
                                $("<span class='mif-calendar calendar-icon'/>").off("click").on('click', function () {
                                    // datepicker.datepicker("show");
                                    
                                    try {
                                        if ($("#ui-datepicker-div").css("display") === "none") {
                                            datepicker.datepicker("show");
                                        } else {
                                            datepicker.datepicker("hide");
                                        }
                                    } catch (e) {

                                    }
                                })
                            );
                        },  
                        post: function postLink(scope, element, attributes) {
                            
                        }
                    };
                }
            }
        }]);
});
