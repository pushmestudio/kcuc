import { connect } from 'react-redux';
import { fetchPages, registerPage, cancelPage } from '../actions';
import PageTable from '../components/PageTable';

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
    , register: (user, page) => {
      dispatch(registerPage(user, page));
    }
    , cancel: (user, page) => {
      dispatch(cancelPage(user, page));
    }
  };
};

const PageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(PageTable);

export default PageContainer;
