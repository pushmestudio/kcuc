import React from 'react';
import { connect } from 'react-redux';
import { fetchUsers } from '../actions';
import RecentChangesTable from '../components/RecentChangesTable';

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    headings: state.kcUser.headings
    , results: state.kcUser.users
    , title: state.kcUser.title
    , type: state.kcUser.type
  };
};

const mapDispatchToProps = (dispatch) => {
  console.log('dispatch to props:');
  return {
    fetch: (page) => {
      dispatch(fetchUsers(page));
    }
  };
};

const UserContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(RecentChangesTable);

export default UserContainer;
