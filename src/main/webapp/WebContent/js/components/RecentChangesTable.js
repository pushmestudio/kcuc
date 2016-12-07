import React from 'react';

class RecentChangesTable extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('table');
    console.dir(this.props);
    return <table>
             {this.props.children}
           </table>;
  }
}

export default RecentChangesTable;
