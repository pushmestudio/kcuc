import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    console.log('Row is rendered');
    return <tr>
    <td><input type="checkbox" ref="checkBox" onChange={(e) => this.handleTick(e)}/></td>
    <td>{this.props.prodId}</td>
    <td>{this.props.url}</td>
    <td>{this.props.updateTime}</td>
    <td>{this.props.updateFlag}</td>
    <td><input type="text" ref="textBox" onChange={(e) => this.handleChange(e)}/></td>
    </tr>;
  }

  // 値の変更を検知して更新
  handleChange(event) {
    console.log('value is updated');
  }

  // チェックON/OFF
  handleTick(event) {
    if (this.refs.checkBox.selected) {
      console.log(this.props.prodId + ' is removed');
      ReactDOM.render(<ModalAlert />, document.getElementById('modalAlert'));
    } else {
      console.log(this.props.prodId + ' is selected');
      this.props.func();
    }
  }
}

export default Row;
