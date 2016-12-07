import React from 'react';

class RecentChangesTable extends React.Component {
  constructor(props) {
    // super()を呼び、値を渡すことで、初期化時に渡したプロパティがこのクラスのprops.xxxとして使えるようになる
    super(props);
  }

  render() {
    console.log('RecentChangesTable is rendered');
    return <table>
             {this.props.children}
           </table>;
  }
}

export default RecentChangesTable;
