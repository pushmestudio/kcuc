import React from 'react';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {changeSet : props.changeSet};
  }

  render() {
    console.log('row');
    console.dir(this.props);
    return <tr>
    <td>{this.state.changeSet.when}</td>
    <td>{this.state.changeSet.who}</td>
    <td>{this.state.changeSet.description}</td>
    </tr>;
  }
}

export default Row;
