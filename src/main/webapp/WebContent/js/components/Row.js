import React from 'react';
import ReactDOM from 'react-dom';
import ModalAlert from './ModalAlert';

// Row component
class Row extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      changeSet : props.changeSet
      , selected : props.selected
      , value : 'init'
    };
  }

  render() {
    console.log('Row is rendered');
    return <tr>
    <td><input type="checkbox" value={this.state.changeSet.who} onChange={this.handleTick.bind(this)}/></td>
    <td>{this.state.changeSet.when}</td>
    <td>{this.state.changeSet.who}</td>
    <td><input type="text" value={this.state.changeSet.description} onChange={this.handleChange.bind(this)}/></td>
    <td><input type="button" value={this.state.value} onClick={this.updateData.bind(this)}/></td>
    </tr>;
  }

  handleChange(event) {
    console.log('value is updated');
    this.setState({changeSet: {
      when: this.state.changeSet.when
      , who: this.state.changeSet.who
      , description: event.target.value
    }});
  }

  handleTick(event) {
    if (this.state.selected) {
      console.log(event.target.value + 'is removed');
      ReactDOM.render(<ModalAlert />, document.getElementById('modalAlert'));
    } else {
      console.log(event.target.value + 'is selected');
    }
    this.setState({selected : event.target.checked});
  }

  updateData(event) {
    this.fetchData().then((data) => {
      this.setState({value : data.userName});
    });
  }

  fetchData() {
    var deff = $.Deferred();
    $.ajax({
      type: 'GET',
      async: true,
      url: 'https://71fe3412-713b-4330-98c7-688705e6fab5-bluemix.cloudant.com/kcucdb/fbc44ada6a5430107ddda34ef67f4673',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      success: function (data, textStatus, jqXHR) {
        console.dir(data);
        deff.resolve(data);
      },
      error: function (xhr, status, error) {
        console.log(xhr.responseText);
        deff.reject();
      }
    });
    return deff.promise();
  }
}

export default Row;
