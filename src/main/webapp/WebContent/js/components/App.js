import React from 'react';
import Footer from './Footer';
import AddTodo from '../containers/AddTodo';
import VisibleTodoList from '../containers/VisibleTodoList';
import PopupContainer from '../containers/PopupContainer';

const App = () => (
  <div>
    <AddTodo />
    <VisibleTodoList />
    <Footer />
    <PopupContainer />
  </div>
);

export default App;
