import React from 'react';
import RecentChangesTable from './RecentChangesTable';
import PageContainer from '../containers/PageContainer';
import UserContainer from '../containers/UserContainer';

// App component, Headings/Rows componentを呼び出し
class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('App is rendered');
    return <div className='container'>
      <h2 className="title">Knowledge Center Update Checker</h2>
      <PageContainer></PageContainer>
      <UserContainer></UserContainer>
    </div>;
  }
}

export default App;
