import React from 'react';
import ReactDOM from 'react-dom';
import RecentChangesTable from './components/RecentChangesTable';
import Headings from './components/Headings';
import Rows from './components/Rows';

// App component, Headings/Rows componentを呼び出し
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      headings : props.headings,
      changeSets : props.changeSets
    };
  }

  render() {
    console.log('exp');
    console.dir(this.props);
    console.dir(this.state);
    return <RecentChangesTable className = 'table'>
    <Headings headings = {this.state.headings} />
    <Rows changeSets = {this.state.changeSets} />
    </RecentChangesTable>;
  }
}

let data = [{
  'when': '2 minutes ago',
  'who': 'Jill Dupre',
  'description': 'Created new account'
},{
  'when': '1 hour go',
  'who': 'Lose White',
  'description': 'Added fist chapter'
}];
let headings = ['When', 'Who', 'Description'];

let props = { headings: headings, changeSets: data };

// headings = {headings}で規定した値が Appのprops.headingsになる
// {...props}の書き方はReact独自ではなくES6で加わったもの
ReactDOM.render(<App {...props} />, document.getElementById('root'));
