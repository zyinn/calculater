/**
 * Created by weilai on 2016/09/28.
 */

define(['angular', 'app', 'jquery', 'directiveUtilService'], function (angular, appModule) {
    // ReSharper disable once InconsistentNaming
    
    appModule.directive('customSelect1Directive', ['$sce', 'directiveUtilService', function ($sce, directiveUtilService) {
            
            var injectedCtrl = ['$scope', function ($scope) {
                    
                    var initView = function () {
                        $scope.idSeq = 0;
                        
                        while (true) {
                            if ($("cut-off-select-1-" + $scope.idSeq).length > 0) $scope.idSeq++;
                            else break;
                        }

                    }();

                }];
            
            
            
            
            
            var pointStr = "{0},{1}";
            
            function createJqWithNS(tagName) {
                var element = $(document.createElementNS("http://www.w3.org/2000/svg", tagName));
                
                if (tagName === "svg") {
                    element.attr({
                        xmln: "http://www.w3.org/2000/svg",
                        version: "1.1"
                    });
                }
                
                return element;
            };
            
            return {
                restrict: 'A',
                replace: true,
                controller: injectedCtrl,
                compile: function (el, attr) {
                    return {
                        pre: function preLink(scope, element, attributes) {
                            if (!directiveUtilService.check("customSelect1Directive", element, "SELECT")) return;
                            
                            // var selectWidth = parseFloat(element.css("width").replace("px"));
                            var selectWidth = element[0].clientWidth;
                            // var selectHeight = parseFloat(element.css("height").replace("px"));
                            var selectHeight = element[0].clientHeight;
                            
                            var points = [
                                pointStr.format(0, 0),
                                pointStr.format(selectWidth - selectHeight, 0),
                                pointStr.format(selectWidth - selectHeight, selectHeight),
                                pointStr.format(0, selectHeight),
                                pointStr.format(0, 0)
                            ];
                            
                            var maskId = "cut-off-select-" + seq++;
                            
                            var maskSvg = createJqWithNS("svg").css({
                                height: selectHeight,
                                width: selectWidth,
                                padding: 0
                            }).append(
                                createJqWithNS("defs").append(
                                    createJqWithNS("clipPath").attr("id", maskId).append(
                                        createJqWithNS("polygon").css("fill-rule", "evenodd").attr("points", points.join(" "))))
                            );
                            
                            // Old start
                            var div = $("<div class='customSelect0_expend' style='display:inline-flex;vertical-align: middle;'/>").attr("class", element.attr("class"));
                            element.parent().append(div.append(element));
                            // Old End
                            
                            maskSvg.append(
                                createJqWithNS("foreignObject").attr("clip-path", "url(#{0})".format(maskId)).attr({
                                    height: selectHeight,
                                    width: selectWidth
                                })
                            );
                            element.before(maskSvg);
                        },
                        post: function postLink(scope, element, attributes) {
                            if (!directiveUtilService.check("customSelect0Directive", element, "SELECT")) return;
                            
                            // var height = element.height();
                            var height = parseFloat(element[0].clientHeight);
                            var width = function () {
                                
                                //if (navigator.userAgent && navigator.userAgent.indexOf("Firefox/") >= 0) {
                                //    return (parseFloat(element.css("width").replace("px")) + parseFloat(element.css("height").replace("px"))) + "px";
                                //}
                                
                                // return element.width();
                                
                                return parseFloat(element[0].clientWidth);
                            }();
                            
                            var size = parseFloat(height);
                            
                            var rate = 0.35;
                            var points = [
                                pointStr.format(size * rate, size * rate),
                                pointStr.format(size * 0.5, size * (1.0 - rate)),
                                pointStr.format(size * (1.0 - rate), size * rate)
                            ];
                            
                            element.prev().before($("<button class='button-dark'/>").css({
                                "margin-left": (parseFloat(width) - size) + "px",
                                "margin-right": "-" + width + "px",
                                // "margin-left": "-" + size + "px",
                                "border-top-left-radius": 0,
                                "border-bottom-left-radius": 0,
                                border: "none",
                                height: height + "px",
                                width: height + "px",
                                padding: "0"
                            }).append(
                                createJqWithNS("svg").css({
                                    height: height + "px",
                                    width: height + "px",
                                }).append(createJqWithNS("polygon").css({
                                    "fill-rule": "evenodd",
                                    fill: "white"
                                }).attr("version", "1.1").attr("points", points.join(" ")))
                            ));
                            
                            element.parent().find("foreignObject").append(element);
                            element.attr("class", "");
                        }
                    };
                }
            };


        }]);
});
