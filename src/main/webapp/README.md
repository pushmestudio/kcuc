# クライアントサイドセットアップ

* Requirement: Yarn導入, NodeJS導入
* Recommendation: Atom + パッケージ2種(linter by atom-community, linter-eslint by AtomLinter)

`webapp`ディレクトリにて`yarn install`。
現時点では次のコマンドを用意している。

- `yarn build`：文法チェック＆JavaScriptのビルド・依存性解決＆JsDocの出力
- `yarn lint`：JavaScript文法チェック
- `yarn webpack`：JavaScriptのビルドのみ(文法チェックを割愛、開発時のみの想定)
- `yarn start`：JavaScriptのビルド＆ローカルサーバー起動

ReduxのサンプルTodoアプリを一部拡張したものが用意してある。

## Yarnについて
### Yarnでできること(vs npm)

1. ローカルインストール
2. 複数のレジストリ追加
3. 高速インストール
4. パッケージのバージョンを固定化
5. 各開発者のインストールを均一化(npmだとnode_modulesの中身が人によって異なるとか)

https://auth0.com/blog/five-things-you-can-do-with-yarn/

### 導入

Macの場合はhomebrew、Windowsの場合はインストーラーを使用する。

Linuxで且つ、Node導入済の場合、一部設定を改める必要がありとのこと。

Mac or Linuxの場合、`$HOME/.yarn/bin`にパスを通す必要がある。

https://yarnpkg.com/en/docs/install

公式には書かれていないが、`npm install yarn`でもできるらしい

### 使い方
基本的にnpmと同じような使い方ができる。`package.json`を用いる。

* インストール：`yarn` or `yarn install`
* プロジェクトで初めてyarnを使う場合：`yarn init`
* パッケージ追加：`yarn add [package]`
* パッケージ除去：`yarn remove [package]`

https://yarnpkg.com/en/docs/usage

今回のプロジェクトで実施した例：
```
yarn init

(プロジェクト名などを入力)

yarn add react react-redux
yarn add eslint jsdoc --dev
```

## 使用しているツール(特徴的なもの)類について

* ES2015：モダンブラウザでサポートされている標準的となりつつあるJSの仕様及びそれに沿ったJSの書き方。`var`を基本的には使用せず`let`と`const`を使う、アロー関数を使って関数宣言を書く、引数にデフォルトの値を指定できる、for...of文が使える(Javaでいうところの拡張for文)、あたりがわかりやすい変化点。
* React：状態の変化があったらDOMを作成し直すことで、確実な状態の遷移を実現するライブラリ。MVCに当てはめるとすれば、Viewに該当。
* Redux(Flux)：Fluxというシステムデザイン(データの流れをある流れに限定することによってカオスに陥らないように管理しようとするデザイン)の実装(フレームワーク)の一種。Fluxのデザインの中でViewの部分をReactにて実装している。Fluxを実現しているフレームワークの中で最も人気。
* Webpack：NodeJSのモジュール利用方法(`import` / `require()`)を使用できるようにするためのビルドツールの一種。同種のツールに、`Browserify`が有名。
* Babel：コンパイラ。例えばES2015で書いたものをES2015に対応していないブラウザ向けにコンパイルできる。
* ESLint：Lint(文法チェックツール)のES2015向け。
* JSDoc：JSDocの仕様に沿ったコメントを書くことで自動的にドキュメントを生成するツール。


Lintについては、`yarn build`時に自動でチェックし、文法チェックルールを違反している場合にはエラーを返してビルドが進まないようにしてある。毎回`yarn build`や`yarn lint`で確認しても良いが、Atomを使用していて必要なパッケージを入れている場合は、プロジェクトルートディレクトリを`webapp`(=ルートディレクトリ直下に`.eslintrc`を配置)にすることで自動チェック可能。

Webpackについては、JSのエントリーポイントを増やす場合に`webpack.config.js`を編集する必要あり。それ以外の場合は、現在の構成のまま変更不要。ビルドが完了すると、`build`ディレクトリ以下に、関連したJSファイルをすべて一箇所にまとめたファイルが出力されるので、htmlではこちらを読み込んで使用する。

## React/Reduxについて
実装に入る前に基本概念を理解する必要あり。基本的に公式を読みつつ、不明点についてブログ記事等を参照する形が良い。 http://redux.js.org/docs/basics/

Reduxを利用する上で理解すべき概念は次の通り。

- `view`:要は見た目、イベントバインディングを含む
    - `component`:見える画面を部分部分で切り出したパーツのイメージ、画面に表示を追加するならこれを追加するイメージ。また、各componentはcontainerから受け取る値を事前にPropsとして持っておき、stateから取り出した値などを格納・自身の処理に使う

    - `container`:State(アプリの状態）をstoreから受け取ってcomponentに渡したり、実際にイベントが呼ばれた場合にaction createrを呼ぶ。actionと1対1の関係。componentとは主従(親子)関係は特にない。


- `action`:JSオブジェクトとして、Key:Valueで実際の処理内容を判断させるための値を記載する。処理内容そのものはReducerにて書く。
    - `action creater`:ContainerからDispatch(actionをreducerに渡す行為)するために呼び出す関数で、戻り値がactionになっている


- `store`:アプリで1つだけ持つ、アプリの状態管理箇所。受け取ったactionと現在のState(アプリの状態）を元に次のStateをViewに返す
    - `reducer`:処理の核となる、DispatchによってViewからActionを受け取り、Stateを更新する

    - `provider`:Storeとアプリを紐づける役割

    - `middleware`:reducerの処理に必要となるモジュールの読み込み(例：非同期通信用のモジュールredux-thunk)などを行う


### 実装時に追加する対象
下記それぞれ追記実施後にビルドが必要になる。

1. パーツを足すだけの時
    - componentsに追加対象のファイルを追加
    - viewを統括している`components/App`に呼び出し対象として記載を追記

2. アクションを足す時
    - containersに追加対象のファイルを追加
    - viewを統括している`components/App`に呼び出し対象として記載を追記
    - `actions/index`にアクション名やパラメータを追記
    - reducersにアクション名などに対応したファイルを追加
    - reducerやmiddlewareを統括している`reducers/index`に追加対象のreducerを追記

3. Stateの値も使用して画面描画する時
    - reducerにviewで使用するStateをreturnさせるように追記
    - Stateを利用するComponent内で`Proptypes`の設定を追記
    - container内の`connect`処理(storeの値を読むためのもの)において、stateをcomponentのPropに格納するための処理を追記
    - (要追加調査)Propで受け取った値を、なにかをトリガーにしてRenderする処理が必要(Renderしないと、値自体は持っているが画面には反映されない状態になる)

## 新たなJSライブラリを使用する際の手順

1. `webapp`ディレクトリ上にて`yarn add XXX` または `yarn add XXX --dev`(`--dev`は開発時のみ使用することの明示)

    開発時のみ使用するか否かについては、例えばあるJSファイルから`import` / `require`するのであれば開発用ではない。逆に、ビルドのために使用するものや文法チェックのような(例：`eslint`, `webpack`)ものについては`--dev`オプションをつけること。

2. 使用したいJS上にて`import`で当該ライブラリを読み込み

    `{}`付きのimportは、import元(from)から特定のオブジェクトのみをimportするもの。また、`yarn.lock`ファイルは、各開発者間の`node_modules`のバージョン差異をなくすために必要となるものなので、バージョン管理対象とすること。

## 参考リンク

* [Introduction · Redux](http://redux.js.org/docs/introduction/) 利用する上では必読。また、Examplesとして公開されている`Shopping Cart`は今後の拡張において重要度が高くなりそう。
* [ReduxのExampleを徹底図解 | 人生と仕事を楽しむブログ](http://blog.andgenie.jp/articles/1021) Reduxのサンプルアプリについて日本語で解説していてわかりやすい
* [André Staltz - Unidirectional User Interface Architectures](http://staltz.com/unidirectional-user-interface-architectures.html) 複数の記事で参照元となっている
* [React-Redux をわかりやすく解説しつつ実践的に一部実装してみる - anti-good.](http://ma3tk.hateblo.jp/entry/2016/06/20/182232) 情報の正しさという点では怪しいところもあるものの、理解の助けにはなりそう。
