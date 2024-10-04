import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
//    alias(libs.plugins.hilt) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}