path = require('path');
webpack = require('webpack');

module.exports = {
  entry: {
    todos: './WebContent/js/index.js'
    // モジュール追加時は下記のようにモジュール名とエントリーポイントのパスを記載
    // sample2: './WebContent/js/sample2.js',
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
