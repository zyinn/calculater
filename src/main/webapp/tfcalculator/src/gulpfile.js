
// ReSharper disable Es6Feature    
//引入gulp  
const gulp = require('gulp'),
        merge = require('merge'),
        path = require('path'),
        concat = require('gulp-concat'),
        clean = require('gulp-clean'),
        gulpCss = require('gulp-clean-css'),
        rename = require('gulp-rename'),
        uglify = require('gulp-uglify'),
        streamify = require('gulp-streamify'),
        amdOptimize = require("amd-optimize"),
        source = require('vinyl-source-stream');

const BASE_PATH = "../dist";
// ReSharper restore Es6Feature

gulp.task('clean', function (cb) {
    gulp.src([path.join(BASE_PATH)], { read: false })
        .pipe(clean({ force: true }));
    
    cb();
});

gulp.task('copyTask', ['clean'], function (cb) {
    gulp.src([
        "../bower_components/metro/build/fonts/*.*",
        "../bower_components/bootstrap/fonts/*.*"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib/fonts")));
    
    gulp.src([
        "../bower_components/jquery-ui/**/*.*"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib/jquery-ui")));
    
    gulp.src([
        "../bower_components/requirejs/require.js"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib")));
    
    gulp.src([
        "../bower_components/bootstrap/dist/css/bootstrap.css.map"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "lib/style")));
    
    gulp.src([
        "common/**/*.html"
    ]).pipe(gulp.dest(path.join(BASE_PATH, "common")));
    
    gulp.src([
        "index.min.html"
    ])
        .pipe(rename("index.html"))
        .pipe(gulp.dest(path.join(BASE_PATH)));
    
    // basis_pndl_analysis
    gulp.src([
        "basis_pndl_analysis/controllers/*.*"
    ]).pipe(gulp.dest(path.join(BASE_PATH, 'basis_pndl_analysis/controllers')));
    
    gulp.src([
        "basis_pndl_analysis/views/*.*"
    ]).pipe(gulp.dest(path.join(BASE_PATH, 'basis_pndl_analysis/views')));
    
    gulp.src([
        "basis_pndl_analysis/main.min.html"
    ])
        .pipe(rename("basis_pndl_analysis/main.html"))
        .pipe(gulp.dest(path.join(BASE_PATH)));
    
    // tc
    gulp.src([
        "tf_calculator/main.min.html"
    ])
        .pipe(rename("tf_calculator/main.html"))
        .pipe(gulp.dest(path.join(BASE_PATH)));
    
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
            
            bootstrap: "../bower_components/bootstrap/dist/js/bootstrap",
            
            echarts: "common/scripts/echarts",
            spin: "common/scripts/spin",
            
            angular: "../bower_components/angular/angular",
            uiRoute: "../bower_components/angular-ui-router/release/angular-ui-router",
            ngAnimate: "../bower_components/angular-animate/angular-animate",
            ngUiBootstrap: "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
            ngDialog: "../bower_components/ng-dialog/js/ngDialog",
            
            ngSelect: "../bower_components/ui-select/dist/select",
            ngDate: "../bower_components/angular-ui-date/dist/date",
            ngNumberic: "../bower_components/angular-numeric-directive/src/numeric-directive",
            'ui-bootstrap-tpls': "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
            
            DateUtils: "common/scripts/DateUtils", 
            StringUtils: "common/scripts/StringUtils", 
            
            app: "app",
            
            percentFilter: "common/filters/percentFilter",
            currencyUnitFilter: "common/filters/currencyUnitFilter",
            
            customSelect0Directive: "common/directives/customSelect0Directive",
            datePickerDirective: "common/directives/datePickerDirective",
            percentValueDirective: "common/directives/percentValueDirective",
            currencyValueDirective: "common/directives/currencyValueDirective",
            
            directiveUtilService: "common/services/directiveUtilService",
            httpService: "common/services/httpService",
            commonService: "common/services/commonService",
            
            confirmDialogCtrl: "common/controllers/confirmDialogCtrl",
            tabViewCtrl: "common/controllers/tabViewCtrl"
        }, 
        shim: {
            jquery: { exports: 'jquery' },
            bootstrap: { deps: ['jquery'] },
            
            angular: { deps: ['jquery'], exports: 'angular' },
            uiRoute: { deps: ['angular'] }, 
            ngAnimate: { deps: ['angular'] },
            ngUiBootstrap: { deps: ['angular'] },
            ngDialog: { deps: ['angular'] },
            
            ngSelect: { deps: ['angular'] },
            ngDate: { deps: ['angular', 'jquery-ui/datepicker/zh-CN'] },
            ngNumberic: { deps: ['angular'] },
            'ui-bootstrap-tpls': { deps: ['angular'] },
            
            customSelect0Directive: { deps: ['StringUtils'] },
            
            app: { deps: ['angular'], exports: 'app' },
            
            commonService: { deps: ['app'] }
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
        // .pipe(gulp.dest(BASE_PATH))//输出保存  
        // .pipe(rename({ suffix: '.min' }))
        // .pipe(uglify())//压缩   
        .pipe(gulp.dest(path.join(BASE_PATH, 'basis_pndl_analysis')));                //输出保存  
    
    cb();
});

gulp.task('js_tc', ['copyTask'], function (cb) {
    
    var amdModuleOptimize = function () {
        return amdOptimize("tf_calculator/main", {
            baseUrl: ".",
            paths: {
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
        // .pipe(uglify())//压缩   
        .pipe(gulp.dest(path.join(BASE_PATH, 'tf_calculator')));                //输出保存  
    
    cb();
});


gulp.task('js', ['copyTask'], function (cb) {
    
    var amdOptimizeOption = function () {
        return amdOptimize("config", amdConfig);
    }();
    
    gulp.src(['config.js'])
        .pipe(amdOptimizeOption)
        .pipe(concat("config.js"))//合并 
        // .pipe(gulp.dest(BASE_PATH))//输出保存  
        // .pipe(rename({ suffix: '.min' }))
        // .pipe(uglify())//压缩   
        .pipe(gulp.dest(BASE_PATH));                //输出保存  
    
    cb();
});

gulp.task('css', ['js'], function () {
    var componentCss = [
        "../bower_components/bootstrap/dist/css/bootstrap.css",
        "../bower_components/jquery-ui/themes/base/datepicker.css",
        "../bower_components/metro/build/css/metro-icons.css",
        "../bower_components/ng-dialog/css/ngDialog.css",
        "../bower_components/ng-dialog/css/ngDialog-theme-default.css"
    ];
    
    gulp.src(componentCss)
        .pipe(concat("bower_components_style.css"))
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulp.dest(path.join(BASE_PATH, "lib/style")));
    
    
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
    
    commonCss = [
        "basis_pndl_analysis/styles/color.css",
        "basis_pndl_analysis/styles/layout.css"
    ];
    
    gulp.src(commonCss)
        .pipe(concat("bplaStyle.css"))
        .pipe(gulp.dest(path.join(BASE_PATH, "basis_pndl_analysis/style")))
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulpCss())
        .pipe(gulp.dest(path.join(BASE_PATH, "basis_pndl_analysis/style")));

    commonCss = [
        "tf_calculator/styles/color.css",
        "tf_calculator/styles/layout.css"
    ];
    
    gulp.src(commonCss)
        .pipe(concat("tcStyle.css"))
        .pipe(gulp.dest(path.join(BASE_PATH, "tf_calculator/style")))
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulpCss())
        .pipe(gulp.dest(path.join(BASE_PATH, "tf_calculator/style")));

});

gulp.task('default', ['copyTask', 'js', 'js_bpla', 'js_tc', 'css']);