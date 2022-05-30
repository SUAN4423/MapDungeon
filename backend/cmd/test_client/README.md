# test_client

auth0 のテスト用クライアント.
golang からサーバーのテストが簡単にできます.

.env を.env.sample を参考に用意

```bash
go run cmd/test_client/main.go --method "GET" --url "http://localhost:8080/" --body ""
```
