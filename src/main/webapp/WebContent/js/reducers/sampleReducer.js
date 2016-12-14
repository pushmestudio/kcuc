const sampleReducer = (state = {
  pages: []
  , users: []
  , headings : [' ', 'ID', 'URL', 'Time', 'Flag', 'Note']
}, action) => {
  switch (action.type) {
  case 'START_FETCH_PAGES':
    return state;
  case 'SUCCESS_FETCH_PAGES':
    return Object.assign({}, state, {pages: action.data.pages});
  case 'ERROR_FETCH_PAGES':
    return state;
  case 'START_FETCH_USERS':
    return state;
  case 'SUCCESS_FETCH_USERS':
    return Object.assign({}, state, {users: action.data.userList});
  case 'ERROR_FETCH_USERS':
    return state;
  default:
    return state;
  }
};
export default sampleReducer;
