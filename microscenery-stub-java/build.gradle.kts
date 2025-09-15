import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("com.google.protobuf") version "0.8.19"
}

group = "me.jan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.scijava.org/content/groups/public")
}


dependencies {
    if(System.getProperty("os.name").lowercase().contains("mac")) {
        implementation ("com.google.protobuf:protobuf-java:4.32.0") //current macOS homebrew version
    } else {
        implementation ("com.google.protobuf:protobuf-java:3.25.3") // I think this one was still compatible with micromanager
    }
    implementation("com.google.protobuf:protobuf-java-util:3.25.3")
}

java.sourceSets["main"].java {
    srcDir("build/generated/source/proto/main/java")
}

java {
    //sourceCompatibility = JavaVersion.VERSION_1_8
    //targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets["main"].proto {
    srcDir("../proto")
}
protobuf {
    var protocPath = providers.gradleProperty("protoc.path").orNull
    if(protocPath == null) {
        logger.warn("protoc path property (protoc.path) not found, falling back to default.")
        protocPath = projectDir.resolve("protoc/bin/protoc.exe").absolutePath
    }

    logger.info("Using protoc from $protocPath")
    this.protobuf.protoc {
        this.path = protocPath
    }
}

tasks{
    jar{
        this.archiveBaseName.set("microscenery-stub-java")
    }
}
