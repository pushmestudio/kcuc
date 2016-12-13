import React from 'react';
import Headings from './Headings';
import Rows from './Rows';

class RecentChangesTable extends React.Component {
  constructor(props) {
    // super()を呼び、値を渡すことで、初期化時に渡したプロパティがこのクラスのprops.xxxとして使えるようになる
    super(props);
  }

  render() {
    console.log('RecentChangesTable is rendered');
    console.dir(this.props);
    return <div>
    <table>
    <Headings headings = {this.props.headings} />
    <Rows dataSet = {this.props.dataSet} />
    </table>
    <button onClick = {this.props.onClickFunc}>hello</button>
    </div>;
  }
}

export default RecentChangesTable;
