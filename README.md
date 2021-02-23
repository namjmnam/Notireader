# Notireader

## General info
This project aims to create a simplified auto-replying chatbot using direct reply action on notification. This feature was introduced in Android 7.0 (API level 24) and requires additional setup for versions below.

## Compatibility

Currently been tested on android 6.0.1 with Wear OS by Google installed:
```
https://play.google.com/store/apps/details?id=com.google.android.wearable.app
```
This is because android anything below 7.0 cannot use notification action without the Wear OS app.

## How to use

Currently supports KakaoTalk messenger. Notification must be turned on. By receiving any message containing "/start" will trigger the direct reply and send "Auto Reply" back to the sender.
