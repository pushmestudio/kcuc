import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
  }

  getEachRow(rowType) {
    if (rowType === 'pages') {
      return <tr>
      <td><input type='checkbox' ref='checkBox' onChange={(e) => this.handleTick(e)}/></td>
      <td>{this.props.pageHref}</td>
      <td>{String(this.props.isUpdated)}</td>
      <td><input type='text' ref='textBox' onChange={(e) => this.handleChange(e)}/></td>
      </tr>;
    } else if (rowType === 'users') {
      return <tr>
      <td><input type='checkbox' ref='checkBox' onChange={(e) => this.handleTick(e)}/></td>
      <td>{this.props.id}</td>
      <td>{String(this.props.isUpdated)}</td>
      </tr>;
    }
  }

  render() {
    console.log('Row is rendered');
    console.dir(this.props);
    return this.getEachRow(this.props.type);
  }

  // 値の変更を検知して更新
  handleChange() {
    console.log('value is updated');
  }

  // チェックON/OFF
  handleTick() {
    if (this.refs.checkBox.selected) {
      ReactDOM.render(<ModalAlert />, document.getElementById('modalAlert'));
    }
  }
}

export default Row;
