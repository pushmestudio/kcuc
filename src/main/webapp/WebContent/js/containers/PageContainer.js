import React from 'react';
import { connect } from 'react-redux';
import { fetchPages } from '../actions';
import RecentChangesTable from '../components/RecentChangesTable';

const mapStateToProps = (state) => {
  console.log('container state:');
  console.dir(state);
  return {
    headings: state.kcPage.headings
    , results: state.kcPage.pages
    , title: state.kcPage.title
    , type: state.kcPage.type
  };
};

const mapDispatchToProps = (dispatch) => {
  console.log('dispatch to props:');
  return {
    fetch: (user) => {
      dispatch(fetchPages(user));
    }
  };
};

const PageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(RecentChangesTable);

export default PageContainer;
