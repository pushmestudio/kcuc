import React from 'react';

// Heading component
class Heading extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('Heading is rendered');
    return <th ><mark>{this.props.heading}</mark></th>;
  }
}

export default Heading;
