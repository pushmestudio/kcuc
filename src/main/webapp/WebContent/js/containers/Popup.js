import React from 'react';
import { connect } from 'react-redux';
import { showPopup } from '../actions';

let callPopup = () => {
  return alert('hello');
};

let Popup = ({ dispatch }) => {
  return (
      <div>
        <p onClick={callPopup} >
          hello world
        </p>
        <p onClick={e => {
          e.preventDefault();
          dispatch(showPopup('dispatch!'));
        }}>
          This is new way to go!
        </p>
      </div>
  );
};
Popup = connect()(Popup);

export default Popup;
