var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var templateCache = require('gulp-angular-templatecache');
var rimraf = require('gulp-rimraf');
var crypto = require('crypto');
var fs = require('fs');
var replace = require('gulp-replace');

function hashFile(name){
	var contents = fs.readFileSync(name,"utf8");
	return crypto.createHash('md5').update(contents).digest('hex');
}


gulp.task('clean',function(){
	  gulp.src('build/**/*', { read: false }) // much faster
	    .pipe(rimraf());
	  
	    gulp.src( ['war/alljs*.js'], {read: false})
        .pipe(rimraf());
	    
	    gulp.src( ['war/index.html'], {read: false})
        .pipe(rimraf());
});

gulp.task('controllers',['clean'], function() {
      return gulp.src(['Websrc/js/Controllers/init.js','Websrc/js/Controllers/*.js'])
          .pipe(ngAnnotate())
          .pipe(concat('controllers.js'))
          .pipe(gulp.dest('build'));
});

gulp.task('miscjs',['clean'], function(){
	return gulp.src(['Websrc/js/misc/*.js'])
		.pipe(gulp.dest('build'));
});

gulp.task('templateUnpack',['clean'], function(){
	
});


gulp.task('templates',['clean'],function(){
	return gulp.src(['Websrc/templates/**/*.html'])
	.pipe(templateCache("templates.js",{module:"postrApp"}))
	.pipe(gulp.dest('build'));
});

gulp.task('alljs',['controllers','miscjs', 'templates'],function(){
	return gulp.src(['build/*.js'])
		.pipe(concat('alljs.js'))
//           .pipe(uglify())
		.pipe(gulp.dest('build'));
});

gulp.task('cachebust',['alljs'],function(){
	var oldFileName = 'build/alljs.js';
	var resultHash = hashFile(oldFileName);
	var newFileName = 'alljs-'+resultHash+'.js';
	fs.renameSync(oldFileName,'war/'+newFileName);

	gulp.src('Websrc/index.html')
		.pipe(replace('alljs\.js',newFileName))
		.pipe(gulp.dest('war'));
});

gulp.task('default',function(){
	gulp.watch('Websrc/**/*',['cachebust']);
});