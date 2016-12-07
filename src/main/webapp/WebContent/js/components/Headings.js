import React from 'react';
import Heading from './Heading';

// Headings component、渡された数だけ Heading componentを呼び出し
class Headings extends React.Component {
  constructor(props) {
    super(props);
    this.state = {headings : props.headings};
  }

  render() {
    console.log('headings');
    console.dir(this.state);
    let headings = this.state.headings.map((name, index) => {
      return <Heading heading = {name} key = {index}/>;
    });
    return <thead><tr>{headings}</tr></thead>;
  }
}

export default Headings;
