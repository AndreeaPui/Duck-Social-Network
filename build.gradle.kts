import java.util.Locale

plugins {
    id("application")
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val os = System.getProperty("os.name").lowercase(Locale.getDefault())
val platform = when {
    os.contains("win") -> "win"
    os.contains("mac") -> "mac"
    else -> "linux"
}

dependencies {
    // JavaFX (IMPORTANT)
    implementation("org.openjfx:javafx-base:21:$platform")
    implementation("org.openjfx:javafx-controls:21:$platform")
    implementation("org.openjfx:javafx-fxml:21:$platform")
    implementation("org.openjfx:javafx-graphics:21:$platform")

    runtimeOnly("org.openjfx:javafx-base:21:$platform")
    runtimeOnly("org.openjfx:javafx-controls:21:$platform")
    runtimeOnly("org.openjfx:javafx-fxml:21:$platform")
    runtimeOnly("org.openjfx:javafx-graphics:21:$platform")

    // Postgres
    implementation("org.postgresql:postgresql:42.7.8")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("org.example.Main")  // pune clasa ta reală!
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
