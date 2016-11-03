/**
 * @file ReduxにおけるReducerの定義
 * @description ReducerはActionから発生時にアプリの状態をどのように変更するかを決める役割
 * Reduxでは、全ての状態は1つのオブジェクトに格納される
 * @see todos
 * @see http://redux.js.org/docs/basics/Reducers.html
 */

import { combineReducers } from 'redux';
import todos from './todos';
import visibilityFilter from './visibilityFilter';
import popupReducer from './popupReducer';

const todoApp = combineReducers({
  todos,
  visibilityFilter,
  popupReducer
});

export default todoApp;
