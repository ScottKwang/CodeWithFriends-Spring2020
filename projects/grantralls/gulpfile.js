const gulp = require("gulp");
const tsProject = require("gulp-typescript").createProject("tsconfig.json");
const clean = require("gulp-clean");
const debug = require("gulp-debug");
const typedoc = require("gulp-typedoc");

function defaultTask(cb) {
	cb();
}

function cleanTask() {
	return gulp.src("dist/*", {read: false})
		.pipe(clean());
}

function docTask() {
	return gulp.src(["src/**/*.ts"])
		.pipe(typedoc({
			module: "commonjs",
			target: "es6",
			out: "docs/",
			name: "Basu"
		}));
}

function compileTS() {
	return gulp.src("src/*.ts")
		.pipe(debug({title: "DEBUG: "}))
		.pipe(tsProject()).js
		.pipe(gulp.dest("dist/"));
}

exports.doc = docTask;
exports.clean = cleanTask;
exports.compile = compileTS;
exports.default = function() {
	gulp.watch(["./src/*.ts"], compileTS);
}
