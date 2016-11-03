const popupReducer = (state = {isClicked : false}, action) => {
  switch (action.type) {
  case 'SHOW_POPUP':
    alert('on reducer ' + action.text + ' : ' + action.count);
    console.log('previous state is :' + state.isClicked);

    return {
      isClicked: !state.isClicked
    };

  default:
    return state;
  }
};

export default popupReducer;
