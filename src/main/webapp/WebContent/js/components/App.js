import React from 'react';
import RecentChangesTable from './RecentChangesTable';
import Headings from './Headings';
import Rows from './Rows';

// App component, Headings/Rows componentを呼び出し
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      changeSets : props.changeSets
    };
  }

  render() {
    console.log('App is rendered');
    return <RecentChangesTable className = 'table'>
    <Headings headings = {this.props.headings} />
    <Rows changeSets = {this.state.changeSets} />
    </RecentChangesTable>;
  }
}

export default App;
