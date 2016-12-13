import SendRequest from '../utils/SendRequest';

export const showSample = (text) => {
  return {
    type: 'SAMPLE',
    text
  };
};

export const startFetchData = () => {
  return {
    type: 'START_FETCH_DATA'
  };
};

export const successFetchData = (result) => {
  return {
    type: 'SUCCESS_FETCH_DATA',
    data: result
  };
};

export const errorFetchData = () => {
  return {
    type: 'ERROR_FETCH_DATA'
  };
};

export const fetchData = () => {
  return (dispatch) => {
    dispatch(startFetchData());

    const cloudantDb = 'https://71fe3412-713b-4330-98c7-688705e6fab5-bluemix.cloudant.com/kcucdb/fbc44ada6a5430107ddda34ef67f4673';
    SendRequest.sendGet(cloudantDb).then((res) => {
      if (res) {
        console.log('fetch done');
        dispatch(successFetchData(res));
      } else {
        console.log('fetch fail');
        dispatch(errorFetchData());
      }
    });
  };
};
