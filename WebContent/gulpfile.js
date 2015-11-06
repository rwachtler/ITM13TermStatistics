/**
 * @description GulpJS setup for ITM13TermStatistics project
 * @author Ramis Wachtler
 * @copyright (c) 2015 Ramis Wachtler
 **/

var gulp = require('gulp');

// Modules
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var less = require('gulp-less');
var cssMin = require('gulp-minify-css');

/**
* Watches task, defines all watchers
**/
gulp.task('WATCH', function () {
    gulp.watch('css/*.css', ['MINIFY_CSS']);
    gulp.watch('js/*.js', ['MINIFY_JS']);
});

gulp.task('default', ['WATCH'] , function () {
    console.log('Default running...');
});

/**
* Building LESS resources
**/
gulp.task('BUILD_LESS', function() {
    return gulp.src('css/*.less')
        .pipe(less())
        .pipe(gulp.dest('css'));
});

/**
* Minify CSS resources but build LESS resources before
**/
gulp.task('MINIFY_CSS', ['BUILD_LESS'], function() {
    return gulp.src(['css/*.css', '!css/*.min.css'])
        .pipe(rename({suffix : '.min'}))
        .pipe(cssMin())
        .pipe(gulp.dest('css'));
});

/**
* Minify JS resources
**/
gulp.task('MINIFY_JS', function() {
    return gulp.src(['js/*.js', '!js/*.min.js'])
        .pipe(rename({suffix : '.min'}))
        .pipe(uglify())
        .pipe(gulp.dest('js'));
});