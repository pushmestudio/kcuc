## クライアントサイドセットアップ

* Requirement: Yarn導入, NodeJS導入

`webapp`ディレクトリにて`yarn install`。
現時点では次のコマンドを用意している。

- 文法チェック＆JavaScriptのビルド・依存性解決＆JsDocの出力：`yarn build`
- JavaScript文法チェック：`yarn lint`
- JavaScriptのビルドのみ(開発時のみの想定)：`yarn webpack`
- JavaScriptのビルド＆ローカルサーバー起動：`yarn start`

ReduxのサンプルTodoアプリを使用できる状態にしてある。

詳細はhttp://redux.js.org/docs/basics/
