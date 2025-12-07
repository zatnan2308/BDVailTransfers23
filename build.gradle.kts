// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    // ВАЖНО: здесь теперь Kotlin 1.9.23, чтобы совпадал с Compose 1.5.11
    kotlin("android") version "1.9.23" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
