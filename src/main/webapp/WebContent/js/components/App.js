import React from 'react';
import RecentChangesTable from './RecentChangesTable';
import SampleContainer from '../containers/SampleContainer';

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
    return <SampleContainer data = {this.props.data}  headings = {this.props.headings} changeSets = {this.state.changeSets} >
      </SampleContainer>;
  }
}

export default App;
