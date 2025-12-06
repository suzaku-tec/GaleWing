# GaleWing

GaleWingは、Webサイトの情報を収集・管理するためのアプリケーションです。

## 機能

*   **フィード管理:** RSS/Atomフィードを登録し、最新の情報を収集します。
*   **コンテンツ分析:** 収集したコンテンツのタグ情報を分析し、関連性の高い情報を抽出します。
*   **カテゴリ分類:** 収集した情報をカテゴリごとに分類・管理します。

## プロジェクト構成

このプロジェクトは、一般的なJavaのWebアプリケーションの構成に基づいています。

*   **`build.gradle`**: プロジェクトのビルド設定ファイルです。
*   **`src/main/java`**: Javaのソースコードが含まれています。
    *   **`com.galewings.controller`**: HTTPリクエストを処理するコントローラークラスが含まれています。
    *   **`com.galewings.service`**: ビジネスロジックを実装するサービスクラスが含まれています。
    *   **`com.galewings.repository`**: データベースとのやり取りを行うリポジトリクラスが含まれています。
    *   **`com.galewings.dto`**: データ転送オブジェクト（DTO）が含まれています。
    *   **`com.galewings.entity`**: データベースのテーブルに対応するエンティティクラスが含まれています。
*   **`src/main/resources`**: SQLファイルや設定ファイルなどのリソースファイルが含まれています。
*   **`src/main/javascript`**: フロントエンドのJavaScriptコードが含まれています。
*   **`src/test`**: 単体テストのコードが含まれています。

# License

This project is licensed under the MIT License, see the LICENSE.txt file for details
