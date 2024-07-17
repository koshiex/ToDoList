import org.gradle.api.JavaVersion

object AndroidConst {
    const val NAMESPACE = "com.kionavani.todotask"
    const val COMPILE_SKD = 34
    const val MIN_SKD = 26
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_1_8
    const val KOTLIN_JVM_TARGET = "1.8"
    const val CLIENT_ID = "YANDEX_CLIENT_ID"
    const val BASE_URL = "BASE_URL"
    const val OAUTH_TOKEN = "OAUTH_TOKEN"
}