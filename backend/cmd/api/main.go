package main

import (
	"flag"
	"fmt"
	"net/http"
	"os"
	"path"
	"path/filepath"

	"github.com/Finatext/logger"
	"github.com/SUAN4424/MapDungeon/internal/service"
	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

var (
	port = flag.String("port", "8080", "port to listen")
)

func mustGetEnv(key string) string {
	v, ok := os.LookupEnv(key)
	if !ok {
		panic(fmt.Errorf("env %s must be set", key))
	}
	return v
}

func main() {

	abs, _ := filepath.Abs(".")
	if err := godotenv.Load(path.Join(abs, "cmd/api/.env")); err != nil {
		panic(fmt.Errorf("failed to load .env: %w", err))
	}
	flag.Parse()

	e := echo.New()
	e.Use(middleware.Recover())
	e.Use(middleware.RequestID())
	e.Use(middleware.Logger())

	auth0Domain := mustGetEnv("AUTH0_DOMAIN")
	auth0Audience := mustGetEnv("AUTH0_AUDIENCE")

	e.GET("/", func(c echo.Context) error {
		return c.JSON(http.StatusOK, map[string]interface{}{"message": "Hello, from a private endpoint!"})
	})

	p := e.Group("/private", echo.WrapMiddleware(service.EnsureValidToken(auth0Domain, auth0Audience)))
	{
		p.GET("", func(c echo.Context) error {
			return c.JSON(http.StatusOK, map[string]interface{}{"message": "Hello, from a private endpoint!"})
		})
	}

	logger.Fatalln(e.Start(":" + *port))
}
