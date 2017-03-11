[![build status(available on GitLab only)](https://gitlab.com/pushmestudio/kcuc/badges/master/build.svg)](https://gitlab.com/pushmestudio/kcuc/commits/master)

# Overview
[IBM Knowledge Center REST API Documentation](https://www.ibm.com/support/knowledgecenter/v1-docs/#/)を利用し、Knowledge Centerに格納されているドキュメントの更新を確認するためのREST APIアプリケーション。

使用しているライブラリやミドルウェアの主要なものは次のとおり。

* Tomcat: Runtime Servlet Container(Tomcatを使用しない場合はservlet-apiなど、一部のライブラリを`build.gradle`に追加する必要がある)
* Cloudant: NoSQL DB
* JAX-RS: REST API Framework
* Swagger: API Document

デプロイされているものは[こちら](https://kcuc.mybluemix.net)から利用可能。

ローカル環境においては、Dockerを使用して環境構築する想定。使用するDockerイメージは[CentOS 7 + Java 8 + Tomcat 8](https://github.com/kirillF/centos-tomcat)としている。

# Quick Start

* Pre-requirement: Install Java8 or later

1. プロパティファイルの設置

    使用するプロパティファイルについては`${プロジェクトルート}/src/main/resources/jp/pushmestudio/credentials/`以下に`cloudant.properties`として配置し、以下の値を記載する。

    ```properties
    CLOUDANT_ACCOUNT=CloudantのアカウントID
    CLOUDANT_USER=DB接続用のユーザーID
    CLOUDANT_PW=DB接続用のユーザーIDに紐づくパスワード
    ```

    なお、プロパティファイルを用意せずに、同名の環境変数を用意している場合はそちらの変数の読み取りを優先する。

2. GradleでWar生成

    `./gradew build`にてビルドを実行しWarを生成する。`gradlew`コマンド初回実行時にこのプロジェクトで事前に定めたGradleがダウンロードされるため、事前にGradleのインストールなどは不要。

3. 生成されたWarのデプロイ

    `build/libs`以下に生成されたWarをデプロイする。なお、現行ではBluemix上にデプロイしており、標準的なビルドパックを使用している都合上、コンテキストルートは`/`とすることを前提にしている。そのため、Tomcatにデプロイする場合には`ROOT.war`にファイル名を変更してからデプロイする。

    事前想定のDockerなどを使用している場合(`/opt/tomcat/webapps/`ディレクトリがある場合)には、`./gradlew deploy`(or `./gradlew build deploy`)実行により、`ROOT.war`にリネーム + デプロイ可能。


# 開発環境セットアップ

* Requirement：Java8 or later
* Recommendation: Eclipse Mars or later(and install Eclipse Buildship Gradle Plugins), Docker

    EclipseのAll in oneパッケージであるPleiadesを利用する場合、Eclipse Mars(4.5)以降にはEclipse Buildshipは同梱されているので、Buildshipを個別に入れる必要はない。

## Eclipseへのプロジェクトのインポート
初回セットアップは、今後どのような方法で開発を実施したいかによって2種類に分かれる。

1. Eclipse内で可能な限りコマンドラインを使わない(Eclipseで完結させる)方法((Recommended)

    Eclipse起動後、`File` > `Import` > `Gradle`を選択。本プロジェクトのルートディレクトリを指した状態でImportを進める。途中、Gradleを選択する画面では、`Gradle Wrapper(Recommended)`のまま進めればOK。

    この方法による場合、Eclipse上でGradleのコマンドが実行できるようになるので便利。また、実行しているGradleのタスクの実行状況が視覚的に把握しやすいのと、サーバーサイドプロジェクトにおいては基本的にオールインワンで済ませることができる点で優れる。


2. コマンドラインとEclipseを併用する方法

    Eclipse起動前に、プロジェクトトップディレクトリにて`./gradlew eclipse`を実行して事前準備する。(このコマンドにて、Eclipseが必要とする`.project`や`.classpath`が設定される。)

    その後、Eclipseを起動し、`File` > `Import` > `General` > `Existing Project into Workspace`を選択。

    この方法による場合、Eclipse上でGradleのコマンドは実行できないものの、Eclipse Luna等Gradleのプラグインが導入されていないマシンでも容易にセットアップできるのが利点。また、EclipseのGradleプラグインは比較的新しいが、その場合でもそこで発生するバグを回避できる点に優れる。

ここまでのセットアップが済んだら、EclipseがJS系のモジュールを理解できないために発生するエラー表示を抑制するための手続きおよびソースコード標準化のための`checkstyle`のセットアップを実施する。

**注意**：ライブラリを追加・削除した際には`./gradlew eclipse`を実行する必要がある。

## EclipseのValidationの停止
Eclipse上にて、プロジェクトルートディレクトリを選択した状態で右クリックし`Properties`を表示。メニュー内下半にある`Validation`を選択し、次の2つをチェックする。

- `Enable project specific settings`
- `Suspend all validators`

設定完了後、再びプロジェクトを右クリックし、`Validate`を実施すれば、既存のJS関連のエラーは消える。

## Checkstyleの導入
ある程度標準的なコードの作りにするために静的チェックツールを使う。Googleが公開しているCheckStyleをベースに、Eclipse上で使用しやすいように、また、ルールが厳しくなりすぎないようにカスタマイズしたものを使用している。

1. EclipseにCheckStyleを導入する

    `Help` > `Eclipse Marketplace`を表示後、`Checktyle`で検索する。その後表示される、`Checkstyle Plug-in`をインストール。(All in oneを利用している場合はインストールされているのでこのステップをパスして次に進む)

2. CheckStyleの導入準備をする

    インストール後、プロジェクトを右クリックし、`Properties`を表示。メニュー内上方にある`Checkstyle`を選択する。

    `Local Check Configuration`を選択し、`New`で表示される設定画面を次のようにする。

    - Type: `Project Relative Configuration`
    - Name: `kcuc.checkstyle`
    - Location: `/kcuc/checkstyle.xml`(or Browseからプロジェクトディレクトリ直下にあるcheckstyle.xmlを選択する)
    - Description: (空白のまま)
    - Advanced options: (チェックしない)

    続いて、`Main`を選択肢し、`Simple - use the following check configuraiton for all files`から`kcuc.checkstyle - (Local)`を選択する。

3. CheckStyleを適用する

    上記設定後、プロジェクトを右クリックし、`Checkstyle` > `Activate Checkstyle`を実施し、設定・適用完了。

Checkstyleによって、未使用の変数名等や`if`や変数の後のスペースがついているかのチェックなどが実施される。少なくともマージリクエストを出す時点においては`Warning`(黄色)以上の指摘事項がない状態にすること。(checkstyleのスタイルに抵触してしまう、プロジェクト上の困難な設定等々がある際にはチームに相談すること)

# Build and Deploy
基本的な内容はQuick Startを参照する。

## よくあるエラーとその対策
ビルド・デプロイ時によくあるエラーとその原因としては、次のようなものがある。

* ビルドに失敗する、テストに失敗する

    * プロパティファイルor環境変数がセットされていない

        プロパティ関連はQuick Start内を参照の上、対応する。

    * インターネットに接続されていない

        インターネット接続については、現行ではテスト時にKnowledge Center及びDBaaSを利用しているため必要となる。諸般の事情により一時的にテストなしでのビルドが必要な場合には、`./gradlew build -x test`によってテストを省略してビルドすることができる。

    * 既存テストに影響のある変更をしたがテストケースを修正していない

        `build/reports/tests/test/index.html`にテストのレポートが出力されるので、ブラウザで参照の上、修正をする。諸般の事情により一時的にテストなしでのビルドが必要な場合には、`./gradlew build -x test`によってテストを省略してビルドすることができる。

* ローカルでデプロイしたアプリが見れない

    * ローカル環境のパーミッション設定がされていない

        パーミッションが適切に設定されていないとデプロイしたWarがTomcatのディレクトリ上に展開されない場合がある。`/opt/tomcat/webapps`以下に`ROOT`ディレクトリが生成され、`WEB-INF/classes`にクラスファイルが含まれているかなどを確認し、そのようになっていなければ、ディレクトリの読み書きが実行ユーザー・Dockerいずれからでも可能になるよう設定にする。

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
