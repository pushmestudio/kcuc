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
    return <div className="container">
        <h2 className="title">Knowledge Center Update Checker</h2>
        <h3>Search by User</h3>
      <div className="input-group">
        <input className="form-control" type="text" ref="userId" placeholder="type here used id... (e.g. capsmalt)"/>
        <span className="input-group-btn">
          <button className="btn btn-primary" onClick={() => this.props.onClickFunc(this.refs.userId.value)}>Search</button>
        </span>
      </div>
      <h3>Search Result Pages</h3>
      <table className='table table-bordered'>
        <Headings headings={this.props.headings} />
        <Rows dataSet={this.props.pageSet} />
      </table>
      <hr/>
      <h3>Search by Page</h3>
      <div className="input-group">
        <input className="form-control" type="text" ref="pageHref" placeholder="type here used id... (e.g. capsmalt)"/>
        <span className="input-group-btn">
          <button className="btn btn-primary" onClick={() => this.props.onClickFunc2(this.refs.pageHref.value)}>Search</button>
        </span>
      </div>
      <h3>Search Result Users</h3>
      <table className='table table-bordered'>
        <Headings headings={this.props.headings} />
        <Rows dataSet={this.props.userSet} />
      </table>
    </div>;
  }
}

export default RecentChangesTable;
