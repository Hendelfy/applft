Start app with cmd line:
- image build -t app .
- docker-compose up

Usage: http://localhost:8080/swagger-ui/index.html
All requests are secured with authorization token:
- header: Authorization
- token: "dcb20f8a-5657-4f1b-9f7f-ce65739b359e", "93f39e2f-80de-4033-99ee-249d92736a25"