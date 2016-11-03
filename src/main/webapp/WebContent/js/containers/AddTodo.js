/**
 * @file ReduxにおけるStoreの定義
 * @description Storeはpayloadとしてのactionと,
 * actionとstateから次のstateを導くreducerとを一つにした上で
 * 現在のstateの保持や更新, listenerの追加などをする役割
 *
 * @see http://redux.js.org/docs/basics/Store.html
 */

import React from 'react';
import { connect } from 'react-redux';
import { addTodo } from '../actions';

let AddTodo = ({ dispatch }) => {
  let input;

  return (
    <div>
      <form onSubmit={e => {
        e.preventDefault();
        if (!input.value.trim()) {
          return;
        }
        dispatch(addTodo(input.value));
        input.value = '';
      }}>
        <input ref={node => {
          input = node;
        }} />
        <button type="submit">
          Add Todo
        </button>
      </form>
    </div>
  );
};
AddTodo = connect()(AddTodo);

export default AddTodo;
