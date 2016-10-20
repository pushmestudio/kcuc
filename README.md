# Getting Started
ひとまずの追加。詳しくはwiki参照。

## クライアントサイドセットアップ

事前にyarn要導入。

導入済なら、`webapp`ディレクトリにて`yarn install`。
その後、`node_modules/bower/bin/bower install`。

JSX変換には`./node_modules/babel-cli/bin/babel.js WebContent/js/ --out-dir WebContent/build/`

## サーバーサイドセットアップ

事前にJava 8 or later要導入。

プロジェクトトップディレクトリにて`./gradlew eclipse`, `./gradlew build`を実行。

クライアントサイドセットアップが済んでいない状態で実施するとクライアントサイドは何もソースコードが含まれない状態になる。
