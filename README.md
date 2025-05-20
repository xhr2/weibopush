# WeiboMonitorApp

## 功能简介
- 监控微博博主（UID: 5997609176）最新博文
- APP页面为列表，显示博主头像、名称、博文内容
- 点击博文可跳转微博原文
- 新博文自动推送通知

## 编译方法
1. 用 Android Studio 打开本项目，点击 Build > Build APK(s)
2. 或推送到 GitHub，自动用 GitHub Actions 编译 APK

## GitHub Actions 自动打包
- 项目自带 `.github/workflows/android.yml`，推送后自动编译 APK 并生成下载链接

## 权限说明
- 需要联网权限
- 需要通知权限

## 依赖
- Kotlin
- OkHttp
- WorkManager

---
如需自定义监控UID或功能，请修改 `WeiboMonitorWorker.kt` 中的相关代码。 