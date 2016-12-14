import React from 'react';
import { connect } from 'react-redux';
import { fetchPages, fetchUsers } from '../actions';
import RecentChangesTable from '../components/RecentChangesTable';

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    headings: state.sampleReducer.headings
    , pageSet: state.sampleReducer.pages
    , userSet: state.sampleReducer.users
  };
};

const mapDispatchToProps = (dispatch) => {
  console.log('dispatch to props:');
  return {
    onClickFunc: (user) => {
      dispatch(fetchPages(user));
    }
    ,onClickFunc2: (page) => {
      dispatch(fetchUsers(page));
    }
  };
};

const SampleContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(RecentChangesTable);

export default SampleContainer;
