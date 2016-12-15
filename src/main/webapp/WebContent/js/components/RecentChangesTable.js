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
      <h3>{this.props.title}</h3>
      <div className='input-group'>
        <input className='form-control' type='text' ref='searchKey' placeholder='capsmalt, SSMTU9/welcometoibmverse.html'/>
        <span className='input-group-btn'>
          <button className='btn btn-primary' onClick={() => this.props.fetch(this.refs.searchKey.value)}>Search</button>
        </span>
      </div>
      <h3>Search Result</h3>
      <table className='table table-bordered'>
        <Headings headings={this.props.headings} />
        <Rows dataSet={this.props.results} type={this.props.type} />
      </table>
    </div>;
  }
}

export default RecentChangesTable;
