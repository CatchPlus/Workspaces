package nl.of.catchplus


import grails.util.GrailsUtil

class LoggingFilters
{
    static filters = {

        def debug=false

        all(controller: "*", action: "*") {
            before = {
                if(debug || GrailsUtil.environment!='development')
                {
                    log.info "${session.id} PARAMETERS ${params.inspect()}"
                }
            }
            after = { model ->
                if(debug || GrailsUtil.environment!='development')
                {
                    log.info "${session.id} MODEL ${model?.inspect()}"
                    request.currentTime = System.currentTimeMillis()
                }
            }
            afterView = {
                if(debug || GrailsUtil.environment!='development' && session?.id && request?.currentTime)
                    log.info "${session.id} VIEWTIME ${System.currentTimeMillis()-request.currentTime}ms"
            }
        }
    }


}
