dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
    dbunitXmlType = "flat" // dbunit-operator data file type: 'flat' or 'structured'
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        /*dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:hsqldb:file:devDB;shutdown=true"
        }*/
        dataSource {
            url = "jdbc:postgresql://localhost:5432/catchplus"
            driverClassName = "org.postgresql.Driver"
            /*dbCreate = "create-drop"*/
            dbCreate = "update"
            username = "postgres"
            password = "pwd4postgres"

            //initialData = "data/init/data-a.xml, data/init/data-b.xml"  // 1-n dbunit-operator Flat-XML or XML comma-separated data files (See http://dbunit.sourceforge.net/components.html)
            initialData = "data/init/data-a.xml"  // 1-n dbunit-operator Flat-XML or XML comma-separated data files (See http://dbunit.sourceforge.net/components.html)
            //initialData = "/Users/Mike/data.xml" // You can use absolute paths
            initialOperation = "CLEAN_INSERT" // dbunit-operator operation

        }
        /*dataSource {
            url = "jdbc:mysql://localhost:3306/catchplus"
            dbCreate = "create-drop"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = ""
        }*/
    }
    test {
        dataSource {
            jndiName = "java:comp/env/jdbc/CatchplusDS"
            dbCreate = "update" // one of 'create', 'create-drop','update'
            /* dialect= org.hibernate.dialect.MySQLInnoDBDialect
          driverClassName = "com.mysql.jdbc.Driver"
          username = "n/a"
          password = "n/a"
          url = "n/a"
          dbCreate = "create-drop"*/
        }
    }
    production {
        dataSource {
            jndiName = "java:comp/env/jdbc/CatchplusDS"
            dbCreate = "update" // one of 'create', 'create-drop','update'
        }

    }
}
