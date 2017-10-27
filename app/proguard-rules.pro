-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }
-keep class suhockii.dev.weather.data.models.** { *; }

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class android.support.v7.widget.SearchView { *; }
-ignorewarnings
