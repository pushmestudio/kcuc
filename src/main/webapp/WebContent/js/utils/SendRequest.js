/**
 * @file リクエスト送信先とパラメータを渡してリクエストを簡易的に送るためのクラス
 */
class SendRequest {
  /**
  * GETでJSONを取得（非同期）
  */
  static sendGet(uri, param) {
    console.log('ASYNC GET ' + uri);
    var $d = $.Deferred();
    try {
      $.ajax({
        type: 'GET',
        async: true,
        url: uri,
        data: param,
        success: function (data) {
          console.dir(data);
          $d.resolve(data);
        },
        error: function (xhr) {
          console.log(xhr.responseText);
          $d.reject();
        }
      });
    } catch (e) {
      $d.reject();
    }
    return $d.promise();
  }

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
        data: param,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        success: function (data) {
          console.dir(data);
          $d.resolve(data);
        },
        error: function (xhr) {
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
