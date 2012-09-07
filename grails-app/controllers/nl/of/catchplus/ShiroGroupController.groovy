package nl.of.catchplus


class ShiroGroupController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    def scaffold = ShiroGroup

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        [shiroGroupInstanceList: ShiroGroup.list(params), shiroGroupInstanceTotal: ShiroGroup.count()]
    }

    def create = {
        def shiroGroupInstance = new ShiroGroup()
        shiroGroupInstance.properties = params
        return [shiroGroupInstance: shiroGroupInstance]
    }

    def save = {
        def shiroGroupInstance = new ShiroGroup(params)

        if (shiroGroupInstance instanceof nl.of.catchplus.BaseRepository)
        {
            shiroGroupInstance.owner = nl.of.catchplus.ShiroUser.findByUsername(org.apache.shiro.SecurityUtils.subject.principal.toString()).institution
        }

        if (shiroGroupInstance.save(flush: true))
        {
            if (shiroGroupInstance instanceof nl.of.catchplus.BaseRepository)
            {
                testService.createOwnerPermission(shiroGroupInstance)
                testService.createRolePermission(shiroGroupInstance)
            }
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), shiroGroupInstance.id])}"
            redirect(action: "show", id: shiroGroupInstance.id)
        }
        else
        {
            render(view: "create", model: [shiroGroupInstance: shiroGroupInstance])
        }
    }

    def show = {
        def shiroGroupInstance = ShiroGroup.read(params.id)
        if (!shiroGroupInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [shiroGroupInstance: shiroGroupInstance]
        }
    }

    def edit = {
        def shiroGroupInstance = ShiroGroup.read(params.id)
        if (!shiroGroupInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [shiroGroupInstance: shiroGroupInstance]
        }
    }

    def update = {
        def shiroGroupInstance = ShiroGroup.get(params.id)
        if (shiroGroupInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (shiroGroupInstance.version > version)
                {

                    shiroGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'shiroGroup.label', default: 'ShiroGroup')] as Object[], "Another user has updated this ShiroGroup while you were editing")
                    render(view: "edit", model: [shiroGroupInstance: shiroGroupInstance])
                    return
                }
            }
            shiroGroupInstance.properties = params
            if (!shiroGroupInstance.hasErrors() && shiroGroupInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), shiroGroupInstance.id])}"
                redirect(action: "show", id: shiroGroupInstance.id)
            }
            else
            {
                render(view: "edit", model: [shiroGroupInstance: shiroGroupInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def shiroGroupInstance = ShiroGroup.get(params.id)
        if (shiroGroupInstance)
        {
            try
            {
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"


                if (ShiroGroup == 'StoredFile')
                {
                    shiroGroupInstance.content.storedFile = null
                }
                else if (ShiroGroup == nl.of.catchplus.WorkSpace.class)
                {

                    def aArray = shiroGroupInstance.collection.toArray()

                    for (int i = 0; i < aArray.length; i++)
                    {
                        Collection collection = aArray[i]
                        shiroGroupInstance.removeFromCollection(collection)
                        collection.myDelete()
                    }
                    shiroGroupInstance.delete()

                    redirect(action: "list", controller: "workSpace")

                }
                else if (ShiroGroup == nl.of.catchplus.Collection.class)
                {
                    shiroGroupInstance.myDelete()
                    *//*def aArray=shiroGroupInstance.bundle.toArray()

                    for(int i=0;i<aArray.length;i++)
                    {
                        Bundle bundle=aArray[i]

                        bundle.removeFromCollection(shiroGroupInstance)
                        if(bundle.ownerCollection==shiroGroupInstance)
                        {
                            bundle.delete(flush:true)
                        }

                    }


                    shiroGroupInstance.refresh()
                    shiroGroupInstance.delete(flush:true)
*//*
                    redirect(action: "show", controller: "workSpace", id: shiroGroupInstance.workSpace.id)

                }
                else if (ShiroGroup == nl.of.catchplus.Bundle.class)
                {


                    shiroGroupInstance.collection.each {collection ->
                        collection.removeFromBundle(shiroGroupInstance)
                    }
                    shiroGroupInstance.delete(flush: true)
                    redirect(action: "show", controller: "collection", id: shiroGroupInstance.ownerCollection.id)

                }
                else if (ShiroGroup == nl.of.catchplus.BaseContent.class)
                {
                    System.out.println('b');
                    shiroGroupInstance.delete(flush: true)
                    redirect(action: "show", controller: "bundle", id: shiroGroupInstance.bundle.id)
                }
                else if (ShiroGroup == nl.of.catchplus.StoredFile.class)
                {
                    shiroGroupInstance.content.storedFile = null
                    shiroGroupInstance.delete(flush: true)
                    redirect(action: "show", controller: "content", id: shiroGroupInstance.content.id)
                }
                else
                {
                    shiroGroupInstance.delete(flush: true)
                    redirect(action: "list")
                }

            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                e.printStackTrace()
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroGroup.label', default: 'ShiroGroup'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
