const sampleReducer = (state = {
  data: [{
    'when': '2 minutes ago',
    'who': 'Jill Dupre',
    'description': 'Created new account'
  },{
    'when': '1 hour go',
    'who': 'Lose White',
    'description': 'Added fist chapter'
  }]
  , headings : ['', 'When', 'Who', 'Description']
  , name : ''
  // headings = {headings}で規定した値が Appのprops.headingsになる
  // {...props}の書き方はReact独自ではなくES6で加わったもの
}, action) => {
  console.log('reducer');
  console.dir(state);
  switch (action.type) {
  case 'SAMPLE':
    console.log('previous state is :' + state.isClicked);
    return {
      isClicked: !state.isClicked
    };
  case 'START_FETCH_DATA':
    return state;
  case 'SUCCESS_FETCH_DATA':
    return {...state, name: action.data.userName};
  case 'ERROR_FETCH_DATA':
    return state;
  default:
    return state;
  }
};
export default sampleReducer;
