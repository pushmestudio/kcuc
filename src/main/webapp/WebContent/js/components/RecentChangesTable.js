import React from 'react';
import Headings from './Headings';
import Rows from './Rows';

class RecentChangesTable extends React.Component {
  constructor(props) {
    // super()を呼び、値を渡すことで、初期化時に渡したプロパティがこのクラスのprops.xxxとして使えるようになる
    super(props);
    this.state = {
      user: ''
    };
  }

  render() {
    console.log('RecentChangesTable is rendered');
    console.dir(this.props);
    return <div>
    <table className='table-bordered'>
    <Headings headings={this.props.headings} />
    <Rows dataSet={this.props.dataSet} />
    </table>
    <input type="text" value={this.state.user} onChange={(e) => this.updateText(e)}/>
    <button onClick={() => this.props.onClickFunc(this.state.user)}>Search by User</button>
    </div>;
  }

  updateText(event) {
    this.setState({user: event.target.value});
  }
}

export default RecentChangesTable;
