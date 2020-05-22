const HtmlWebPackPlugin = require("html-webpack-plugin");
const path = require('path');
const htmlPlugin = new HtmlWebPackPlugin({
  template: "./src/client/index.html", 
  filename: "./index.html"
});
module.exports = {
  entry: "./src/client/index.js",
  output: {
    path: path.join(__dirname, 'dist'),
    filename: "bundle.js"
  },
  plugins: [htmlPlugin],
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader"
        }
      },
      {
        test: /\.(png|svg|jpg|gif)$/,
        loader: "file-loader",
        options: { name: './src/client/public/[name].[ext]' }
      },
      {
        test: /\.css$/,
        use: [
          "style-loader",
          "css-loader"
        ],
        // options: { name: './src/client/public/stylesheets/[name].[ext]' }
      }
    ]
  }
};