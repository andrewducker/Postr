var gulp = require('gulp');
var ngAnnotate = require('gulp-ng-annotate');
concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var templateCache = require('gulp-angular-templatecache');
var rev = require('gulp-rev');
var rimraf = require('gulp-rimraf');
var crypto = require('crypto');
var fs = require('fs');

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

gulp.task('templates',['clean'],function(){
	return gulp.src(['Websrc/templates/**/*.html'])
	.pipe(templateCache("templates.js",{module:"postrApp"}))
	.pipe(gulp.dest('build'));
});

gulp.task('alljs',['controllers','miscjs', 'templates'],function(){
	return gulp.src(['build/*.js'])
		.pipe(concat('alljs.js'))
//           .pipe(uglify())
//		.pipe(rev())
		.pipe(gulp.dest('war'));
});

gulp.task('cachebust',['alljs'],function(){
	var oldFileName = 'war/alljs.js';
	var resultHash = hashFile(oldFileName);
	var newFileName = 'war/alljs-'+resultHash+'.js';
	fs.renameSync(oldFileName,newFileName);

	var indexhtml = fs.readFileSync('Websrc/index.html','utf8');
	
	var result = indexhtml.replace('alljs.js', newFileName);
	
	fs.writeFileSync('war/index.html',result);
});

gulp.task('default',function(){
	gulp.watch('Websrc/**/*',['cachebust']);
});