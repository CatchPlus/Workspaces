package nl.of.catchplus

import org.apache.shiro.SecurityUtils

class WorkSpaceController
{

    def testService

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def scaffold =  WorkSpace

    def test =
    {
        System.out.println('A');

        def r=SecurityUtils.getSubject().isPermitted("workspace:addCollection:55554")
        System.out.println(r);

    }

    def addCollection =
    {
        def workSpaceInstance = WorkSpace.get(params.id)

        def collectionInstance = Collection.get(params.collectionID)

        def returnObject = params.returnObjectID == params.id ? workSpaceInstance : collectionInstance

        workSpaceInstance.addCollection(collectionInstance)



        flash.message = "${message(code: 'workspace.collection.added', args: [params.id,params.collectionID])}"

        render(view:'/show', model: [domainInstance: returnObject])




    }



    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [workSpaceInstanceList: WorkSpace.list(params), workSpaceInstanceTotal: WorkSpace.count()]
    }

    def create = {
        def workSpaceInstance = new WorkSpace()
        workSpaceInstance.properties = params
        return [workSpaceInstance: workSpaceInstance]
    }

    def save = {
        def workSpaceInstance = new WorkSpace(params)
        if (workSpaceInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), workSpaceInstance.id])}"
            redirect(action: "show", id: workSpaceInstance.id)
        }
        else
        {
            render(view: "create", model: [workSpaceInstance: workSpaceInstance])
        }
    }

    def show = {
        def workSpaceInstance = WorkSpace.get(params.id)
        if (!workSpaceInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [workSpaceInstance: workSpaceInstance]
        }
    }

    def edit = {
        def workSpaceInstance = WorkSpace.get(params.id)
        if (!workSpaceInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [workSpaceInstance: workSpaceInstance]
        }
    }

    def update = {
        def workSpaceInstance = WorkSpace.get(params.id)
        if (workSpaceInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (workSpaceInstance.version > version)
                {

                    workSpaceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'workSpace.label', default: 'WorkSpace')] as Object[], "Another user has updated this WorkSpace while you were editing")
                    render(view: "edit", model: [workSpaceInstance: workSpaceInstance])
                    return
                }
            }
            workSpaceInstance.properties = params
            if (!workSpaceInstance.hasErrors() && workSpaceInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), workSpaceInstance.id])}"
                redirect(action: "show", id: workSpaceInstance.id)
            }
            else
            {
                render(view: "edit", model: [workSpaceInstance: workSpaceInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def workSpaceInstance = WorkSpace.get(params.id)
        if (workSpaceInstance)
        {
            try
            {
                workSpaceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'workSpace.label', default: 'WorkSpace'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
