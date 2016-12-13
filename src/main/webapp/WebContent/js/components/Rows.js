import React from 'react';
import Row from './Row';

// Rows component, 渡されたデータの数だけRow componentを呼び出し
class Rows extends React.Component {
  constructor(props) {
    super(props);
    this.state = {changeSets : props.changeSets};
  }

  render() {
    console.log('Rows is rendered');
    let rows = this.state.changeSets.map((changeSet, index) => {
      return(<Row selected = {false} changeSet = {changeSet} key = {index} name = {this.props.name}/>);
    });
    return <tbody>{rows}</tbody>;
  }
}

export default Rows;
