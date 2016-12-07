/* eslint-disable no-undef */
path = require('path');
webpack = require('webpack');

module.exports = {
  entry: {
    bundle: './WebContent/js/exp.js'
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
      },

      // 下記はBootstrapのcssを読み込む関係で必要
      { test: /\.svg$/, loader: 'url-loader?mimetype=image/svg+xml' },
      { test: /\.woff$/, loader: 'url-loader?mimetype=application/font-woff' },
      { test: /\.woff2$/, loader: 'url-loader?mimetype=application/font-woff' },
      { test: /\.eot$/, loader: 'url-loader?mimetype=application/font-woff' },
      { test: /\.ttf$/, loader: 'url-loader?mimetype=application/font-woff' }
    ]
  },
  // Bootstrap使用のために必要
  plugins: [
    new webpack.ProvidePlugin({
      jQuery: 'jquery',
      $: 'jquery'
    })
  ]
};
