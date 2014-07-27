var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
concat = require('gulp-concat');

gulp.task('controllers', function() {
      return gulp.src(['Websrc/Controllers/init.js','Websrc/Controllers/*.js'])
          .pipe(ngAnnotate())
          .pipe(concat('controllers.js'))
        .pipe(gulp.dest('war'));
});

gulp.task('default',function(){
	return gulp.watch('Websrc/Controllers/*',['controllers']);
});