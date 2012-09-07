package nl.of.catchplus


class MetaRecordKeyController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    def scaffold = MetaRecordKey

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        [metaRecordKeyInstanceList: MetaRecordKey.list(params), metaRecordKeyInstanceTotal: MetaRecordKey.count()]
    }

    def create = {
        def metaRecordKeyInstance = new MetaRecordKey()
        metaRecordKeyInstance.properties = params
        return [metaRecordKeyInstance: metaRecordKeyInstance]
    }

    def save = {
        def metaRecordKeyInstance = new MetaRecordKey(params)

        if (metaRecordKeyInstance instanceof nl.of.catchplus.BaseRepository)
        {
            metaRecordKeyInstance.owner = nl.of.catchplus.ShiroUser.read(org.apache.shiro.SecurityUtils.subject.principal.id).institution
        }

        if (metaRecordKeyInstance.validate())
        {

            if (metaRecordKeyInstance instanceof nl.of.catchplus.BaseRepository || metaRecordKeyInstance instanceof nl.of.catchplus.WorkSpace)
            {
                metaRecordKeyInstance.insert()
                *//*testService.createOwnerPermission(metaRecordKeyInstance)
                testService.createRolePermission(metaRecordKeyInstance)*//*
            }
            else
                metaRecordKeyInstance.save(flush: true)



            flash.message = "${message(code: 'default.created.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), metaRecordKeyInstance.id])}"
            redirect(action: "show", id: metaRecordKeyInstance.id)
        }
        else
        {
            render(view: "create", model: [metaRecordKeyInstance: metaRecordKeyInstance])
        }
    }

    def show = {
        def metaRecordKeyInstance = MetaRecordKey.read(params.id)
        if (!metaRecordKeyInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [metaRecordKeyInstance: metaRecordKeyInstance]
        }
    }

    def edit = {
        def metaRecordKeyInstance = MetaRecordKey.read(params.id)
        if (!metaRecordKeyInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [metaRecordKeyInstance: metaRecordKeyInstance]
        }
    }

    def update = {
        def metaRecordKeyInstance = MetaRecordKey.get(params.id)
        if (metaRecordKeyInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (metaRecordKeyInstance.version > version)
                {

                    metaRecordKeyInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'metaRecordKey.label', default: 'MetaRecordKey')] as Object[], "Another user has updated this MetaRecordKey while you were editing")
                    render(view: "edit", model: [metaRecordKeyInstance: metaRecordKeyInstance])
                    return
                }
            }
            metaRecordKeyInstance.properties = params
            if (!metaRecordKeyInstance.hasErrors() && metaRecordKeyInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), metaRecordKeyInstance.id])}"
                redirect(action: "show", id: metaRecordKeyInstance.id)
            }
            else
            {
                render(view: "edit", model: [metaRecordKeyInstance: metaRecordKeyInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def metaRecordKeyInstance = MetaRecordKey.get(params.id)
        if (metaRecordKeyInstance)
        {
            try
            {
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"

                metaRecordKeyInstance.myDelete()

                if (metaRecordKeyInstance instanceof nl.of.catchplus.BaseRepository || nl.of.catchplus.WorkSpace)
                    redirect(action: "show", id: metaRecordKeyInstance.id)
                else
                    redirect(action: "list")

                *//*if(MetaRecordKey == 'StoredFile')
                {
                    metaRecordKeyInstance.content.storedFile=null
                }
                else if(MetaRecordKey == nl.of.catchplus.WorkSpace.class)
                {
                    metaRecordKeyInstance.myDelete()


                    redirect(action: "list" ,controller:"workSpace")

                }
                else if(MetaRecordKey == nl.of.catchplus.Collection.class)
                {
                    metaRecordKeyInstance.myDelete()

                    redirect(action: "show" ,controller:"workSpace",id:metaRecordKeyInstance.workSpace.id)

                }
                else if(MetaRecordKey == nl.of.catchplus.Bundle.class)
                {


                    metaRecordKeyInstance.collection.each {collection->
                        collection.removeFromBundle(metaRecordKeyInstance)
                    }
                    metaRecordKeyInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"collection",id:metaRecordKeyInstance.ownerCollection.id)

                }
                else if(MetaRecordKey == nl.of.catchplus.BaseContent.class)
                {
                    System.out.println('b');
                    metaRecordKeyInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"bundle",id:metaRecordKeyInstance.bundle.id)
                }
                else if(MetaRecordKey == nl.of.catchplus.StoredFile.class)
                {
                    metaRecordKeyInstance.content.storedFile=null
                    metaRecordKeyInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"content",id:metaRecordKeyInstance.content.id)
                }
                else
                {
                    metaRecordKeyInstance.delete(flush: true)
                    redirect(action: "list")
                }*//*

            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                e.printStackTrace()
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecordKey.label', default: 'MetaRecordKey'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
