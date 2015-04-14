grails.project.work.dir = 'target'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    plugins {
        build ':release:3.1.1', ':rest-client-builder:2.1.3', {
            export = false
        }

        compile (":platform-core:1.0.0") {
            excludes "slf4j-api"
        }

        runtime(":jquery:1.11.1")
        runtime(":resources:1.2.14")
    }
}
