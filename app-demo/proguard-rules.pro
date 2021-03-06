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
-optimizations !code/simplification/cast,!code/allocation/*,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# ADDED
-keep class com.google.zxing.client.android.camera.open.**
#-keep class com.google.zxing.client.android.camera.exposure.**
-keep class com.google.zxing.client.android.common.executor.**

# ADDED
-dontobfuscate
-useuniqueclassmembernames

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-ignorewarnings
 #??????  ???????????????????????????
-dontoptimize
 #?????????
-dontpreverify
 #???????????????????????????
-verbose
#????????????
-keepattributes *Annotation*
#-ignorewarnings

 #????????????????????????
 -keepattributes Signature

#?????? Serializable ????????????
 -keepnames class * implements java.io.Serializable
 #?????? Serializable ??????????????????enum ??????????????????
 -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

#ok http
#-dontwarn com.squareup.**
-keep class com.squareup.**{*;}
#-dontwarn okio.**
-keep class okio.**{*;}

-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#?????????widget
# TODO
#fastjson
#??????????????? fastjson ?????????????????????????????????????????????????????????????????????????????????
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }

-keep class **.R$* {*;}

#keep ?????????
# TODO
-keep public class * implements java.io.Serializable {
    public *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# keep annotated by NotProguard
-keep @com.cody.component.util.NotProguard class * {*;}
-keep class * {
    @com.cody.component.util.NotProguard <fields>;
}
-keepclassmembers class * {
    @com.cody.component.util.NotProguard <methods>;
}
# glide start
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
# glide end