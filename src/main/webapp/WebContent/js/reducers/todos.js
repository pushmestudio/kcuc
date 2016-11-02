/**
 * @file ReducerのうちのTodo
 * @description Reducerでは、(previousState, action) => newState)といった形で、
 * 以前の状態とアクション内容から次の状態を返す
 * 重要なポイントとしては、reducerはあくまでも状態を返すだけで、
 * 処理内容は記述しないということ
 * stateは初期値ではundefinedになるため、このタイミングが初期パラメータを返すのに適している
 * reducerは肥大化しないように、更新されるタイミングが別のものは別のファイルに分割するのが良い
 * @see http://redux.js.org/docs/basics/Reducers.html#handling
 */

const todo = (state = {}, action) => {
  switch (action.type) {
  case 'ADD_TODO':
    return {
      id: action.id,
      text: action.text,
      completed: false
    };
  case 'TOGGLE_TODO':
    if (state.id !== action.id) {
      return state;
    }

    return Object.assign({}, state, {
      completed: !state.completed
    });

  default:
    return state;
  }
};

const todos = (state = [], action) => {
  switch (action.type) {
  case 'ADD_TODO':
    return [
      ...state,
      todo(undefined, action)
    ];
  case 'TOGGLE_TODO':
    return state.map(t =>
      todo(t, action)
    );
  default:
    return state;
  }
};

export default todos;
