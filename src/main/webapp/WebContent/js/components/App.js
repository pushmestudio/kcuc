import React from 'react';
import RecentChangesTable from './RecentChangesTable';
import SampleContainer from '../containers/SampleContainer';

// App component, Headings/Rows componentを呼び出し
class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('App is rendered');
    return <SampleContainer>
      </SampleContainer>;
  }
}

export default App;
