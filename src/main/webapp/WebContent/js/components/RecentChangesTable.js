import React from 'react';

let RecentChangesTable = React.createClass({
  render: function() {
    return <table>
             {this.props.children}
           </table>;
  }
});

export default RecentChangesTable;
