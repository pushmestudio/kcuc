/**
 * @file ReduxにおけるActionの定義
 * @description ActionはApplicationからStoreへ送る際のpayloadの役割
 * returnの中にある{}がActionで、このファイルで定義されているのはAction Creater(Actionを返す関数の定義)
 * Storeへと送る(dispatchする)際には、ActionではなくてAction Createrを渡す
 * @see http://redux.js.org/docs/basics/Actions.html
 */

let nextTodoId = 0
export const addTodo = (text) => {
  return {
    type: 'ADD_TODO',
    id: nextTodoId++,
    text
  }
}

export const setVisibilityFilter = (filter) => {
  return {
    type: 'SET_VISIBILITY_FILTER',
    filter
  }
}

export const toggleTodo = (id) => {
  return {
    type: 'TOGGLE_TODO',
    id
  }
}
