plugins { id("rbt.adapter.inbound.gradle") }

group = "com.github.c128lib.retro-assembler.processors.goattracker.in"

dependencies {
    implementation(project(":processors:goattracker"))
    implementation(project(":shared:gradle"))
}
