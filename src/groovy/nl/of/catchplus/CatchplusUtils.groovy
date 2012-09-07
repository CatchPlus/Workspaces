package nl.of.catchplus

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * Created by IntelliJ IDEA.
 * User: Michaell
 * Date: 18-1-12
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
class CatchplusUtils {

    def grailsApplication


    public static  def getApplicationTagLib() {
        def ctx = ApplicationHolder.getApplication().getMainContext()
        return ctx.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }

    public static int countUpperCase(String _string)
    {
        int caps = 0;
        for (int i = 0; i < _string.length(); i++)
        {
            if (Character.isUpperCase(_string.charAt(i)))
                caps++;
        }
        return caps
    }

    /*public static int  getMaxUploadFileSize()
    {
        Integer.valueOf(grailsApplication.config.file.upload.max.size).intValue()
    }*/

}
