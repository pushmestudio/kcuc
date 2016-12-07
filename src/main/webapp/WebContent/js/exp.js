import React from 'react';
import ReactDOM from 'react-dom';
import RecentChangesTable from './components/RecentChangesTable';

// Heading component
RecentChangesTable.Heading = React.createClass({
  render: function() {
    return <th>{this.props.heading}</th>;
  }
});

// Headings component、渡された数だけ Heading componentを呼び出し
RecentChangesTable.Headings = React.createClass({
  render: function() {
    let headings = this.props.headings.map(function(name) {
      return <RecentChangesTable.Heading heading = {name} key = {name}/>;
    });
    return <thead><tr>{headings}</tr></thead>;
  }
});

// Row component
RecentChangesTable.Row = React.createClass({
  render: function() {
    return <tr>
    <td>{this.props.changeSet.when}</td>
    <td>{this.props.changeSet.who}</td>
    <td>{this.props.changeSet.description}</td>
    </tr>;
  }
});

// Rows component, 渡されたデータの数だけRow componentを呼び出し
RecentChangesTable.Rows = React.createClass({
  render: function() {
    let rows = this.props.changeSets.map(function(changeSet) {
      return(<RecentChangesTable.Row changeSet = {changeSet} key = {changeSet.id}/>);
    });
    return <tbody>{rows}</tbody>;
  }
});

// App component, Headings/Rows componentを呼び出し
let App = React.createClass({
  render: function() {
    return <RecentChangesTable className = 'table'>
    <RecentChangesTable.Headings headings = {this.props.headings} />
    <RecentChangesTable.Rows changeSets = {this.props.changeSets} />
    </RecentChangesTable>;
  }
});

let data = [{
  'id': 1,
  'when': '2 minutes ago',
  'who': 'Jill Dupre',
  'description': 'Created new account'
},{
  'id': 2,
  'when': '1 hour go',
  'who': 'Lose White',
  'description': 'Added fist chapter'
}];
let headings = ['When', 'Who', 'Description'];

// headings = {headings}で規定した値が Appのprops.headingsになる
ReactDOM.render(<App headings = {headings}
  changeSets = {data} />,
  document.getElementById('root'));
