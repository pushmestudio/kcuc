const kcUser = (state = {
  users: []
  , headings: [' ', 'ID', 'Flag']
  , title: 'User List'
  , type: 'users'
}, action) => {
  switch (action.type) {
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
export default kcUser;
