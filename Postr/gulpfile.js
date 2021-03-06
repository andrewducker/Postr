'use strict';
var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var templateCache = require('gulp-angular-templatecache');
var crypto = require('crypto');
var fs = require('fs');
var replace = require('gulp-replace');
var del = require('del');
var merge = require('merge-stream');

function hashFile(name){
	var contents = fs.readFileSync(name,"utf8");
	return crypto.createHash('md5').update(contents).digest('hex');
}

gulp.task('cleanBuild',function(cb){
	del('build/**/*',cb);
});

gulp.task('delWarJs', function(cb){
	del('war/alljs*.js', cb);
});

gulp.task('delWarCss', function(cb){
	del('war/allcss*.css', cb);
});

gulp.task('delIndexHtml', function(cb){
	del('war/index.html', cb);
});

gulp.task('clean',['delWarCss','delWarJs','cleanBuild','delIndexHtml'],function(){});

gulp.task('controllers',['clean'], function() {
     return gulp.src(['Websrc/js/Controllers/init.js','Websrc/js/Controllers/*.js', 'Websrc/js/**/*.js'])
          .pipe(ngAnnotate())
          .pipe(concat('controllers.js'))
          .pipe(gulp.dest('build'));
});

gulp.task('miscjs',['clean'], function(){
	return gulp.src(['Websrc/js/misc/*.js'])
		.pipe(gulp.dest('build'));
});

gulp.task('copyTemplates', ['clean'],function(){
	return gulp.src('Websrc/templates/**/*')
	.pipe(gulp.dest('build/templates'));
});

gulp.task('templateUnpack',['copyTemplates'], function(){
	var createTestOutput = gulp.src('build/templates/sites/OutputTemplate/*')
	.pipe(replace('SITENAME',"Test Output"))
	.pipe(gulp.dest('build/templates/sites/TestOutput'));

	var createDreamwidthOutput= gulp.src('build/templates/sites/OutputTemplate/*')
	.pipe(replace('SITENAME',"Dreamwidth"))
	.pipe(gulp.dest('build/templates/sites/Dreamwidth'));
	
	var createLivejournalOutput= gulp.src('build/templates/sites/OutputTemplate/*')
	.pipe(replace('SITENAME',"Livejournal"))
	.pipe(gulp.dest('build/templates/sites/Livejournal'));

	return merge(createTestOutput, createDreamwidthOutput,createLivejournalOutput);
});

gulp.task('templates',['clean','templateUnpack'],function(){
	return gulp.src(['build/templates/**/*.html','!build/templates/sites/OutputTemplate/*'])
	.pipe(templateCache("templates.js",{module:"postrApp"}))
	.pipe(gulp.dest('build'));
});

gulp.task('alljs',['controllers','miscjs', 'templates'],function(){
	return gulp.src(['build/*.js'])
		.pipe(concat('alljs.js'))
//           .pipe(uglify())
		.pipe(gulp.dest('build'));
});

gulp.task('allcss',function(){
	return gulp.src(['Websrc/css/*.css'])
		.pipe(concat('allcss.css'))
		.pipe(gulp.dest('build'));
});

function cacheBust(fileName,extension){
	var oldFileName = 'build/'+fileName+"."+extension;
	var resultHash = hashFile(oldFileName);
	var newFileName = fileName+'-'+resultHash+'.'+extension;
	fs.renameSync(oldFileName,'war/'+newFileName);
	return newFileName;
}

gulp.task('cachebust',['alljs','allcss'],function(){
	var newJsFileName = cacheBust("alljs", "js");
	var newCssFileName = cacheBust("allcss","css");
	
	return gulp.src('Websrc/index.html')
		.pipe(replace('alljs\.js',newJsFileName))
		.pipe(replace('allcss\.css',newCssFileName))
		.pipe(gulp.dest('war'));
});

gulp.task('default',function(){
	gulp.watch('Websrc/**/*',['cachebust']);
});