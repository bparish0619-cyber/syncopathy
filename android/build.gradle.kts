allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
subprojects {
    afterEvaluate {
        val android = extensions.findByName("android")
        if (android != null) {
            (android as? com.android.build.gradle.BaseExtension)?.let { extension ->
                if (extension.namespace == null) {
                    extension.namespace = project.group.toString()
                }
            }
        }
    }
}
