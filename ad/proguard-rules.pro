# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# keep bytedance AD sdk
 -keep class com.bytedance.sdk.openadsdk.** { *; }
 -keep class com.androidquery.callback.** {*;}
 -keep class com.ss.android.crash.** {*;}
 -keep class com.bytedance.sdk.openadsdk.service.TTDownloadProvider
 -keep class com.cootek.domostic.ad.**{*;}
 -dontwarn com.androidquery.**
 -dontwarn com.ss.android.crash**
 -dontwarn com.cootek.feeds.ui.activity.LockScreenActivity

# keep GDT SDK
 -keep class com.qq.e.** {
      public protected *;
  }
 -keep class MTT.ThirdAppInfoNew {
        *;
    }
 -keep class com.tencent.** {
        *;
    }
# keep Baidu AD SDK
-keep class com.baidu.mobads.*.** { *; }