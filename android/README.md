# Map Dungeon

## How to Setup for Development

1. Get Google Map API Key following [this site](https://developers.google.com/maps/documentation/android-sdk/start?hl=ja)

2. Add below sentence to  `local.properties` file in your android project root.

```
MAPS_API_KEY=
```

3. Add app/src/main/res/values/auth0.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="com_auth0_domain">AUTH0_DOMAIN</string>
    <string name="com_auth0_client_id">AUTH0_CLIENT_ID</string>
</resources>
```

4. Run and Debug!
