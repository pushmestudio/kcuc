import React from 'react';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      changeSet : props.changeSet
      , selected : props.selected
    };
  }

  render() {
    console.log('Row is rendered');
    return <tr>
    <td><input type="checkbox" value={this.state.changeSet.who} onChange={this.handleTick.bind(this)}/></td>
    <td>{this.state.changeSet.when}</td>
    <td>{this.state.changeSet.who}</td>
    <td><input type="text" value={this.state.changeSet.description} onChange={this.handleChange.bind(this)}/></td>
    </tr>;
  }

  handleChange(event) {
    console.log('value is updated');
    this.setState({changeSet: {
      when: this.state.changeSet.when
      , who: this.state.changeSet.who
      , description: event.target.value
    }});
  }

  handleTick(event) {
    if (this.state.selected) {
      console.log(event.target.value + 'is removed');
    } else {
      console.log(event.target.value + 'is selected');
    }
    this.setState({selected : event.target.checked});
  }
}

export default Row;
