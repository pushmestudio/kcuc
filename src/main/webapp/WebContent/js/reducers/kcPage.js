const kcPage = (state = {
  pages: []
  , headings: [' ', 'URL', 'Flag', 'Note']
  , title: 'Page List'
  , type: 'pages'
}, action) => {
  switch (action.type) {
  case 'START_FETCH_PAGES':
  case 'START_REG_PAGE':
  case 'START_CANCEL_PAGE':
    return state;
  case 'SUCCESS_FETCH_PAGES':
  case 'SUCCESS_REG_PAGE':
  case 'SUCCESS_CANCEL_PAGE':
    return Object.assign({}, state, {pages: action.data.pages});
  case 'ERROR_FETCH_PAGES':
    alert('An error has occured in fetching pages. Please try it again later.'); // TODO より適切なアナウンス方法に変更
    return state;
  case 'ERROR_REG_PAGE':
    alert('An error has occured in registering a page. Please try it again later.'); // TODO より適切なアナウンス方法に変更
    return state;
  case 'ERROR_CANCEL_PAGE':
    alert('An error has occured in canceling a page. Please try it again later.'); // TODO より適切なアナウンス方法に変更
    return state;
  default:
    return state;
  }
};
export default kcPage;
