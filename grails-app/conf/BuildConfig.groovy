grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"



grails.server.port.http=8080


grails.plugin.cloudfoundry.username = 'michael@deontwikkelfabriek.nl'
grails.plugin.cloudfoundry.password = 'tyball'
grails.plugin.cloudfoundry.target = 'api.deontwikkelfabriek.cloudfoundry.me'

//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()


        mavenRepo "http://mavensync.zkoss.org/maven2/"
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    /*dependencies {
              // there are three theme 'breeze', 'sapphire', 'silvertail'
            def zkThemeVersion="6.0.0"
        def zkTheme="silvertail"
        runtime "org.zkoss.theme:${zkTheme}:${zkThemeVersion}"
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
    }*/
}
