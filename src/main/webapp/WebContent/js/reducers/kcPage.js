const kcPage = (state = {
  pages: []
  , headings: [' ', 'URL', 'Flag', 'Note']
  , title: 'Page List'
  , type: 'pages'
}, action) => {
  switch (action.type) {
  case 'START_FETCH_PAGES':
    return state;
  case 'SUCCESS_FETCH_PAGES':
    return Object.assign({}, state, {pages: action.data.pages});
  case 'ERROR_FETCH_PAGES':
    return state;
  default:
    return state;
  }
};
export default kcPage;
