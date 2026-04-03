# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Socket.io
-keep class io.socket.** { *; }
-dontwarn io.socket.**

# Gson
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class com.drawling.app.**.dto.** { *; }

# Hilt
-dontwarn dagger.hilt.**

# Firebase
-keep class com.google.firebase.** { *; }
