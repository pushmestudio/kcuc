import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';
import SendRequest from '../utils/SendRequest';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      changeSet : props.changeSet
      , selected : props.selected
      , value : props.data
      , name : props.name
    };
  }

  // ここのonChange内をdispatchにすればいいのか？
  render() {
    console.log('Row is rendered');
    console.dir(this.props);
    return <tr>
    <td><input type="checkbox" value={this.state.changeSet.who} onChange={this.handleTick.bind(this)}/></td>
    <td>{this.state.changeSet.when}</td>
    <td>{this.state.changeSet.who}</td>
    <td><input type="text" value={this.state.changeSet.description} onChange={this.handleChange.bind(this)} ref="textBox"/></td>
    <td><input type="button" value={this.props.name} onClick={this.updateData.bind(this)}/></td>
    </tr>;
  }

  // 値の変更を検知して更新
  handleChange(event) {
    console.log('value is updated');
    this.setState({changeSet: {
      when: this.state.changeSet.when
      , who: this.state.changeSet.who
      , description: event.target.value
    }});
  }

  // チェックが外されたらモーダルを表示
  handleTick(event) {
    if (this.state.selected) {
      console.log(event.target.value + 'is removed');
      ReactDOM.render(<ModalAlert />, document.getElementById('modalAlert'));
    } else {
      console.log(event.target.value + 'is selected');
    }
    this.setState({selected : event.target.checked});
  }

  // cloudantDBにリクエストを送り、得られた結果を反映
  updateData() {
    console.dir(ReactDOM.findDOMNode(this.refs.textBox));
    const cloudantDb = 'https://71fe3412-713b-4330-98c7-688705e6fab5-bluemix.cloudant.com/kcucdb/fbc44ada6a5430107ddda34ef67f4673';
    SendRequest.sendGet(cloudantDb).then((data) => {
      this.setState({value : data.userName});
    });
  }
}

export default Row;
