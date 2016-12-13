import React from 'react';
import Heading from './Heading';

// Headings component、渡された数だけ Heading componentを呼び出し
class Headings extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('Headings is rendered');
    let headings = this.props.headings.map((name, index) => {
      return <Heading heading={name} key={index}/>;
    });
    return <thead><tr>{headings}</tr></thead>;
  }
}

export default Headings;
