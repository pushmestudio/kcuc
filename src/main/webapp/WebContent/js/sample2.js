/**
 * @file お試しファイルその2
 */

/**
 * @module sample2
 * @requires react
 * @requires react-dom
 * @requires components/input
 */
import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/input.jsx';

ReactDOM.render(
  <App />,
  document.getElementById('app')
);
