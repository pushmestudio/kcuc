# Getting Started
ひとまずの追加。詳しくはwiki参照。

## クライアントサイドセットアップ

事前にyarn要導入。

導入済なら、`webapp`ディレクトリにて`yarn install`。
現時点では次のコマンドを用意している。

- JavaScript文法チェック：`yarn lint`
- 文法チェック＆JavaScriptのビルド・依存性解決＆JsDocの出力：`yarn build`
- JavaScriptのビルド＆ローカルサーバー起動：`yarn start`
- JsDocの出力：`yarn doc`

## サーバーサイドセットアップ

事前にJava 8 or later要導入。

プロジェクトトップディレクトリにて`./gradlew eclipse`, `./gradlew build`を実行。

クライアントサイドセットアップが済んでいない状態で実施するとクライアントサイドは何もソースコードが含まれない状態になる。
