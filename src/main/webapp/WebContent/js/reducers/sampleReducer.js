const sampleReducer = (state = {
  data: []
  , headings : [' ', 'ID', 'URL', 'Time', 'Flag', 'Note']
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
    return Object.assign({}, state, {data: action.data.subscribeTarget});
  case 'ERROR_FETCH_DATA':
    return state;
  default:
    return state;
  }
};
export default sampleReducer;
