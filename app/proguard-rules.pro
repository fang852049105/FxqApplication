# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Fangxq/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#基线包使用，生成mapping.txt
#-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix

-keepattributes Signature,*Annotation*,EnclosingMethod

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-keepclassmembers public class * extends android.view.View {
void set*(***);
*** get*();
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keep class * implements java.io.Serializable{*;}

-keepclassmembers class **.R$* {
public static <fields>;
}


# 保持哪些类不被混淆
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#如果引用了v4或者v7包
-dontwarn android.support.**

-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}


#保护注解
-keepattributes *Annotation*

-keep class com.tencent.matrix.trace.core.$ { *; }
#防止inline
-dontoptimize