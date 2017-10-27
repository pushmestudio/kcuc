[![build status(available on GitLab only)](https://gitlab.com/pushmestudio/kcuc/badges/master/build.svg)](https://gitlab.com/pushmestudio/kcuc/commits/master)

# Overview
[IBM Knowledge Center REST API Documentation](https://www.ibm.com/support/knowledgecenter/v1-docs/#/)を利用し、Knowledge Centerに格納されているドキュメントの更新を確認するためのREST APIアプリケーション。

使用しているライブラリやミドルウェアの主要なものは次のとおり。

* Docker
* Tomcat: Runtime Servlet Container(Tomcatを使用しない場合はservlet-apiなど、一部のライブラリを`build.gradle`に追加する必要がある)
* Cloudant: NoSQL DB
* JAX-RS(Jersey Framework): REST API Framework
* Swagger: API Document

デプロイされているものは[こちら](https://kcuc.mybluemix.net)から利用可能。

ローカル環境においてはDockerを使用して環境構築する想定。Dockerを使用する場合、Dockerイメージは[CentOS 7 + Java 8 + Tomcat 8](https://github.com/kirillF/centos-tomcat)としている。

次のコマンドでログインしKCUCのDockerイメージをPullする。(ログインにはGitLabのアカウントを使用する)

```
docker login registry.gitlab.com
docker pull registry.gitlab.com/pushmestudio/kcuc:latest
```

その後、次のコマンドで実際にコンテナを動かす。(上は記載すべき内容、下はそれを具体的に入れた例)

```sh
#docker run -v ${ローカルのwar配置のパス}:/usr/local/tomcat/webapps\
# -v ${ローカルのlog出力先のパス:/usr/local/tomcat/logs} \
# -p ${ローカルのポート}:8080 -i -t --name ${ローカルで管理する任意のコンテナの名前} registry.gitlab.com/pushmestudio/kcuc

docker run -v /opt/tomcat/webapps:/usr/local/tomcat/webapps \
 -v /opt/tomcat/logs:/usr/local/tomcat/logs \
 -p 8080:8080 -i -t --name kcuc-container registry.gitlab.com/pushmestudio/kcuc
```

なお、特定のOSにおいてはPermissionの設定を適切に実施しないとWARが見れないことやDockerのコマンドに失敗することがあるので確認の上実行すること。また、1度上記の形でコンテナを動かした後は`docker start ${ローカルで管理する任意のコンテナの名前}`で開始でき、WARの入れ替えも単にWARファイルを置き換えるだけで済む。(ホットスワップ、再起動不要)

# Quick Start

* Pre-requirement: Install Java8 or later

1. プロパティファイルの設置

    使用するプロパティファイルについては`${プロジェクトルート}/src/main/resources/jp/pushmestudio/credentials/`以下に`cloudant.properties`として配置し、以下の値を記載する。

    ```properties
    CLOUDANT_ACCOUNT=CloudantのアカウントID
    CLOUDANT_USER=DB接続用のユーザーID
    CLOUDANT_PW=DB接続用のユーザーIDに紐づくパスワード
    ```

    また、Snippetに保存されているプロパティファイルを使用する場合は[ここ](https://gitlab.com/pushmestudio/kcuc/snippets/34933)からダウンロードして配置する。なお、プロパティファイルを用意せずに、同名の環境変数を用意している場合はそちらの変数の読み取りを優先する。

2. GradleでWar生成

    `./gradew build`にてビルドを実行しWarを生成する。`gradlew`コマンド初回実行時にこのプロジェクトで事前に定めたGradleがダウンロードされるため、事前にGradleのインストールなどは不要。

3. 生成されたWarのデプロイ

    `build/libs`以下に生成されたWarをデプロイする。事前想定のDockerなどを使用している場合(`/opt/tomcat/webapps/`ディレクトリがある場合)には、`./gradlew deploy`(or `./gradlew build deploy`)実行により、デプロイ可能。

# 開発環境セットアップ

* Requirement：Java8 or later
* Recommendation: Eclipse Mars or later(and install Eclipse Buildship Gradle Plugins), Docker

    EclipseのAll in oneパッケージであるPleiadesを利用する場合、Eclipse Mars(4.5)以降にはEclipse Buildshipは同梱されているので、Buildshipを個別に入れる必要はない。

## Eclipseへのプロジェクトのインポート
初回セットアップは、今後どのような方法で開発を実施したいかによって2種類に分かれる。

### Eclipse内で可能な限りコマンドラインを使わない(Eclipseで完結させる)方法(Recommended)

Eclipse起動後、`File` > `Import` > `Gradle`を選択。本プロジェクトのルートディレクトリを指した状態でImportを進める。途中、Gradleを選択する画面では、`Gradle Wrapper(Recommended)`のまま進めればOK。

この方法による場合、Eclipse上でGradleのコマンドが実行できるようになるので便利。また、実行しているGradleのタスクの実行状況が視覚的に把握しやすいのと、サーバーサイドプロジェクトにおいては基本的にオールインワンで済ませることができる点で優れる。


### コマンドラインとEclipseを併用する方法

Eclipse起動前に、プロジェクトトップディレクトリにて`./gradlew eclipse`を実行して事前準備する。(このコマンドにて、Eclipseが必要とする`.project`や`.classpath`が設定される。)

その後、Eclipseを起動し、`File` > `Import` > `General` > `Existing Project into Workspace`を選択。

この方法による場合、Eclipse上でGradleのコマンドは実行できないものの、Eclipse Luna等Gradleのプラグインが導入されていないマシンでも容易にセットアップできるのが利点。また、EclipseのGradleプラグインは比較的新しいが、その場合でもそこで発生するバグを回避できる点に優れる。

**注意**：併用している場合でライブラリを追加・削除した際には`./gradlew eclipse`を実行する必要がある。


ここまでのセットアップが済んだら、EclipseがJS系のモジュールを理解できないために発生するエラー表示を抑制するための手続きおよびソースコード標準化のための`checkstyle`のセットアップを実施する。

## EclipseのValidationの停止
(特にエラーが出ていない場合はスキップしても良い) Eclipse上にて、プロジェクトルートディレクトリを選択した状態で右クリックし`Properties`を表示。メニュー内下半にある`Validation`を選択し、次の2つをチェックする。

- [x] `Enable project specific settings`
- [x] `Suspend all validators`

設定完了後、再びプロジェクトを右クリックし、`Validate`を実施すれば、既存のJS関連のエラーは消える。

# Build and Deploy
基本的な内容はQuick Startを参照する。

## よくあるエラーとその対策
ビルド・デプロイ時によくあるエラーとその原因としては、次のようなものがある。

### ビルドに失敗する、テストに失敗する

* プロパティファイルor環境変数がセットされていない

    プロパティ関連はQuick Start内を参照の上、対応する。

* インターネットに接続されていない

    インターネット接続については、現行ではテスト時にKnowledge Center及びDBaaSを利用しているため必要となる。諸般の事情により一時的にテストなしでのビルドが必要な場合には、`./gradlew build -x test`によってテストを省略してビルドすることができる。

* 既存テストに影響のある変更をしたがテストケースを修正していない

    `build/reports/tests/test/index.html`にテストのレポートが出力されるので、ブラウザで参照の上、修正をする。諸般の事情により一時的にテストなしでのビルドが必要な場合には、`./gradlew build -x test`によってテストを省略してビルドすることができる。

### ローカルでデプロイしたアプリが見れない(Docker)

* ローカル環境のパーミッション設定がされていない

    パーミッションが適切に設定されていないとデプロイしたWarがTomcatのディレクトリ上に展開されない場合がある。`/opt/tomcat/webapps`以下に`kcuc`ディレクトリが生成され、`WEB-INF/classes`にクラスファイルが含まれているかなどを確認し、そのようになっていなければ、ディレクトリの読み書きが実行ユーザー・Dockerいずれからでも可能になるよう設定にする。

    Macユーザーの場合は、Dockerにおいて共有ディレクトリとして`/opt/tomcat`を指定する必要がある。

## Build時の環境変数とプロパティファイル
既にQuick Startにて触れている内容だが、Cloudant接続に際し、接続情報を読み込ませる必要がある。現在はこの接続情報を、環境変数あるいはプロパティファイルから読み取るようにしている。

次の環境変数が定義されている場合には、その値を読み取り使用する。

```properties
CLOUDANT_ACCOUNT=CloudantのアカウントID
CLOUDANT_USER=DB接続用のユーザーID
CLOUDANT_PW=DB接続用のユーザーIDに紐づくパスワード
```

BluemixのRuntimeからCloudantにアクセスする場合にはこれらを事前に環境変数として定義することにより読み取るが、ローカルのDocker環境からCloudantにアクセスする場合にはプロパティファイルを用いる方法を推奨している。

# その他の注意点
サーバーサイドのプロジェクトは、GitにPushするとビルド＋テストが実行される。この際、エラーになった場合にはマージができないように設定している。

そのため、少なくともテストが書かれているコードについては、変更時に既存のテストを尊重し、その変更によってテストが失敗になっていないか確認すること。(一時的なテスト回避については既に触れたとおり、`./gradlew build -x test`)

また、本番環境へのデプロイは、別途本番用のディレクトリへのPushによる自動デプロイを実現しているので、別途担当者にて対応すること。
