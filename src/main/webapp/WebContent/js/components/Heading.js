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
    let self = this; // アロー関数内でthisを参照する場合はselfに代入して使う
    let headingDisplay = (() => {
      if (self.props.heading.length !== 0) {
        return self.state.clicked.toString();
      } else {
        return '';
      }
    })();
    return <th onClick={this.toggleState.bind(this)}>{this.props.heading} <mark>{headingDisplay}</mark></th>;
  }

  toggleState() {
    this.setState({clicked: !this.state.clicked});
  }
}

export default Heading;
