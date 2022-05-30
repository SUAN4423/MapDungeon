# backend

- cmd: エントリーポイント
- internal
  - app
    - handler, usecase を書く
  - model
    - ドメイン駆動設計のドメイン, 仕様をコードに落とし込む
    - エンティティ, 値オブジェクト
  - service
    - 外部サービスとの接続部分
      - データベースとか外部 API とか
      - repository パターン
        - interface として宣言する
        - モックを使ったテストなどがしやすいように
