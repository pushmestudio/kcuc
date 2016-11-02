import React from 'react';
import Footer from './Footer';
import AddTodo from '../containers/AddTodo';
import VisibleTodoList from '../containers/VisibleTodoList';
import Popup from '../containers/Popup';

const App = () => (
  <div>
    <AddTodo />
    <VisibleTodoList />
    <Footer />
    <Popup />
  </div>
);

export default App;
