[![build status(available on GitLab only)](https://gitlab.com/pushmestudio/kcuc/badges/master/build.svg)](https://gitlab.com/pushmestudio/kcuc/commits/master)

# 全体構成
REST APIは以下`/rest-v1/`、REST APIのお試しに使えるSwaggerは`/v1-docs/`以下にて使用可能。

REST APIは[KCUC for Web](https://gitlab.com/pushmestudio/kcuc-web)や[KCUC for iOS](https://gitlab.com/pushmestudio/kcuc-ios)から呼び出される想定。

また、REST APIを経由して、[Knowledge CenterのAPI](http://www.ibm.com/support/knowledgecenter/v1-docs/)やCloudant DBを利用している。

デプロイされているものは[こちら](https://kcuc.mybluemix.net)から利用可能。

ローカル環境においては、Dockerを使用して環境構築する想定。使用するDockerイメージは[CentOS 7 + Java 8 + Tomcat 8](https://github.com/kirillF/centos-tomcat)としている。

# セットアップ

* Requirement：Java8 or later
* Recommendation: Eclipse Mars or later, Docker
* Optional: Gradle 3.2(Gradle Wrapperというモジュールを用いるので、ローカルにGradleを事前インストールする必要はない)

初回セットアップは、今後どのような方法で開発を実施したいかによって2種類に分かれる。

1. コマンドラインとEclipseを併用する方法(Recommended)

    Eclipse起動前に、プロジェクトトップディレクトリにて`./gradlew eclipse`, `./gradlew build`を実行して事前準備する。

    その後、Eclipseを起動し、`File` > `Import` > `General` > `Existing Project into Workspace`を選択。

    この方法による場合、Eclipse上でGradleのコマンドは実行できないものの、Eclipse Luna等Gradleのプラグインが導入されていないマシンでも容易にセットアップできるのが利点。また、EclipseのGradleプラグインは比較的新しいが、その場合でもそこで発生するバグを回避できる点に優れる。

2. 可能な限りコマンドラインを使わない(Eclipseで完結させる)方法

    Eclipse起動後、`File` > `Import` > `Gradle`を選択。本プロジェクトのルートディレクトリを指した状態でImportを進める。途中、Gradleを選択する画面では、`Gradle Wrapper(Recommended)`のまま進めればOK。

    この方法による場合、Eclipse上でGradleのコマンドが実行できるようになるので便利。また、実行しているGradleのタスクの実行状況が視覚的に把握しやすいのと、サーバーサイドプロジェクトにおいては基本的にオールインワンで済ませることができる点で優れる。


その他、`.project`, `.classpath`ファイルを共有するようにすれば、各自のセットアップ方法の不備による環境差異を最小限に抑えることができるが、一方で、基本的に同じEclipseのバージョンを使用しないと頻繁にdiffが発生してしまうので煩わしいという懸念事項があるため、チームの方針をいずれかに倒す必要がある。

ここまでのセットアップが済んだら、EclipseがJS系のモジュールを理解できないために発生するエラー表示を抑制するための手続きおよびソースコード標準化のための`checkstyle`のセットアップを実施する。

## EclipseのValidationの停止
Eclipse上にて、プロジェクトルートディレクトリを選択した状態で右クリックし`Properties`を表示。メニュー内下半にある`Validation`を選択し、次の2つをチェックする。

- `Enable project specific settings`
- `Suspend all validators`

設定完了後、再びプロジェクトを右クリックし、`Validate`を実施すれば、既存のJS関連のエラーは消える。

## Checkstyleの導入
`Help` > `Eclipse Marketplace`を表示後、`Checktyle`で検索する。その後表示される、`Checkstyle Plug-in`をインストール。

インストール後、プロジェクトを右クリックし、`Properties`を表示。メニュー内上方にある`Checkstyle`を選択する。

`Local Check Configuration`を選択し、`New`で表示される設定画面を次のようにする。

- Type: `Project Relative Configuration`
- Name: `kcuc.checkstyle`
- Location: `/kcuc/checkstyle.xml`(or Browseからプロジェクトディレクトリ直下にあるcheckstyle.xmlを選択する)
- Description: (空白のまま)
- Advanced options: (チェックしない)

続いて、`Main`を選択肢し、`Simple - use the following check configuraiton for all files`から`kcuc.checkstyle - (Local)`を選択する。

上記設定後、プロジェクトを右クリックし、`Checkstyle` > `Activate Checkstyle`を実施し、設定完了。

Checkstyleによって、未使用の変数名等や`if`や変数の後のスペースがついているかのチェックなどが実施される。少なくともマージリクエストを出す時点においては`Warning`(黄色)以上の指摘事項がない状態にすること。(checkstyleのスタイルに抵触してしまう、プロジェクト上の困難な設定等々がある際にはチームに相談すること)

# Build and Deploy
ビルド及びWARの作成はGradleのコマンドによって実行する。前述のとおり、Dockerイメージを利用するが`/opt/tomcat/webapps`ディレクトリをローカルとDockerエンジンとの間で共有することで、ローカル環境でのデプロイを容易にしている。

ビルド・WAR作成の準備が整ったら、`./gradlew build deploy`を実行する。ビルド及びテスト実行後、`/opt/tomcat/webapps/ROOT.war`としてデプロイされる。なお、パーミッションが適切に設定されていないとファイルが展開されない場合がある。`/opt/tomcat/webapps`ディレクトリの読み書きが実行ユーザー・Dockerいずれからでも可能になるよう、パーミッションの設定に注意すること。

## Build時の環境変数とプロパティファイル
Cloudant接続に際し、接続情報を読み込ませる必要がある。現在はこの接続情報を、環境変数あるいはプロパティファイルから読み取るようにしている。

次の環境変数が定義されている場合には、その値を読み取り使用する。

* `CLOUDANT_ACCOUNT`: CloudantのアカウントID
* `CLOUDANT_USER`: DB接続用のユーザーID
* `CLOUDANT_PW`: DB接続用のユーザーIDに紐づくパスワード

BluemixのRuntimeからCloudantにアクセスする場合にはこれらを事前に環境変数として定義することにより読み取るが、ローカルのDocker環境からCloudantにアクセスする場合にはプロパティファイルを用いる方法を推奨している。

使用するプロパティファイルについては`${プロジェクトルート}/src/main/resources/jp/pushmestudio/credentials`ディレクトリ以下に`cloudant.propertiesとして配置する。プロパティファイルに記載すべき値については別途担当者に確認のこと。`

# その他の注意点
サーバーサイドのプロジェクトは、GitにPushするとビルド＋テストが実行される。この際、エラーになった場合にはマージができないように設定している。

そのため、少なくともテストが書かれているコードについては、変更時に既存のテストを尊重し、その変更によってテストが失敗になっていないか確認すること。(回避困難な場合には、一時的に`@Ignore`アノテーションを付すことでたテスト対象から外すことができる)

また、本番環境へのデプロイは、別途本番用のディレクトリへのPushによる自動デプロイを実現しているので、別途担当者にて対応すること。
