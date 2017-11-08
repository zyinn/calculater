/**
 * Create by WeiLai on 03/14/2016
 * 
 */
// ReSharper disable once InconsistentNaming
define([
    'basis_pndl_analysis.mainModule', 'echarts'
], function (mainModule, echarts) {
    'use strict';
    
    var legendAndTitleFontSize = 14;
    
    var chartLineColors = ['gold', '#00aaac', '#dc4444'];
    
    var chartMarkLineColor = 'green';
    var chartMarkLineBaseColor = 'white';
    
    var currentYield = "";
    
    var bplaDataDefine = function () {
        return {
            chart1: {
                title: {
                    text: '平仓日盈亏分析:',
                    left: 350,
                    textStyle: {
                        color: 'white',
                        fontWeight: '100',
                        fontSize: legendAndTitleFontSize
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { lineStyle: { color: 'white', width: '2' } }
                },
                legend: {
                    textStyle: { color: 'white' },
                    left: 540,
                    data: [
                        { name: '基差交易总盈亏', icon: 'square', textStyle: { fontSize: legendAndTitleFontSize, fontWeight: 100 } }
                    ]
                },
                xAxis: {
                    name: '平仓日\r\n收益率（%）',
                    nameTextStyle: { color: 'white' },
                    nameLocation: 'end',
                    splitNumber: 9,
                    splitLine: {
                        lineStyle: {
                            color: '#2c2c2c'
                        }
                    },
                    type: 'category',
                    axisLine: {
                        onZero: false,
                        lineStyle: { color: 'gray' }
                    }
                },
                yAxis: {
                    name: '总盈亏（元）',
                    nameLocation: 'start',
                    nameGap: '25',
                    nameTextStyle: { color: 'white' },
                    splitNumber: 6,
                    splitLine: {
                        lineStyle: {
                            color: '#2c2c2c'
                        }
                    },
                    type: 'value',
                    axisLine: {
                        lineStyle: { color: 'gray' }
                    },
                    axisLabel: {
                        formatter: function (value, index) {
                            var s1 = value + "";
                            var start = s1.indexOf(".");
                            
                            if (start <= 0) return value;
                            
                            if (start + 3 < s1.length) value.toFixed(2);
                            
                            return value.toFixed(1);
                        }
                    }
                },
                series: []
            },
            chart2: {
                title: {
                    text: '平仓日现券和期货价格:',
                    left: 350,
                    textStyle: {
                        color: 'white',
                        fontWeight: '100',
                        fontSize: legendAndTitleFontSize
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { lineStyle: { color: 'white', width: '2' } }
                },
                legend: {
                    textStyle: { fontSize: legendAndTitleFontSize, color: 'white' },
                    left: 540,
                    data: []
                },
                xAxis: {
                    name: '平仓日\r\n收益率（%）',
                    nameTextStyle: { color: 'white' },
                    type: 'category',
                    splitNumber: 3,
                    splitLine: {
                        lineStyle : {
                            color: '#2c2c2c'
                        }
                    },
                    nameLocation: 'end',
                    axisLine: {
                        onZero: false,
                        lineStyle : { color : 'gray' }
                    }
                },
                yAxis: {
                    name: '价格（元）',
                    nameLocation: 'start',
                    type: 'value',
                    nameGap: '25',
                    nameTextStyle: { color: 'white' },
                    splitNumber : 6,
                    splitLine: {
                        lineStyle : {
                            color: '#2c2c2c'
                        }
                    },
                    axisLine: {
                        lineStyle : { color : 'gray' }
                    }
                },
                series: []
            }
        };
    }();
    
    function dataAdd(datas) {
        if (!datas || !(datas instanceof Array)) return datas;
        return datas.map(function (e) { return { value: e, textStyle: { color: 'white' } }; });
    };
    
    function createlegendData(name) {
        return { name: name, icon: 'square', textStyle: { fontSize: legendAndTitleFontSize, fontWeight: 100 } };
    };
    
    function createSeries(datas, name, color) {
        
        name = name || "noname";
        color = color || "white";
        
        return {
            name: name,
            type: 'line',
            data: datas,
            smooth: true,
            symbol: 'circle',
            showSymbol: true,
            symbolSize: 10,
            itemStyle: {
                normal: { color: color }
            },
            lineStyle: {
                normal: { color: color }
            },
            markLine: {
                data: [],
                animationDuration: 300,
                label: {
                    normal: {
                        formatter: function (data) {
                            return "当前: " + currentYield + " %";
                        }
                    }
                },
                lineStyle: { normal: { color : chartMarkLineColor, width : 2, type: 'dotted' } }
            },
            //markPoint: {
            //    data: [],
            //    symbolSize: 70,
            //    label: {
            //        normal: {
            //            textStyle: { color: 'black', fontWeight: 400 }, 
            //            formatter: function (data) {
            //                if (data.name === 'base') return "开仓日\r\n收益率";
                            
            //                return '当前';
            //            }
            //        }
            //    }
            //}
        };
    };
    
    var moduleDef = ['$scope', '$injector', '$state', '$location', '$filter', 'commonService', 'bplaService',
        function ($scope, $injector, $state, $location, $filter, commonService, bplaService) {
            
            var percentFilter = $filter('percentFilter'),
                numberFilter = $filter('number'),
                currencyFilter = $filter('currency');
            
            var optionTemp = {
                chart1: undefined,
                chart2: undefined
            };
            
            var instance = {
                chart1: undefined,
                chart2: undefined
            };
            
            function getChartPointVm(index) {
                if (!$scope.vm) return undefined;
                if (!$scope.vm.responseDto) return undefined;
                if (!$scope.vm.responseDto.profitLossByYield) return undefined;
                
                //var target = function (source, innerIndex) {
                //    var i = 0;
                //    var target = undefined;
                //    for (var prop in source) {
                //        if (!source.hasOwnProperty(prop)) continue;
                
                //        if (i === innerIndex) {
                //            target = source[prop];
                //            break;
                //        } else {
                //            i++;
                //        }
                //    }
                //    return target;
                //}($scope.vm.responseDto.profitLossByYield, index);
                
                var target = $scope.vm.responseDto.profitLossByYield[index];
                
                if (!target) return target;
                
                target.alterColumn1 = $scope.vm.responseDto.bondCode;
                target.alterColumn2 = $scope.vm.selectedFutureContract && $scope.vm.selectedFutureContract.tfId;
                
                return target;
            };
            
            function onClickChartItem(params) {
                
                var targetChart = undefined;
                
                switch (params.color) {
                    case chartLineColors[0]:
                        $scope.detailPostition = "bottom";
                        targetChart = optionTemp.chart1;
                        break;
                    case chartLineColors[1]:
                        $scope.detailPostition = "top";
                        targetChart = optionTemp.chart2;
                        break;
                    case chartLineColors[2]:
                        $scope.detailPostition = "top";
                        targetChart = optionTemp.chart2;
                        break;
                    case chartMarkLineColor:
                        return;
                    case chartMarkLineBaseColor:
                        return;
                    default:
                        $scope.detailPostition = "bottom";
                        targetChart = optionTemp.chart1;
                        break;
                }
                
                //targetChart.series[0].markPoint.data = [];
                
                //targetChart.series[0].markPoint.data.push({ name: '当前', coord: [params.dataIndex, params.value] });
                
                // if (params.dataIndex || params.dataIndex === 0) refreshChart();
                
                $scope.isDetailShow = true;
                
                $scope.vm.selectChartPoint = getChartPointVm(params.dataIndex);
                $scope.vm.selectChartPointIndex = params.dataIndex;
                
                syncMarkPoint($scope.vm.selectChartPointIndex, $scope.vm.selectChartPoint);
                
                console.log("onClickChartItem");
                commonService.safeApply($scope, function () {
                    var input = $("#detail input")[0];
                    
                    if (input) {
                        input.focus();
                        input.select();
                    }
                });
            };
            
            function onMouseOverChartItem(params) {
                //if (params.dataIndex === 0) return;
                
                //var targetChart = undefined;
                
                //switch (params.color) {
                //    case chartLineColors[0]:
                //        $scope.detailPostition = "bottom";
                //        targetChart = optionTemp.chart1;
                //        break;
                //    case chartLineColors[1]:
                //        $scope.detailPostition = "top";
                //        targetChart = optionTemp.chart2;
                //        break;
                //    case chartLineColors[2]:
                //        $scope.detailPostition = "top";
                //        targetChart = optionTemp.chart2;
                //        break;
                //    default:
                //        $scope.detailPostition = "bottom";
                //        targetChart = optionTemp.chart1;
                //        break;
                //}
                
                //// targetChart.series[0].markPoint.data = [];
                
                //// targetChart.series[0].markPoint.data.push({ name: '当前', coord: [params.dataIndex, params.value] });
                
                //// if(params.dataIndex || params.dataIndex === 0) refreshChart();
                
                //// $scope.isDetailShow = true;
                
                //$scope.vm.selectChartPoint = getChartPointVm(params.dataIndex);
                //$scope.vm.selectChartPointIndex = params.dataIndex;
                
                //syncMarkPoint($scope.vm.selectChartPointIndex, $scope.vm.selectChartPoint);
                
                //console.log("onMouseOverChartItem");
                //commonService.safeApply($scope, function () {
                //    var input = $("#detail input")[0];
                    
                //    if (input) input.blur();
                //});
            };
            
            $scope.isPopupEditing = false;
            
            $scope.onClickChart = function () {
                // $scope.isDetailShow = false;
            };
            
            $scope.onClickClosePopup = function () {
                $scope.isDetailShow = false;
            };
            
            $scope.beginEditPopup = function (e) {
                var ex = e || window.event;
                var obj = ex.target || ex.srcElement;
                
                if (obj.attributes["editable"]) {
                    var attr = obj.attributes.getNamedItem("editable");
                    
                    var element = $("input[ng-model='" + attr.nodeValue + "']");
                    
                    if (element && element.length === 1) element.focus();
                    
                    $scope.isPopupEditing = true;
                } else {
                    $scope.isPopupEditing = false;
                }
            };
            
            $scope.enterAndCommit = function (event) {
                // console.log("Keycode : " + e.keyCode);
                var target = event.currentTarget;
                var attr = target.attributes.getNamedItem("ng-model");
                
                if (event && event.keyCode === 13) {
                    // $scope.endEditPopup();
                    
                    if (target && attr) {
                        target.placeholder = commonService.getPropertyX($scope, attr.nodeValue);
                    }
                    
                    $scope.doCalCulatePoint && $scope.doCalCulatePoint();
                }
            };
            
            $scope.beginEditPopup = function (event) {
                if (!event) return;
                
                var target = event.currentTarget;
                var attr = target.attributes.getNamedItem("ng-model");
                
                if (target && attr) {
                    target.placeholder = commonService.getPropertyX($scope, attr.nodeValue);
                }

                
            };
            
            $scope.endEditPopup = function (event) {
                if (!event) return;
                
                $scope.isPopupEditing = false;
                
                var target = event.currentTarget;
                var attr = target.attributes.getNamedItem("ng-model");
                
                if (target && attr) {
                    commonService.setPropertyX($scope, attr.nodeValue, target.placeholder);
                }

                // $scope.doCalCulatePoint && $scope.doCalCulatePoint();
            };
            
            $scope.commitFuturePrice = function () {
                $scope.doCalCulatePoint && $scope.doCalCulatePoint();
            };
            
            $scope.refreshFuturePrice = function () {
                
                $scope.refreshProfitLossByYieldPoint && $scope.refreshProfitLossByYieldPoint($scope.vm.selectChartPointIndex);
                
                $scope.vm.selectChartPoint = getChartPointVm($scope.vm.selectChartPointIndex);
                
                $scope.onVmChanged4Chart();
            };
            
            // 刷新图表
            function refreshChart() {
                function refresh(name) {
                    
                    if (!document.getElementById(name)) return;
                    
                    if (instance[name]) instance[name].dispose();
                    
                    delete instance[name];
                    
                    if ($("#" + name).length > 1) {
                        instance[name] = echarts.init($("#" + name)[0]);
                    } else {
                        instance[name] = echarts.init(document.getElementById(name));
                    }
                    
                    switch (name) {
                        case "chart1":
                            instance[name].off('click');
                            instance[name].on('click', onClickChartItem);
                            instance[name].off('mouseover');
                            instance[name].on('mouseover', onMouseOverChartItem);
                            break;
                        case "chart2":
                            instance[name].off('click');
                            instance[name].on('click', onClickChartItem);
                            instance[name].off('mouseover');
                            instance[name].on('mouseover', onMouseOverChartItem);
                            break;
                        default:
                            break;
                    }
                    
                    instance[name].setOption(optionTemp[name]);
                };
                
                refresh("chart1");
                refresh("chart2");
            };
            
            function updateChart() {
                function update(name) {
                    if (!document.getElementById(name)) return;
                    
                    instance[name].setOption(optionTemp[name]);
                };
                
                update("chart1");
                update("chart2");
            };
            
            // 同步游标
            function syncMarkPoint(pointXIndex, point) {
                
                function setMp(series, valueX) {
                    var targetIndex = undefined;
                    
                    if (!series || !series.markPoint || !series.markPoint.data || !(series.markPoint.data instanceof Array)) return;
                    
                    series.markPoint.data.forEach(function (item, index) {
                        if (item.name === 'current') targetIndex = index;
                    });
                    
                    var valueY = series.data[valueX];
                    var baseValueY = series.data[parseInt(series.data.length / 2) + 1];
                    
                    if (targetIndex >= 0) series.markPoint.data[targetIndex].coord = [valueX, valueY];
                    else {
                        series.markPoint.data.push({ name: 'base', coord: [parseInt(series.data.length / 2), baseValueY] });
                        series.markPoint.data.push({ name: 'current', coord: [valueX, valueY] });
                    }
                };
                
                function setMl(series, valueX, rangeY) {
                    if (!series || !(series instanceof Array)) return;
                    
                    // series.markLine.data = [[{ coord: [valueX, rangeY[0]], symbol: 'none' }, { coord: [valueX, rangeY.slice(-1)[0]], symbol: 'none' }]];
                    
                    var targetIndex = undefined;
                    
                    series.forEach(function (item, index) {
                        if (item.name === 'currentSelect') targetIndex = index;
                    });
                    
                    if (targetIndex >= 0) {
                        series[targetIndex].markLine.data = [[{ coord: [valueX, rangeY[0]], symbol: 'none' }, { coord: [valueX, rangeY.slice(-1)[0]], symbol: 'none' }]];
                    } else {
                        series.push({
                            name: 'currentSelect',
                            type: 'line',
                            data: [],
                            smooth: true,
                            symbol: 'circle',
                            showSymbol: true,
                            symbolSize: 10,
                            itemStyle: {
                                normal: { color: 'white' }
                            },
                            markLine: {
                                data: [[{ coord: [valueX, rangeY[0]], symbol: 'none' }, { coord: [valueX, rangeY.slice(-1)[0]], symbol: 'none' }]],
                                animationDuration: 300,
                                //label: {
                                //    normal: {
                                //        formatter: function(data) {
                                //            return "选择: " + currentYield + " %";
                                //        }
                                //    }
                                //},
                                lineStyle: { normal: { color: chartMarkLineBaseColor, width: 2, type: 'dotted' } }
                            }
                        });
                    }
                };
                
                function getRangeY(arr) {
                    if (!arr) return [0, 0];
                    
                    if (!arr.length) return [0, 0];
                    
                    return [arr[0].value, arr.slice(-1)[0].value];
                };
                
                if (pointXIndex >= 0) {
                    //optionTemp.chart1.series[0].markPoint.data = [];
                    //optionTemp.chart1.series[0].markPoint.data.push({ name: 'current', coord: [pointXIndex, pointInfo.totalProfitLoss] });
                    //optionTemp.chart2.series[0].markPoint.data = [];
                    //optionTemp.chart2.series[0].markPoint.data.push({ name: 'current', coord: [pointXIndex, pointInfo.bondPrice] });
                    //optionTemp.chart2.series[1].markPoint.data = [];
                    //optionTemp.chart2.series[1].markPoint.data.push({ name: 'current', coord: [pointXIndex, pointInfo.futurePrice] });
                    
                    //setMp(optionTemp.chart1.series[0], pointXIndex);
                    //setMp(optionTemp.chart2.series[0], pointXIndex);
                    //setMp(optionTemp.chart2.series[1], pointXIndex);
                    
                    var rangeY1 = getRangeY(optionTemp.chart1.yAxis.data);
                    var rangeY2 = getRangeY(optionTemp.chart2.yAxis.data);
                    
                    setMl(optionTemp.chart1.series, pointXIndex, rangeY1);
                    setMl(optionTemp.chart2.series, pointXIndex, rangeY2);
                    
                    var targetIndex = optionTemp.chart1.series.indexOfX(function (item) { return item.name !== 'currentSelect'; });
                    
                    if ($scope.vm && $scope.vm.responseDto) {
                        if (targetIndex >= 0) {
                            optionTemp.chart1.series[targetIndex].markLine.data = [[{ coord: [10, rangeY1[0]], symbol: 'none' }, { coord: [10, rangeY1.slice(-1)[0]], symbol: 'none' }]];
                        } else {
                            // optionTemp.chart1.series[targetIndex].markLine.data.push([{ coord: [10, rangeY1[0]], symbol: 'none' }, { coord: [10, rangeY1.slice(-1)[0]], symbol: 'none' }]);
                        }
                    }
                    
                    targetIndex = optionTemp.chart2.series.indexOfX(function (item) { return item.name !== 'currentSelect'; });
                    if ($scope.vm && $scope.vm.responseDto && optionTemp.chart2.series[targetIndex]) {
                        optionTemp.chart2.series[targetIndex].markLine.data = [[{ coord: [10, rangeY2[0]], symbol: 'none' }, { coord: [10, rangeY2.slice(-1)[0]], symbol: 'none' }]];
                    }
                    
                    updateChart();
                }
            };
            
            $scope.chartDomLoaded = function () {
                console.log("chartDomLoaded");
                // $scope.onVmChanged4Chart();
                refreshChart();
                
            };
            
            function setSeriesAndLegend(xBases, yBases1, yUnit1, yBases2, points) {
                function setBase(series, yBases) {
                    var targetIndex = series.indexOfX(function (item) { return item.name !== 'currentSelect'; });
                    
                    if ($scope.vm && $scope.vm.responseDto && series[targetIndex]) {
                        series[targetIndex].markLine.data = [
                            [{ coord: [10, yBases[0]], symbol: 'none' }, { coord: [10, yBases.slice(-1)[0]], symbol: 'none' }]
                        ];
                    }
                };
                
                function setLastSelected(series, yBases) {
                    
                    if (!$scope.vm || $scope.vm.selectChartPointIndex < 0) return;
                    
                    var targetIndex = series.indexOfX(function (item) { return item.name === 'currentSelect'; });
                    
                    if (targetIndex >= 0) {
                        series[targetIndex].markLine.data = [[{ coord: [$scope.vm.selectChartPointIndex, yBases[0]], symbol: 'none' }, { coord: [$scope.vm.selectChartPointIndex, yBases.slice(-1)[0]], symbol: 'none' }]];
                    } else {
                        series.push({
                            name: 'currentSelect',
                            type: 'line',
                            data: [],
                            smooth: true,
                            symbol: 'circle',
                            showSymbol: true,
                            symbolSize: 10,
                            itemStyle: {
                                normal: { color: 'white' }
                            },
                            markLine: {
                                data: [[{ coord: [$scope.vm.selectChartPointIndex, yBases[0]], symbol: 'none' }, { coord: [$scope.vm.selectChartPointIndex, yBases.slice(-1)[0]], symbol: 'none' }]],
                                animationDuration: 300,
                                lineStyle: { normal: { color: chartMarkLineBaseColor, width: 2, type: 'dotted' } }
                            }
                        });
                    }
                };
                
                optionTemp.chart1.series.push(createSeries(points.map(function (x) { return +((x.totalProfitLoss * Math.pow(10, yUnit1)).toFixed(4)); }), '基差交易总盈亏', chartLineColors[0]));
                var bondCodeName = function () {
                    if (!$scope.vm || !$scope.vm.responseDto) return "bondCode";
                    return $scope.vm.responseDto.bondCode || "bondCode";
                }();
                var futureContract = function () {
                    if (!$scope.vm || !$scope.vm.selectedFutureContract || !$scope.vm.selectedFutureContract.tfId) return "futureContract";
                    return $scope.vm.selectedFutureContract.tfId;
                }();
                
                // chart1
                setBase(optionTemp.chart1.series, yBases1);
                setLastSelected(optionTemp.chart1.series, yBases1);
                
                // chart2
                optionTemp.chart2.series.push(createSeries(points.map(function (x) { return x.bondPrice; }), bondCodeName, chartLineColors[1]));
                optionTemp.chart2.series.push(createSeries(points.map(function (x) { return x.futurePrice; }), futureContract, chartLineColors[2]));
                
                setBase(optionTemp.chart2.series, yBases2);
                setLastSelected(optionTemp.chart2.series, yBases2);
                
                optionTemp.chart2.legend.data.push(createlegendData(bondCodeName));
                optionTemp.chart2.legend.data.push(createlegendData(futureContract));
            };
            
            var peddingRefreshChart = false;
            
            $scope.$on('$stateChangeSuccess', function () {
                peddingRefreshChart && clearTimeout(peddingRefreshChart);
                
                peddingRefreshChart = setTimeout(function () {
                    refreshChart();
                }, 500);
            });
            
            $(window).resize(function () {
                console.log("resize");
                
                peddingRefreshChart && clearTimeout(peddingRefreshChart);
                
                peddingRefreshChart = setTimeout(function () {
                    refreshChart();
                }, 500);
            });
            
            // 更新chart2
            //$scope.updateChart2 = function (param) {
            
            //    if (!param || !param.key) return;
            
            //    var legendData = commonService.findFromArrayBy(optionTemp.chart2.legend.data, param.key);
            
            //    if (legendData) {
            //        legendData.name = param.value;
            //    } else {
            //        optionTemp.chart2.legend.data.push(
            //            { name: param.value, icon: 'square', textStyle: { fontSize: 16, fontWeight: 100 }, id: param.key }
            //        );
            //    }
            
            //    refreshChart();
            //};
            
            // 计算图表 时间轴的刻度跨度
            function createYAxis(datas, stepCount) {
                if (!datas) return undefined;
                
                if (datas.length === 0) return [0];
                
                var sorted = datas.sort(function (x1, x2) {
                    if (!angular.isNumber(x1)) return -1;
                    if (!angular.isNumber(x2)) return 1;
                    return x1 - x2;
                });
                
                stepCount = Math.abs(stepCount);
                
                // 最大值与最小值的差
                var maxDiff = sorted.slice(-1)[0] - sorted[0];
                
                // 中间值
                // var mid = Math.abs(maxDiff / 2);
                
                //var step = mid / (stepCount / 2 - 1);
                var step = Math.abs(maxDiff / (stepCount - 3));
                var precision = Math.pow(10, Math.abs(step) < 1 ? 0 - parseInt(1 / step).toString().length  : parseInt(step).toString().length - 1);
                
                if (maxDiff === 0) {
                    step = 1;
                } else {
                    step = precision * 5 >= step ? precision * 5 : precision * 10;
                    
                    // x1 or x5?
                    // step = precision;
                }
                
                // if (step < 1) step = 1;
                
                // 最大值或最小值与中间值的差
                //var midValue = (sorted.slice(-1)[0] - mid);
                //midValue -= midValue % step;
                
                var midValue = (sorted[0] + (sorted.slice(-1)[0])) / 2;
                midValue -= midValue % step;
                
                var target = [];
                for (var i = parseInt(0 - (stepCount / 2)); i < parseInt(stepCount / 2) + 1; i++) {
                    
                    if (precision < 1) {
                        var temp = midValue + step * i;
                        target.push(parseInt(temp / precision) / parseInt(1 / precision));
                    } else {
                        target.push(midValue + step * i);
                    }
                }
                
                return target;
            };
            
            function getY1Unit(arr) {
                if (!arr || arr.length < 0) return 0;
                
                var maxAbs = Math.abs(arr[0]);
                
                arr.forEach(function (item) {
                    if (maxAbs < Math.abs(item)) maxAbs = Math.abs(item);
                });
                
                if (maxAbs > Math.pow(10, 8)) {
                    optionTemp.chart1.yAxis.name = '总盈亏（亿元）';
                    return -8;
                }
                if (maxAbs > Math.pow(10, 4)) {
                    optionTemp.chart1.yAxis.name = '总盈亏（万元）';
                    return -4;
                }
                return 0;
            };
            
            $scope.onVmChanged4Chart = function () {
                
                optionTemp.chart1 = angular.copy(bplaDataDefine.chart1);
                optionTemp.chart2 = angular.copy(bplaDataDefine.chart2);
                
                var points = [];
                var xBases = [];
                var yBases1 = [];
                var yBases2 = [];
                
                // Y轴单位，0是元，万元是-4，亿是-8
                var yUnit1 = 0;
                
                // 设置x轴
                if ($scope.vm && $scope.vm.responseDto && $scope.vm.responseDto.profitLossByYield) {
                    //for (var xPoint in $scope.vm.responseDto.profitLossByYield) {
                    //    if ($scope.vm.responseDto.profitLossByYield.hasOwnProperty(xPoint)) {
                    //        // xBases.push(xPoint);
                    //        // xBases.push(xPoint * 100);
                    //        if ($scope.vm.responseDto.profitLossByYield.hasOwnProperty(xPoint)) {
                    //            points.push($scope.vm.responseDto.profitLossByYield[xPoint]);
                    //            xBases.push($scope.vm.responseDto.profitLossByYield[xPoint].yield);
                    //        }
                    //    }
                    //}
                    
                    
                    // 设置图表的当前收益率
                    currentYield = $scope.vm.responseDto.yield;
                    
                    $scope.vm.responseDto.profitLossByYield.forEach(function (item, index) {
                        points.push(item);
                        // xBases.push(item.yield);
                        // xBases.push(+percentFilter(item.yield, false));
                        // xBases.push(+(numberFilter(item.yield, 1)));
                        xBases.push((+item.yield).toFixed(1));
                        
                        // 准备popup上显示的属性
                        item.impactCost = $scope.vm.impactCost;
                        item.opYield = $scope.vm.responseDto.yield;
                        item.opBondNetPrice = $scope.vm.responseDto.bondNetPrice;
                        item.opFuturePrice = $scope.vm.responseDto.futurePrice;
                    });
                    
                    syncMarkPoint($scope.vm.selectChartPointIndex, $scope.vm.responseDto.profitLossByYield[$scope.vm.selectChartPointIndex]);
                }
                
                // 设置y轴
                yBases1 = createYAxis(points.map(function (x) { return +x.totalProfitLoss; }), 9);
                yUnit1 = getY1Unit(yBases1);
                if (yUnit1 !== 0) {
                    yBases1 = createYAxis(points.map(function (x) { return ((x.totalProfitLoss * Math.pow(10, yUnit1)).toFixed(2)); }), 9);
                }
                
                yBases2 = function () {
                    var bondPriceArray = points.map(function (x) { return +x.bondPrice; });
                    var futurePriceArray = points.map(function (x) { return +x.futurePrice; });
                    bondPriceArray.push.apply(bondPriceArray, futurePriceArray);
                    
                    return createYAxis(bondPriceArray, 9);
                }();
                
                setSeriesAndLegend(xBases, yBases1, yUnit1, yBases2, points);
                
                optionTemp.chart1.xAxis.data = dataAdd(xBases);
                optionTemp.chart2.xAxis.data = dataAdd(xBases);
                
                optionTemp.chart1.yAxis.data = dataAdd(yBases1);
                optionTemp.chart1.yAxis.min = yBases1[0];
                optionTemp.chart1.yAxis.max = yBases1.slice(-1)[0];
                optionTemp.chart2.yAxis.data = dataAdd(yBases2);
                optionTemp.chart2.yAxis.min = yBases2[0];
                optionTemp.chart2.yAxis.max = yBases2.slice(-1)[0];
                
                // tooltip.formatter 
                optionTemp.chart1.tooltip.formatter = function (params) {
                    var template = '收益率：{0}% <br/>' +
                        '{1} : {2} {3}<br/>' +
                        '点击 <button class="chart-tooltip-point" style="background:{4};"></button> 查看更多';
                    
                    if (!params[0]) return undefined;
                    
                    var yUnitName = function (yUnit1) {
                        switch (yUnit1) {
                            case 0: return '元';
                            case -4: return '万元';
                            case -8: return '亿元';
                            default: return '元';
                        }
                    }(yUnit1);
                    
                    var targetIndex = params.indexOfX(function (item) { return item.seriesName !== 'currentSelect'; });
                    
                    if ($scope.vm.responseDto && $scope.vm.responseDto.profitLossByYield[params[targetIndex].dataIndex])
                        return template.format($scope.vm.responseDto.profitLossByYield[params[targetIndex].dataIndex].yield,  
                        params[targetIndex].seriesName, params[targetIndex].value, yUnitName,
                        params[targetIndex].color);
                    else
                        return template.format(params[targetIndex].name, 
                        params[targetIndex].seriesName, params[targetIndex].value, yUnitName,
                        params[targetIndex].color);
                };
                
                optionTemp.chart2.tooltip.formatter = function (params) {
                    
                    var template = '收益率：{0}% <br/>' +
                        '{1} : {2} <br/>' +
                        '{3} : {4} <br/>' +
                        '点击 <button class="chart-tooltip-point" style="background:{5};"></button> <button class="chart-tooltip-point" style="background:{6};"></button> 查看更多';
                    
                    if (!params || !(params instanceof Array)) return "";
                    
                    var targetIndex = params.indexOfX(function (item) { return item.seriesName === 'currentSelect'; });
                    if (targetIndex >= 0 && targetIndex < params.length) params.splice(targetIndex, 1);
                    targetIndex = params.indexOfX(function (item) { return item.seriesName === 'currentSelect'; });
                    if (targetIndex >= 0 && targetIndex < params.length) params.splice(targetIndex, 1);
                    
                    if ($scope.vm.responseDto && $scope.vm.responseDto.profitLossByYield[params[0].dataIndex])
                        return template.format($scope.vm.responseDto.profitLossByYield[params[0].dataIndex].yield, 
                        params[0].seriesName, params[0].value,
                        params[1].seriesName, params[1].value,
                        params[0].color, params[1].color);
                    else
                        return template.format(params[0].name, 
                        params[0].seriesName, params[0].value,
                        params[1].seriesName, params[1].value,
                        params[0].color, params[1].color);
                };
                
                refreshChart();
            };
            
            // 页面初始化
            var init = function () {
                // refreshChart();
                
                $scope.onVmChanged4Chart();
                
                console.log("bplaChartCtrl $scope.id: " + $scope.$id);
            }();
           
        }];
    
    
    mainModule.controller('basis_pndl_analysis.bplaChartCtrl', [
        '$scope', '$injector', '$sce', '$location', '$filter', 'commonService', 'bplaService',
        function ($scope, $injector, $sce, $location, $filter, commonService, bplaService) {
            $injector.invoke(moduleDef, this, {
                '$scope': $scope.$parent,
                '$sce': $sce,
                '$location': $location,
                '$filter': $filter,
                'commonService': commonService,
                'bplaService': bplaService
            });
        }
    ]);
});