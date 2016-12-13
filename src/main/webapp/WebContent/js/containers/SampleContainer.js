import React from 'react';
import { connect } from 'react-redux';
import { showSample, fetchData } from '../actions';
import RecentChangesTable from '../components/RecentChangesTable';

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    name: state.sampleReducer.name,
    headings: state.sampleReducer.headings,
    changeSets: state.sampleReducer.data
  };
};

const mapDispatchToProps = (dispatch) => {
  console.log('dispatch to props:');
  return {
    onClickFunc: (text) => {
      dispatch(fetchData());
    }
  };
};

const SampleContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(RecentChangesTable);

export default SampleContainer;
