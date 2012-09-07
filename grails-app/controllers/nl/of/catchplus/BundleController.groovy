package nl.of.catchplus

class BundleController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    def gormService

    def scaffold = Bundle

    def test=
    {

        BaseContent c=BaseContent.list().toArray()[8]



        Bundle b= Bundle.get(27)

        //b.addToContent(c)

        b.addToUnconfirmedBaseContent(c)

        System.out.println(b.content);
        System.out.println(b.unconfirmedBaseContent);

        System.out.println('X');
        b.save(flush:true)

        b.refresh()

        System.out.println(b.content);
        System.out.println(b.unconfirmedBaseContent);


        System.out.println('Y');

    }

    def addBaseContent =
    {
        def bundleInstance = Bundle.get(params.id)

        def baseContentInstance = BaseContent.get(params.baseContentID)

        def returnObject = params.returnObjectID == params.id ? bundleInstance : baseContentInstance

        bundleInstance.addContent(baseContentInstance)

        flash.message = "${message(code: 'bundle.content.added', args: [params.id,params.baseContentID])}"

        render(view:'/show', model: [domainInstance: returnObject])




    }

    def moveBaseContent =
    {
        def bundleInstance = Bundle.get(params.id)
        def contentInstance = BaseContent.get(params.contentID)

        def returnObject = params.returnObjectID == params.id ? bundleInstance : contentInstance

        flash.message = "${message(code: 'bundle.content.moved', args: [params.contentID,contentInstance.ownerBundle.id,params.id])}"

        gormService.moveBaseContent(contentInstance,bundleInstance)

        render(view:'/show', model: [domainInstance: returnObject])
    }

    def removeBaseContent =
    {

        def bundleInstance = Bundle.get(params.id)
        def contentInstance = BaseContent.get(params.contentID)

        def returnObject = params.returnObjectID == params.id ? bundleInstance : contentInstance

        bundleInstance.removeFromContent(contentInstance)
        bundleInstance.removeFromUnconfirmedBaseContent(contentInstance)

        ShiroPermission shiroPermission = ShiroPermission.findByPermission("bundle:removebasecontent:" + contentInstance.id)
        shiroPermission.delete()

        flash.message = "${message(code: 'bundle.content.removed', args: [params.id,params.contentID])}"

        render(view:'/show', model: [domainInstance: returnObject])




    }



    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bundleInstanceList: Bundle.list(params), bundleInstanceTotal: Bundle.count()]
    }

    def create = {
        def bundleInstance = new Bundle()
        bundleInstance.properties = params
        return [bundleInstance: bundleInstance]
    }

    def save = {
        def bundleInstance = new Bundle(params)
        if (bundleInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'bundle.label', default: 'Bundle'), bundleInstance.id])}"
            redirect(action: "show", id: bundleInstance.id)
        }
        else
        {
            render(view: "create", model: [bundleInstance: bundleInstance])
        }
    }

    def show = {
        def bundleInstance = Bundle.get(params.id)
        if (!bundleInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [bundleInstance: bundleInstance]
        }
    }

    def edit = {
        def bundleInstance = Bundle.get(params.id)
        if (!bundleInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [bundleInstance: bundleInstance]
        }
    }

    def update = {
        def bundleInstance = Bundle.get(params.id)
        if (bundleInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (bundleInstance.version > version)
                {

                    bundleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bundle.label', default: 'Bundle')] as Object[], "Another user has updated this Bundle while you were editing")
                    render(view: "edit", model: [bundleInstance: bundleInstance])
                    return
                }
            }
            bundleInstance.properties = params
            if (!bundleInstance.hasErrors() && bundleInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bundle.label', default: 'Bundle'), bundleInstance.id])}"
                redirect(action: "show", id: bundleInstance.id)
            }
            else
            {
                render(view: "edit", model: [bundleInstance: bundleInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def bundleInstance = Bundle.get(params.id)
        if (bundleInstance)
        {
            try
            {
                bundleInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bundle.label', default: 'Bundle'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
