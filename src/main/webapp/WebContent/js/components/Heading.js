import React from 'react';

// Heading component
class Heading extends React.Component {
  constructor(props) {
    super(props);
    this.state = {heading : props.heading};
  }

  render() {
    console.log('heading');
    console.dir(this.state);
    return <th>{this.state.heading}</th>;
  }
}

export default Heading;
