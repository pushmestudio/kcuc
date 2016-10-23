# Getting Started
ひとまずの追加。詳しくはwiki参照。

## クライアントサイドセットアップ

事前にyarn要導入。

導入済なら、`webapp`ディレクトリにて`yarn install`。

詳細は[クライアントサイドのREADME](tree/master/src/main/webapp)にて。

## サーバーサイドセットアップ

事前にJava 8 or later要導入。

プロジェクトトップディレクトリにて`./gradlew eclipse`, `./gradlew build`を実行。

クライアントサイドセットアップが済んでいない状態で実施するとクライアントサイドは何もソースコードが含まれない状態になる。
