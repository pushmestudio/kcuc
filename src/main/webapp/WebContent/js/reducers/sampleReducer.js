const sampleReducer = (state = {
  data: []
  , headings : [' ', 'ID', 'URL', 'Time', 'Flag', 'Note']
  // headings = {headings}で規定した値が Appのprops.headingsになる
  // {...props}の書き方はReact独自ではなくES6で加わったもの, spread syntax
}, action) => {
  switch (action.type) {
  case 'SAMPLE':
    console.log('previous state is :' + state.isClicked);
    return {
      isClicked: !state.isClicked
    };
  case 'START_FETCH_DATA':
    return state;
  case 'SUCCESS_FETCH_DATA':
    return {
      data: action.data.subscribeTarget
      , headings: state.headings
    };
  case 'ERROR_FETCH_DATA':
    return state;
  default:
    return state;
  }
};
export default sampleReducer;
