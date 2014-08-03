var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
concat = require('gulp-concat');
var uglify = require('gulp-uglify');

gulp.task('controllers', function() {
      return gulp.src(['Websrc/js/Controllers/init.js','Websrc/js/Controllers/*.js'])
          .pipe(ngAnnotate())
          .pipe(concat('controllers.js'))
          .pipe(gulp.dest('build'));
});

gulp.task('miscjs', function(){
	return gulp.src(['Websrc/js/misc/*.js'])
		.pipe(gulp.dest('build'));
});

gulp.task('alljs',['controllers','miscjs'],function(){
	return gulp.src(['build/*.js'])
		.pipe(concat('alljs.js'))
//           .pipe(uglify())
		.pipe(gulp.dest('war'));
});

gulp.task('default',function(){
	gulp.watch('Websrc/js/**/*',['alljs']);
});