import React from 'react';
import { connect } from 'react-redux';
import { showSample, fetchData } from '../actions';
import RecentChangesTable from '../components/RecentChangesTable';

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    headings: state.sampleReducer.headings,
    dataSet: state.sampleReducer.data
  };
};

const mapDispatchToProps = (dispatch) => {
  console.log('dispatch to props:');
  return {
    onClickFunc: (user) => {
      dispatch(fetchData(user));
    }
  };
};

const SampleContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(RecentChangesTable);

export default SampleContainer;
