class SendRequest {
  /**
   * POSTでJSONを送信してJSONを取得（非同期）
   */
  static sendPost(uri, param) {
    let $d = $.Deferred();
    try {
      $.ajax({
        type: 'POST',
        async: true,
        url: uri,
        dataType: 'json',
        data: JSON.stringify(param),
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
          console.dir(data);
          $d.resolve(data);
        },
        error: function (xhr, status, error) {
          console.log(xhr.responseText);
          console.log(error);
          $d.reject();
        }
      });
    } catch (e) {
      $d.reject();
    }
    return $d.promise();
  }

  /**
  * GETでJSONを取得（非同期）
  */
  static sendGet(uri) {
    console.log('ASYNC GET ' + uri);
    var $d = $.Deferred();
    try {
      $.ajax({
        type: 'GET',
        async: true,
        url: uri,
        dataType: 'json',
        success: function (data) {
          console.dir(data);
          $d.resolve(data);
        },
        error: function (xhr, status, error) {
          console.log(xhr.responseText);
          $d.reject();
        }
      });
    } catch (e) {
      $d.reject();
    }
    return $d.promise();
  }
}

export default SendRequest;
