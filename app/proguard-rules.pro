# Proguard rules for Modern Termux:MOTD
-dontobfuscate

-keepattributes SourceFile,LineNumberTable

# Preserve model data classes for JSON or reflection
-keep class com.termux.motd.domain.** { *; }
