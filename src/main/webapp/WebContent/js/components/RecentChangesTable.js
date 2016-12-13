import React from 'react';
import Headings from './Headings';
import Rows from './Rows';

class RecentChangesTable extends React.Component {
  constructor(props) {
    // super()を呼び、値を渡すことで、初期化時に渡したプロパティがこのクラスのprops.xxxとして使えるようになる
    super(props);

    this.state = {
      headings : this.props.headings
      , changeSets : this.props.changeSets
      , name : this.props.name
    };
  }

  render() {
    console.log('RecentChangesTable is rendered');
    console.dir(this.props);
    console.dir(this.state);
    return <div>
          <table>
            <Headings headings = {this.state.headings} />
            <Rows changeSets = {this.state.changeSets} name = {this.props.name} />
           </table>
           <button onClick = {this.props.onClickFunc}>hello</button>
           </div>;
  }
}

export default RecentChangesTable;
