const path = require('path');
module.exports = {
	mode: 'development',
	entry: './client.js',
	output: {
		path: path.join(__dirname, '/public', 'javascripts'),
		filename: 'bundle.js',
		publicPath: '/javascripts'
	},
	module: {
		rules: [
			{
				test: /\.jsx?$/,
				exclude: [/node_modules/, /"app.js/],
				loader: "babel-loader",
				options: {
					presets: ["react"]
				}
			}
		]
	}
}