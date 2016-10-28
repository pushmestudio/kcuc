# Getting Started
ひとまずの追加。気付いたベースで随時更新すること。

## 全体構成


## クライアントサイドセットアップ

* Requirement: Yarn, NodeJS

初回セットアップには、`webapp`ディレクトリにて`yarn install`。

詳細は[クライアントサイドのREADME](tree/master/src/main/webapp)にて。

## サーバーサイドセットアップ

* Requirement：Java8 or later
* Recommendation: Eclipse Mars or later

初回セットアップは、今後どのような方法で開発を実施したいかによって2種類に分かれる。

1. 可能な限りコマンドラインを使わない(Eclipseで完結させる)方法

Eclipse起動後、`File` > `Import` > `Gradle`を選択。本プロジェクトのルートディレクトリを指した状態でImportを進める。途中、Gradleを選択する画面では、`Gradle Wrapper(Recommended)`のまま進めればOK。

この方法による場合、Eclipse上でGradleのコマンドが実行できるようになるので便利。また、実行しているGradleのタスクの実行状況が視覚的に把握しやすいのと、サーバーサイドプロジェクトにおいては基本的にオールインワンで済ませることができる点で優れる。


2. コマンドラインとEclipseを併用する方法

Eclipse起動前に、プロジェクトトップディレクトリにて`./gradlew eclipse`, `./gradlew build`を実行して事前準備する。

その後、Eclipseを起動し、`File` > `Import` > `General` > `Existing Project into Workspace`を選択。

この方法による場合、Eclipse上でGradleのコマンドは実行できないものの、Eclipse Luna等Gradleのプラグインが導入されていないマシンでも容易にセットアップできるのが利点。また、EclipseのGradleプラグインは比較的新しいが、その場合でもそこで発生するバグを回避できる点に優れる。


なお、いずれの方法で初回セットアップする場合でも、クライアントサイドセットアップが済んでいない状態で実施するとクライアントサイドは何もソースコードが含まれない状態になる。


`.project`, `.classpath`ファイルを共有するようにすれば、各自のセットアップ方法の不備による環境差異を最小限に抑えることができるが、一方で、基本的に同じEclipseのバージョンを使用しないと頻繁にdiffが発生してしまうので煩わしいという懸念事項があるため、チームの方針をいずれかに倒す必要がある。


ここまでのセットアップが済んだら、EclipseがJS系のモジュールを理解できないために発生するエラー表示を抑制するための手続きおよびソースコード標準化のための`checkstyle`のセットアップを実施する。

### EclipseのValidationの停止
Eclipse上にて、プロジェクトルートディレクトリを選択した状態で右クリックし`Properties`を表示。メニュー内下半にある`Validation`を選択し、次の2つをチェックする。

- `Enable project specific settings`
- `Suspend all validators`

設定完了後、再びプロジェクトを右クリックし、`Validate`を実施すれば、既存のJS関連のエラーは消える。

### Checkstyleの導入
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

## その他の注意点
サーバーサイドのプロジェクトは、GitにPushするとビルド＋テストが実行される。この際、エラーになった場合にはマージができないように設定している。

そのため、少なくともテストが書かれているコードについては、変更時に既存のテストを尊重し、その変更によってテストが失敗になっていないか確認すること。(回避困難な場合には、一時的に`@Ignore`アノテーションを付すことでたテスト対象から外すことができる)
