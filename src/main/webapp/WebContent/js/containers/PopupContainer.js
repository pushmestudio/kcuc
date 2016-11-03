import { connect } from 'react-redux';
import { showPopup } from '../actions';
import PopupComponent from '../components/PopupComponent';

let callPopup = () => {
  return alert('hello');
};

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    isClicked: state.popupReducer.isClicked
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    onClickFunc: (text) => {
      dispatch(showPopup(text));
    }
  };
};

const PopupContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(PopupComponent);

export default PopupContainer;
