import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';
import SendRequest from '../utils/SendRequest';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected : props.selected
      , prodId : props.prodId
      , url : props.url
      , updateTime : props.updateTime
      , updateFlag : props.updateFlag
      , note: ''
    };
  }

  // ここのonChange内をdispatchにすればいいのか？
  render() {
    console.log('Row is rendered');
    console.dir(this.props);
    return <tr style={{border:'normal'}}>
    <td><input type="checkbox" value={this.state.selected} onChange={this.handleTick.bind(this)}/></td>
    <td>{this.state.prodId}</td>
    <td>{this.state.url}</td>
    <td>{this.state.updateTime}</td>
    <td>{this.state.updateFlag}</td>
    <td><input type="text" value={this.state.note} onChange={this.handleChange.bind(this)} ref="textBox"/></td>
    </tr>;
  }

  // 値の変更を検知して更新
  handleChange(event) {
    console.log('value is updated');
    this.setState({note: event.target.value});
  }

  // チェックが外されたらモーダルを表示
  handleTick(event) {
    this.setState({selected : event.target.checked});
  }
}

export default Row;
