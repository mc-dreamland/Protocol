dependencies {
    api(libs.netty.buffer)
    api(libs.fastutil.int.`object`.maps)
    api(libs.fastutil.`object`.int.maps)
    api(libs.math)
    api(libs.natives)
    api("com.github.caoli5288.igzip","igzip-all","0.2-SNAPSHOT")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "org.cloudburstmc.protocol.common")
    }
}