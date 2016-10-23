path = require('path');
webpack = require('webpack');

module.exports = {
  entry: {
    sample1: './WebContent/js/app.js',
    sample2: './WebContent/js/sample2.js',
    todos: './WebContent/js/index.js'
  },
  output: {
    path: path.resolve('WebContent/build/'),
    filename: '[name].js'
  },
  module: {
    loaders: [
      {
        test: /.jsx?$/,
        loader: 'babel',
        exclude: /node_modules/
      },
      {
        test: /.css$/,
        loader: 'style-loader!css-loader'
      }
    ]
  }
}
