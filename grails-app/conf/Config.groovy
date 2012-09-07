import it.openutils.log4j.AlternateSMTPAppender
import org.apache.log4j.DailyRollingFileAppender
import org.apache.log4j.FileAppender

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = [ "classpath:${appName}-config.properties"/*,
                             "classpath:${appName}-config.groovy",
                             "file:${userHome}/.grails/${appName}-config.properties",
                             "file:${userHome}/.grails/${appName}-config.groovy"*/]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.mail.default.from='Workspaces <no-reply@tomcat.deontwikkelfabriek.nl>'


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

//org.grails.jaxrs.provider.name='restlet'

file.vault.location = "/data/vault"

file.upload.max.size="5000000"

//grails.disableCommonsMultipart = true

grails.hibernate.cache.queries=true

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Collection to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

grails.gorm.default.constraints = {
    '*'(nullable: true, blank: false)
}

logDirectory = "../logs"

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://217.21.192.152:8080/${appName}"
    }
    development {
        file.vault.location = "c:\\Temp\\vault"
        //file.vault.location = "e:\\vault"
        grails.serverURL = "http://localhost:8080/${appName}"
        grails {
            mail {
                host = "smtp.gmail.com"
                port = 465
                username = "dobbel01@gmail.com"
                password = "XXX"
                props = ["mail.smtp.auth": "true",
                        "mail.smtp.socketFactory.port": "465",
                        "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                        "mail.smtp.socketFactory.fallback": "false"]
            }
        }
    }
    test {
        grails.serverURL = "http://217.21.192.152:8080/${appName}"
        /*grails {
            mail {
                host = "smtp.gmail.com"
                port = 465
                username = "catchplustest@gmail.com"
                password = "pwd4catchplus"
                props = ["mail.smtp.auth": "true",
                        "mail.smtp.socketFactory.port": "465",
                        "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                        "mail.smtp.socketFactory.fallback": "false"]
            }
        }*/
    }

}

//compass.query.maxClauseCount='4048'

grails.plugins.dynamicController.mixins = [
   'com.burtbeckwith.grails.plugins.appinfo.IndexControllerMixin':       'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.HibernateControllerMixin':   'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.Log4jControllerMixin' :      'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.SpringControllerMixin' :     'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.MemoryControllerMixin' :     'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.PropertiesControllerMixin' : 'com.burtbeckwith.appinfo_test.AdminManageController',
   'com.burtbeckwith.grails.plugins.appinfo.ScopesControllerMixin' :     'com.burtbeckwith.appinfo_test.AdminManageController'
]

auditLog {
  actorClosure = { request, session ->
      //session.accountId
     org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()?.id
  }
}

def fs = File.separator // Local variable.
globalDirs.targetDir = new File("target${fs}").isDirectory() ? "target${fs}" : ''
globalDirs.catalinaBase = System.properties.getProperty('catalina.base')
globalDirs.logDirectory = globalDirs.catalinaBase ? "${globalDirs.catalinaBase}${fs}logs${fs}" : globalDirs.targetDir
globalDirs.workDirectory = globalDirs.catalinaBase ? "${globalDirs.catalinaBase}${fs}work${fs}" : globalDirs.targetDir
globalDirs.searchableIndexDirectory = globalDirs.logDirectory//globalDirs.targetDir//"${globalDirs.workDirectory}SearchableIndex${fs}${appName}${fs}"

log4j = {
    appenders {
        /*  console name:'stdout'*/

        rollingFile name: "stacktrace", file: "${globalDirs.logDirectory}${appName}-stacktrace.log".toString()

 //       null name:'stacktrace'

        //log4j.appender.'errors.File'="${globalDirs.logDirectory}${appName}-stacktrace.log".toString()

        console name: 'stdout', layout: pattern(conversionPattern: '[%t] %-5p %c{2} %x - %m%n')
        // Use if we want to prevent creation of a stacktrace.log file.

        appender new DailyRollingFileAppender(
                name: 'dailyAppender',
                datePattern: "'.'yyyy-MM-dd",  // See the API for all patterns.
                file: "${globalDirs.logDirectory}${appName}.log".toString(),
                threshold: org.apache.log4j.Level.INFO,
                'append': true,
                layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
        )
        appender new DailyRollingFileAppender(
                name: 'dailyErrorAppender',
                datePattern: "'.'yyyy-MM-dd",  // See the API for all patterns.
                file: "${globalDirs.logDirectory}${appName}-error.log".toString(),
                threshold: org.apache.log4j.Level.ERROR,
                'append': true,
                layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
        )

        appender new AlternateSMTPAppender(
                name: 'mailAppender',
                to: 'dobbel01@hotmail.com',
                from: 'error@catchplus.nl',
                SMTPHost: 'localhost',
                threshold: org.apache.log4j.Level.ERROR,
                layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n'),
                subject: "%m"

        )


        /*appender new FileAppender(
                name: 'manoAppender',
                file: "${globalDirs.logDirectory}${appName}-mano.log".toString(),
                threshold: nl.of.manomano.ManoLevel.MANO,
                layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')

        )*/


    }

    //debug 'grails.plugin.searchable'

    info 'grails.app',                 // Logging warnings and higher for all of the app
            'org.codehaus.groovy.grails.web.servlet',        //  controllers
            'org.codehaus.groovy.grails.web.pages',          //  GSP
            'org.codehaus.groovy.grails.web.sitemesh',       //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping'         // URL mapping
    warn 'org.mortbay.log'

    error 'org.codehaus.groovy.grails.commons',    // core classloading
            'org.codehaus.groovy.grails.plugins',       // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',                      // spring framework
            'org.hibernate'




    root {
        switch (environment)
        {
            case 'development':
                // Configure the root logger to output to stdout and appLog appenders.
                root {
                    info 'stdout', 'dailyAppender', 'dailyErrorAppender', 'syslog'/*, 'manoAppender'*/
                    additivity = true
                }
                //debug "org.hibernate.SQL"
                /*     debug 'grails.app.service'
               debug 'grails.app.controller'*/
                break
            case 'test':
                // Configure the root logger to only output to appLog appender.
                root {
                    info 'stdout', 'dailyAppender', 'dailyErrorAppender'/*, 'manoAppender'*/
                    additivity = true
                }
                /*    debug 'grails.app.service'
               debug 'grails.app.controller'*/
                break
            case 'accept':
                // Configure the root logger to only output to appLog appender.
                root {
                    info 'stdout', 'dailyAppender', 'dailyErrorAppender'/*, 'manoAppender'*/
                    additivity = true
                }
                /*    debug 'grails.app.service'
               debug 'grails.app.controller'*/
                break
            case 'production':
                // Configure the root logger to only output to appLog appender.
                root {
                    info 'dailyAppender', 'dailyErrorAppender', 'mailAppender'/*, 'manoAppender'*/
                    additivity = true
                }
                warn 'grails.app.service'
                warn 'grails.app.controller'

      //          log4j.appender.'errors.File'="${globalDirs.logDirectory}/stacktrace.log"

                break
        }


    }
    debug /*manoAppender: 'mano',*/ additivity: false


}


// log4j configuration
/*
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    warn 'org.mortbay.log'
}
*/
