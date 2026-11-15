# Private messages module

## Local database configuration

The application reads database settings from `%USERPROFILE%\.blogger\database.properties` (or matching Java system properties). Copy `database.properties.example` to that location and replace `CHANGE_ME` with the MySQL password for the `site` database. Keep this local file out of source control.

Hibernate's existing `hbm2ddl.auto=update` setting creates the `chat_messages` table and its indexes when the application starts. The entity includes foreign keys to the existing `userinfo` table.

## Open chat

Sign in, then select **Private Messages** from the user home page or Admin Dashboard. Search for a registered user to start a conversation. Text and JPG/JPEG/PNG/WEBP images up to 5 MB are supported. Messages refresh every 3.5 seconds using authenticated AJAX polling.

Chat images are stored outside the web application under `%USERPROFILE%\.blogger\chat-images`. The browser can retrieve an image only through the authenticated `/chat/image/{messageId}` endpoint, which checks that the current account participated in that message.
