plugins {
    id("java")
}

group = "br.com.dio.board"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:10.14.0")
    implementation("org.flywaydb:flyway-mysql:10.14.0")
    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("org.projectlombok:lombok:1.18.34")
    //
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}
