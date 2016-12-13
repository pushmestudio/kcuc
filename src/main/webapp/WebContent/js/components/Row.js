import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected : false
      , prodId : props.prodId
      , url : props.url
      , updateTime : props.updateTime
      , updateFlag : props.updateFlag
      , note: ''
    };
  }

  render() {
    console.log('Row is rendered');
    return <tr style={{border:'normal'}}>
    <td><input type="checkbox" value={this.state.selected} onChange={(e) => this.handleTick(e)}/></td>
    <td>{this.state.prodId}</td>
    <td>{this.state.url}</td>
    <td>{this.state.updateTime}</td>
    <td>{this.state.updateFlag}</td>
    <td><input type="text" value={this.state.note} onChange={(e) => this.handleChange(e)} ref="textBox"/></td>
    </tr>;
  }

  // 値の変更を検知して更新
  handleChange(event) {
    console.log('value is updated');
    this.setState({note: event.target.value});
  }

  // チェックON/OFF
  handleTick(event) {
    if (this.state.selected) {
      console.log(this.state.prodId + ' is removed');
      ReactDOM.render(<ModalAlert />, document.getElementById('modalAlert'));
    } else {
      console.log(this.state.prodId + ' is selected');
    }
    this.setState({selected : event.target.checked});
  }
}

export default Row;
