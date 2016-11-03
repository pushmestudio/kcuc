import React, { PropTypes } from 'react';

const PopupComponent = ({ onClickFunc, isClicked}) => {
  return (
    <p onClick={() => {
      onClickFunc('isClicked is ' + isClicked);
    }}>PopupComponent status update
  </p>
  );
};

PopupComponent.propTypes = {
  onClickFunc: PropTypes.func.isRequired,
  isClicked: PropTypes.bool
};

export default PopupComponent;
