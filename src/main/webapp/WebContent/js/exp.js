/**
 * @file エントリーポイント, Appを呼び出す
 */

import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';

// bootstrapはes6 importには対応していないのでrequireする
require('bootstrap');
require('bootstrap/dist/css/bootstrap.css');

// 下記はダミーデータ
let data = [{
  'when': '2 minutes ago',
  'who': 'Jill Dupre',
  'description': 'Created new account'
},{
  'when': '1 hour go',
  'who': 'Lose White',
  'description': 'Added fist chapter'
}];
let headings = ['', 'When', 'Who', 'Description'];

let props = { headings: headings, changeSets: data };

// headings = {headings}で規定した値が Appのprops.headingsになる
// {...props}の書き方はReact独自ではなくES6で加わったもの
ReactDOM.render(<App {...props} />, document.getElementById('root'));
