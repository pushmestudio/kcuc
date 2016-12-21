import { combineReducers } from 'redux';
import kcUser from './kcUser';
import kcPage from './kcPage';

const rootReducer = combineReducers({
  kcUser
  , kcPage
});

export default rootReducer;
