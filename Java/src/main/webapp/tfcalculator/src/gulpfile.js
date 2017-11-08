
// ReSharper disable Es6Feature    
//引入gulp  
const gulp = require('gulp'),
    path = require('path'),
    concat = require('gulp-concat'),
    clean = require('gulp-clean'),
    gulpCss = require('gulp-clean-css'),
    rename = require('gulp-rename'),
    uglify = require('gulp-uglify'),
    streamify = require('gulp-streamify'),
    amdOptimize = require("amd-optimize"),
    source = require('vinyl-source-stream'),
    deletefile = require('gulp-delete-file'),
    exclude = require('gulp-ignore').exclude;

const BASE_PATH = "../dist";


const DataDefine = {
    cssTask : [
        {
            src: [
                "../bower_components/bootstrap/dist/css/bootstrap.css",
                "../bower_components/jquery-ui/themes/base/datepicker.css",
                "../bower_components/metro/build/css/metro-icons.css",
                "../bower_components/ng-dialog/css/ngDialog.css",
                "../bower_components/ng-dialog/css/ngDialog-theme-default.css"
            ], 
            outputPath: path.join(BASE_PATH, "lib/style/basis_pndl_analysis"), 
            outputFile: "bower_components_style.css"
        }, {
            src: [
                "basis_pndl_analysis/styles/color.css",
                "basis_pndl_analysis/styles/layout.css"
            ], 
            outputPath: path.join(BASE_PATH, "basis_pndl_analysis/style"), 
            outputFile: "bplaStyle.css"
        }, {
            src: [
                "../bower_components/jquery-ui/themes/base/datepicker.css",
                "../bower_components/metro/build/css/metro.css",
                "../bower_components/metro/build/css/metro-responsive.css",
                "../bower_components/metro/build/css/metro-icons.css",
                "../bower_components/ng-dialog/css/ngDialog.css",
                "../bower_components/ng-dialog/css/ngDialog-theme-default.css"
            ], 
            outputPath: path.join(BASE_PATH, "lib/style/tf_calculator"), 
            outputFile: "bower_components_style.css"
        }, {
            src: [
                "tf_calculator/styles/color.css",
                "tf_calculator/styles/layout.css"
            ], 
            outputPath: path.join(BASE_PATH, "tf_calculator/style"), 
            outputFile: "tcStyle.css"
        }
    ],
    
    copyTask: [
        { src: ["../bower_components/bootstrap/dist/css/bootstrap.css.map"], tgt: gulp.dest(path.join(BASE_PATH, "lib/style/basis_pndl_analysis")) },
        { src: ["../bower_components/bootstrap/dist/css/bootstrap.css.map"], tgt: gulp.dest(path.join(BASE_PATH, "lib/style/tf_calculator")) },
        {
            src: [
                "../bower_components/metro/build/fonts/*.*",
                "../bower_components/bootstrap/fonts/*.*"
            ], tgt: gulp.dest(path.join(BASE_PATH, "lib/style/fonts"))
        }
    ]
}
// ReSharper restore Es6Feature



gulp.task('clean', function (cb) {
    gulp.src([path.join(BASE_PATH)], { read: false })
        .pipe(clean({ force: true }))
        .on('end', cb);
});

gulp.task('copyTask_htmlMin', [], function (cb) {
    
    var htmlMin = ["index.min.html"];
    
    htmlMin.forEach(function (item, index) {
        gulp.src([path.join(BASE_PATH, item.replace(".min.html", ".html"))]).pipe(deletefile({ reg: /.*\.html/, deleteMatch: true }));
        gulp.src([item])
        .pipe(rename(item.replace(".min.html", ".html")))
        .pipe(gulp.dest(path.join(BASE_PATH)));
    });
    

    cb();
});

gulp.task('copyTask', ['copyTask_htmlMin'], function (cb) {
    
    DataDefine.copyTask.forEach(function (item, index) {
        gulp.src(item.src).pipe(item.tgt);
    });
    
    gulp.src([
        "../bower_components/jquery-ui/**/*.*"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib/jquery-ui")));
    
    gulp.src([
        "../bower_components/requirejs/require.js"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib")));
    
    gulp.src([
        "**/*.html",
        "!index.min.html",
        "!index.html"
    ]).pipe(gulp.dest(BASE_PATH));
    
    cb();
});

var amdConfig = function () {
    return {
        baseUrl: ".",
        paths: {
            jquery: "../bower_components/jquery/dist/jquery",
            'jquery-ui/core': "lib/jquery-ui/ui/core",
            'jquery-ui/datepicker': "lib/jquery-ui/ui/datepicker",
            'jquery-ui/datepicker/zh-CN': "lib/jquery-ui/ui/i18n/datepicker-zh-CN",
            
            metro: "../bower_components/metro/build/js/metro",
            // widget: "../bower_components/metro/js/widget",
            // 'widgets.hits': "common/scripts/widgets/hits",
            
            bootstrap: "../bower_components/bootstrap/dist/js/bootstrap",
            
            echarts: "common/scripts/echarts",
            spin: "common/scripts/spin",
            
            angular: "../bower_components/angular/angular",
            
            ngAnimate: "../bower_components/angular-animate/angular-animate",
            ngDialog: "../bower_components/ng-dialog/js/ngDialog",
            ngDate: "../bower_components/angular-ui-date/dist/date",
            ngNumberic: "../bower_components/angular-numeric-directive/src/numeric-directive",
            
            ngCss: "../bower_components/angular-css/angular-css",
            uiRoute: "../bower_components/angular-ui-router/release/angular-ui-router",
            uiSelect: "../bower_components/ui-select/dist/select",
            uib: "../bower_components/angular-bootstrap/ui-bootstrap",
            uibTpls: "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
            
            DateUtils: "common/scripts/DateUtils", 
            StringUtils: "common/scripts/StringUtils", 
            NumberUtils: "common/scripts/NumberUtils", 
            ArrayUtils: "common/scripts/ArrayUtils", 
            FileSaver: "common/scripts/FileSaver", 
            
            // config: "config",
            app: "app",
            
            percentFilter: "common/filters/percentFilter",
            currencyUnitFilter: "common/filters/currencyUnitFilter",
            filetimeToDatetime: "common/filters/filetimeToDatetime",
            
            customSelect0Directive: "common/directives/customSelect0Directive",
            datePickerDirective: "common/directives/datePickerDirective",
            percentValueDirective: "common/directives/percentValueDirective",
            currencyValueDirective: "common/directives/currencyValueDirective",
            repoRateDirective: "common/directives/repoRateDirective",
            
            searchBoxDirective: "common/directives/searchBoxDirective",
            
            directiveUtilService: "common/services/directiveUtilService",
            httpService: "common/services/httpService",
            commonService: "common/services/commonService",
            delayEventService: "common/services/delayEventService",
            
            confirmDialogCtrl: "common/controllers/confirmDialogCtrl",
            tabViewCtrl: "common/controllers/tabViewCtrl"
        }, 
        shim: {
            jquery: { exports: 'jquery' },
            
            spin: { deps: ['jquery'] },
            
            bootstrap: { deps: ['jquery'] },
            
            angular: { deps: ['jquery'], exports: 'angular' },
            
            ngAnimate: { deps: ['angular'] },
            ngUiBootstrap: { deps: ['angular'] },
            ngDialog: { deps: ['angular'] },
            ngDate: { deps: ['angular', 'jquery-ui/datepicker/zh-CN'] },
            ngNumberic: { deps: ['angular'] },
            
            ngCss: { deps: ['angular'] },
            uiRoute: { deps: ['angular'] }, 
            uiSelect: { deps: ['angular'] },
            uib: { deps: ['angular'] },
            uibTpls: { deps: ['angular'] },
            
            customSelect0Directive: { deps: ['StringUtils'] },
        
        // config: { deps: ['angular'], exports: 'config' },
        
        // commonService: { deps: ['config'] }
        },
        priority: [
            'angular'
        ]
    };
}();

gulp.task('js_bpla', ['copyTask'], function (cb) {
    var amdModuleOptimize = function () {
        return amdOptimize("basis_pndl_analysis/main", {
            baseUrl: ".",
            paths: {
                echarts: "common/scripts/echarts",
                spin: "common/scripts/spin",
                
                "basis_pndl_analysis.mainModule": "basis_pndl_analysis/mainModule",
                
                "basis_pndl_analysis.bplaService": "basis_pndl_analysis/bplaService",
                
                "basis_pndl_analysis.mainCtrl": "basis_pndl_analysis/controllers/mainCtrl",
                "basis_pndl_analysis.bplaFormCtrl": "basis_pndl_analysis/controllers/bplaFormCtrl",
                "basis_pndl_analysis.bplaChartCtrl": "basis_pndl_analysis/controllers/bplaChartCtrl"
            },
            shim: {
                "basis_pndl_analysis.bplaFormCtrl": { deps: ['basis_pndl_analysis.mainCtrl', 'spin'], exports: "basis_pndl_analysis.bplaFormCtrl" },
                "basis_pndl_analysis.bplaChartCtrl": { deps: ['basis_pndl_analysis.mainCtrl'], exports: "basis_pndl_analysis.bplaChartCtrl" }
            },
            // findNestedDependencies: true
        });
    }();
    
    gulp.src(['basis_pndl_analysis/main.js'])
        .pipe(amdModuleOptimize)
        .pipe(concat("main.js"))//合并 
        // .pipe(gulp.dest(path.join(BASE_PATH, 'basis_pndl_analysis')))
        // .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())//压缩   
        .pipe(gulp.dest(path.join(BASE_PATH, 'basis_pndl_analysis')));             //输出保存  
    
    cb();
});

gulp.task('js_tc', ['copyTask'], function (cb) {
    
    var amdModuleOptimize = function () {
        return amdOptimize("tf_calculator/main", {
            baseUrl: ".",
            paths: {
                spin: "common/scripts/spin",
                
                'tc.mainModule': "tf_calculator/mainModule",
                
                'tcService': "tf_calculator/tcService",
                
                'tc.mainCtrl': "tf_calculator/controllers/mainCtrl",
                'tc.headerCtrl': "tf_calculator/controllers/headerCtrl",
                'tc.tabViewCtrl': "tf_calculator/controllers/tabViewCtrl",
                'tc.resultListCtrl': "tf_calculator/controllers/resultListCtrl"
            },
            shim: {
                
            },
            // findNestedDependencies: true
        });
    }();
    
    gulp.src(['tf_calculator/main.js'])
        .pipe(amdModuleOptimize)
        .pipe(concat("main.js"))//合并 
        // .pipe(gulp.dest(BASE_PATH))//输出保存  
        // .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())//压缩   
        .pipe(gulp.dest(path.join(BASE_PATH, 'tf_calculator')));                //输出保存  
    
    cb();
});


gulp.task('js', ['copyTask'], function (cb) {
    
    var amdOptimizeOption = function () {
        return amdOptimize("main", amdConfig);
    }();
    
    gulp.src(['main.js'])
        .pipe(amdOptimizeOption)
        .pipe(exclude('config.js'))
        .pipe(concat("main.js"))//合并 
        // .pipe(gulp.dest(BASE_PATH))//输出保存  
        // .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())//压缩   
        .pipe(gulp.dest(BASE_PATH));                //输出保存  
    
    gulp.src([
        "configs/config.release.js"
    ])
    .pipe(rename("config.js"))
    .pipe(gulp.dest(path.join(BASE_PATH)));
    
    cb();
});

gulp.task('css', ['js'], function () {
    
    DataDefine.cssTask.forEach(function (item, index) {
        gulp.src(item.src)
        .pipe(concat(item.outputFile))
        .pipe(gulp.dest(item.outputPath))
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulpCss())
        .pipe(gulp.dest(item.outputPath));
    });
    
    var commonCss = [
        "common/styles/dialog.css",
        "common/styles/common-color.css",
        "common/styles/common-layout.css",
        "common/styles/common-element.css"
    ];
    
    gulp.src(commonCss)
        .pipe(concat("common.css"))
        .pipe(gulp.dest(path.join(BASE_PATH, "style")))
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulpCss())
        .pipe(gulp.dest(path.join(BASE_PATH, "style")));

});

gulp.task('default', ['copyTask_htmlMin', 'copyTask', 'js', 'js_bpla', 'js_tc', 'css']);


