import SendRequest from '../utils/SendRequest';

export const startFetchPages = () => {
  return {
    type: 'START_FETCH_PAGES'
  };
};

export const successFetchPages = (result) => {
  return {
    type: 'SUCCESS_FETCH_PAGES',
    data: result
  };
};

export const errorFetchPages = () => {
  return {
    type: 'ERROR_FETCH_PAGES'
  };
};

export const fetchPages = (userId) => {
  return (dispatch) => {
    dispatch(startFetchPages());

    let requestPagesParam = {user: userId};
    const kcucPagesApi = '/kcuc/rest-v1/check/pages';
    $('#loader').removeClass('hide'); // ローディングアイコン表示

    SendRequest.sendGet(kcucPagesApi, requestPagesParam).then((res) => {
      console.dir(res);
      if (res.code) {
        dispatch(errorFetchPages());
      } else {
        dispatch(successFetchPages(res));
      }
    }).fail(() => {
      dispatch(errorFetchPages());
    }).always(() => {
      $('#loader').addClass('hide'); // ローディングアイコン非表示
    });
  };
};

export const startFetchUsers = () => {
  return {
    type: 'START_FETCH_USERS'
  };
};

export const successFetchUsers = (result) => {
  return {
    type: 'SUCCESS_FETCH_USERS',
    data: result
  };
};

export const errorFetchUsers = () => {
  return {
    type: 'ERROR_FETCH_USERS'
  };
};

export const fetchUsers = (page) => {
  return (dispatch) => {
    dispatch(startFetchUsers());

    let requestUsersParam = {href: page};
    const kcucUsersApi = '/kcuc/rest-v1/check/users';
    $('#loader').removeClass('hide'); // ローディングアイコン表示

    SendRequest.sendGet(kcucUsersApi, requestUsersParam).then((res) => {
      console.dir(res);
      if (res.code) {
        dispatch(errorFetchUsers());
      } else {
        dispatch(successFetchUsers(res));
      }
    }).fail(() => {
      dispatch(errorFetchUsers());
    }).always(() => {
      $('#loader').addClass('hide'); // ローディングアイコン非表示
    });
  };
};

export const startRegisterPage = () => {
  return {
    type: 'START_REG_PAGE'
  };
};

export const successRegisterPage = (result) => {
  return {
    type: 'SUCCESS_REG_PAGE',
    data: result
  };
};

export const errorRegisterPage = () => {
  return {
    type: 'ERROR_REG_PAGE'
  };
};

export const registerPage = (user, page) => {
  return (dispatch) => {
    dispatch(startRegisterPage());

    let requestRegisterParam = {user: user, href: page};
    const kcucUsersApi = '/kcuc/rest-v1/check/pages';
    $('#loader').removeClass('hide'); // ローディングアイコン表示

    SendRequest.sendPost(kcucUsersApi, requestRegisterParam).then((res) => {
      console.dir(res);
      if (res.code) {
        dispatch(errorRegisterPage());
      } else {
        dispatch(successRegisterPage(res));
      }
    }).fail(() => {
      dispatch(errorRegisterPage());
    }).always(() => {
      $('#loader').addClass('hide'); // ローディングアイコン非表示
    });
  };
};

export const startCancelPage = () => {
  return {
    type: 'START_CANCEL_PAGE'
  };
};

export const successCancelPage = (result) => {
  return {
    type: 'SUCCESS_CANCEL_PAGE',
    data: result
  };
};

export const errorCancelPage = () => {
  return {
    type: 'ERROR_CANCEL_PAGE'
  };
};

// TODO 現時点で購読解除のAPIはできていないため、パラメーターを送ると購読をするようになっている、APIが出来次第URLを切り替える
export const cancelPage = (user, page) => {
  return (dispatch) => {
    dispatch(startCancelPage());

    let requestCancelParam = {user: user, href: page};
    const kcucUsersApi = '/kcuc/rest-v1/check/pages';
    $('#loader').removeClass('hide'); // ローディングアイコン表示

    SendRequest.sendPost(kcucUsersApi, requestCancelParam).then((res) => {
      console.dir(res);
      if (res.code) {
        dispatch(errorCancelPage());
      } else {
        dispatch(successCancelPage(res));
      }
    }).fail(() => {
      dispatch(errorCancelPage());
    }).always(() => {
      $('#loader').addClass('hide'); // ローディングアイコン非表示
    });
  };
};
