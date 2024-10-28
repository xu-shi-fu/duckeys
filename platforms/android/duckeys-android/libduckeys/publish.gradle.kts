apply plugin :"maven-publish"


String localMavenRepoPath = hasProperty ("LOCAL_REPO") ? getProperty("LOCAL_REPO") : "${rootDir}/outputs/android/maven"


publishing {
    repositories {
        maven {
            url localMavenRepoPath
        }
    }
}
