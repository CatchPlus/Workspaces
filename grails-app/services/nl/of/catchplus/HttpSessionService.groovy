package nl.of.catchplus

import javax.servlet.http.HttpSession
import groovy.sql.Sql
import org.springframework.web.context.request.RequestContextHolder

class HttpSessionService
{

    static scope = "singleton"

    public def timeEx = 0
    public def timePer = 0

    def dataSource

    private Sql sql


    public Sql getSql()
    {
        if (!this.sql)
            sql = new Sql(dataSource)
        return this.sql
    }

    public void test()
    {

        RequestContextHolder.resetRequestAttributes()
    }

    public void addTimeEx(def addTime)
    {

        this.timeEx = this.timeEx + addTime
        //  println this.timeEx
    }

    public void addTimePer(def addTime)
    {

        this.timePer = this.timePer + addTime
        println this.timePer
    }

    // method called upon session creation
    def sessionCreated(HttpSession session)
    {
        log.info("Session created: " + session.id)

        //   println("Session created: "+session.id)
    }

    //method called upon session destruction
    def sessionDestroyed(HttpSession session)
    {
        log.info("Session destroyed: " + session.id)
        //println("Session destroyed: "+session.id)
    }
}
