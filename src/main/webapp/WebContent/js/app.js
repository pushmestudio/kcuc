/**
 * @file お試しファイル
 */

/**
 * @module app
 * @requires react
 * @requires react-dom
 * @requires components/main
 */
import React from 'react';
import ReactDOM from 'react-dom';
import Main from './components/main.jsx';

for (let i = 1; i < 10; i++) {
  debugger;
  console.log('hello');
}

ReactDOM.render(
  <Main message='and GoodBye World' date={new Date()} />,
  document.getElementById('app')
);
