package nl.of.catchplus

import org.zkoss.zk.ui.Executions


class BlaService
{

    static transactional = true

   // static scope = 'singleton'

    HashSet ss = new HashSet()

    def serviceMethod(String sessionID)
    {
        ss.add(new String(sessionID + " :  " + ss.size()))

        System.out.println(ss.size());
        ss.each {s ->
            System.out.println(s);
        }


    }

    def registerComposer(def plotComposer)
    {


        System.out.println('registerComposer');
        ss.add(plotComposer)

    }

    def outComposers()
    {
        System.out.println('START outComposers');
        ss.each {s ->
            System.out.println(s);
        }

    }

    def broadCastHi()
    {
        System.out.println('START broadCastHi');
        ss.each {s ->
            System.out.println("START broadCastHi TO: " + s);
            //  System.out.println(s.sessionID);
            try
            {
                /*def d=s.headwindow.getDesktop()
                Executions.activate(d,1000);
                println 'welll'
                s.test.label = 'RECEIVED MESSAGE COUNTER: ' + Math.random()

                Executions.deactivate(d);*/
                
                s.sayHi()
            }
            catch (Exception e)
            {
                println "ERORRRRRR"
                e.printStackTrace()
            }
            System.out.println("END broadCastHi TO: " + s);
        }


    }


}
