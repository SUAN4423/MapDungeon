package main

import (
	"bytes"
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"net/http"
	"os"
	"path"
	"path/filepath"
	"strings"

	"github.com/joho/godotenv"
)

var (
	method = flag.String("method", "GET", "http method")
	url    = flag.String("url", "http://localhost:8080", "url")
	body   = flag.String("body", "", "request body")
)

func main() {
	abs, _ := filepath.Abs(".")
	if err := godotenv.Load(path.Join(abs, "cmd/test_client/.env")); err != nil {
		panic(fmt.Errorf("failed to load .env: %w", err))
	}
	flag.Parse()

	mustGetEnv := func(key string) string {
		val, ok := os.LookupEnv(key)
		if !ok {
			panic(fmt.Errorf("env %s is not set", key))
		}
		return val
	}
	ac := NewAuthoriezedClient(&clientConfig{
		audience:     mustGetEnv("AUTH0_AUDIENCE"),
		clientID:     mustGetEnv("AUTH0_TEST_APP_CLIENT_ID"),
		clientSecret: mustGetEnv("AUTH0_TEST_APP_CLIENT_SECRET"),
		auth0Domain:  mustGetEnv("AUTH0_DOMAIN"),
	})

	req, err := http.NewRequest(*method, *url, strings.NewReader(*body))
	req.Header.Set("Authorization", "Bearer "+ac.token.AccessToken)
	if err != nil {
		panic(fmt.Errorf("failed to create request: %w", err))
	}
	res, err := new(http.Client).Do(req)
	if err != nil {
		panic(fmt.Errorf("failed to get response: %w", err))
	}
	defer res.Body.Close()
	resBody, _ := io.ReadAll(res.Body)
	fmt.Printf("status: %v", res.StatusCode)
	fmt.Println("body:")
	fmt.Println(string(resBody))
}

type tokenInfo struct {
	AccessToken string `json:"access_token"`
	TokenType   string `json:"token_type"`
}

type authorizedClient struct {
	token tokenInfo
}

type clientConfig struct {
	audience     string
	clientID     string
	clientSecret string
	auth0Domain  string
}

func NewAuthoriezedClient(conf *clientConfig) *authorizedClient {
	if err := godotenv.Load(".env"); err != nil {
		panic(err)
	}

	var res tokenInfo
	{
		req := map[string]string{
			"audience":      conf.audience,
			"client_id":     conf.clientID,
			"client_secret": conf.clientSecret,
			"grant_type":    "client_credentials",
		}

		if err := postAndReceiveJSON("https://"+path.Join(conf.auth0Domain, "oauth/token"), req, &res); err != nil {
			panic(fmt.Errorf("post request to get access token was failed: %v", err))
		}
	}
	return &authorizedClient{token: res}
}

func postAndReceiveJSON(url string, reqJSON interface{}, resJSON interface{}) error {

	payload, err := json.Marshal(reqJSON)
	if err != nil {
		return fmt.Errorf("failed to marshal json: %w", err)
	}
	res, err := http.Post(url, "application/json", bytes.NewBuffer(payload))
	if err != nil {
		return fmt.Errorf("failed to post: %w", err)
	}
	defer res.Body.Close()
	body, _ := io.ReadAll(res.Body)
	if res.StatusCode != http.StatusOK {
		return fmt.Errorf(
			`status: %v
body: %v`, res.Status, string(body))
	}

	if err := json.Unmarshal(body, resJSON); err != nil {
		return fmt.Errorf("failed to unmarshal responce: %w", err)
	}
	return nil
}
