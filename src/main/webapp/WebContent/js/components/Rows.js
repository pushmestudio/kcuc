import React from 'react';
import Row from './Row';

// Rows component, 渡されたデータの数だけRow componentを呼び出し
class Rows extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('Rows is rendered');
    console.dir(this.props.dataSet);
    let rows = this.props.dataSet.map((data, index) => {
      return(<Row key={index} {...data} type={this.props.type} />);
    });
    return <tbody>{rows}</tbody>;
  }
}

export default Rows;
