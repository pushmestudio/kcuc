import React from 'react';

// Heading component
class Heading extends React.Component {
  constructor(props) {
    super(props);
    this.state = {clicked : false};
  }

  render() {
    console.log('Heading is rendered');
    console.log('Heading: ' + this.props.heading + ', state.clicked: '+ this.state.clicked);
    return <th onClick={this.toggleState.bind(this)}>{this.props.heading} {this.state.clicked.toString()}</th>;
  }

  toggleState() {
    this.setState({clicked: !this.state.clicked});
  }
}

export default Heading;
