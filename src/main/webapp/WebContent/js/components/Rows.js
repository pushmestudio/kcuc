import React from 'react';
import Row from './Row';

// Rows component, 渡されたデータの数だけRow componentを呼び出し
class Rows extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('Rows is rendered');
    console.log(this.props.dataSet);
    let rows = this.props.dataSet.map((data, index) => {
      return(<Row selected={false} key={index} prodId={data.prodId} url={data.url} updateTime={data.updateTime} updateFlag={data.updateFlag}/>);
    });
    console.dir(rows);
    return <tbody>{rows}</tbody>;
  }
}

export default Rows;
