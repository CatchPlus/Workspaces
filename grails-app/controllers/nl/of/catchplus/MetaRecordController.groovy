package nl.of.catchplus


class MetaRecordController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    def scaffold = MetaRecord

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        [metaRecordInstanceList: MetaRecord.list(params), metaRecordInstanceTotal: MetaRecord.count()]
    }

    def create = {
        def metaRecordInstance = new MetaRecord()
        metaRecordInstance.properties = params
        return [metaRecordInstance: metaRecordInstance]
    }

    def save = {
        def metaRecordInstance = new MetaRecord(params)

        if (metaRecordInstance instanceof nl.of.catchplus.BaseRepository)
        {
            metaRecordInstance.owner = nl.of.catchplus.ShiroUser.read(org.apache.shiro.SecurityUtils.subject.principal.id).institution
        }

        if (metaRecordInstance.validate())
        {

            if (metaRecordInstance instanceof nl.of.catchplus.BaseRepository || metaRecordInstance instanceof nl.of.catchplus.WorkSpace)
            {
                metaRecordInstance.insert()
                *//*testService.createOwnerPermission(metaRecordInstance)
                testService.createRolePermission(metaRecordInstance)*//*
            }
            else
                metaRecordInstance.save(flush: true)



            flash.message = "${message(code: 'default.created.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), metaRecordInstance.id])}"
            redirect(action: "show", id: metaRecordInstance.id)
        }
        else
        {
            render(view: "create", model: [metaRecordInstance: metaRecordInstance])
        }
    }

    def show = {
        def metaRecordInstance = MetaRecord.read(params.id)
        if (!metaRecordInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [metaRecordInstance: metaRecordInstance]
        }
    }

    def edit = {
        def metaRecordInstance = MetaRecord.read(params.id)
        if (!metaRecordInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [metaRecordInstance: metaRecordInstance]
        }
    }

    def update = {
        def metaRecordInstance = MetaRecord.get(params.id)
        if (metaRecordInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (metaRecordInstance.version > version)
                {

                    metaRecordInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'metaRecord.label', default: 'MetaRecord')] as Object[], "Another user has updated this MetaRecord while you were editing")
                    render(view: "edit", model: [metaRecordInstance: metaRecordInstance])
                    return
                }
            }
            metaRecordInstance.properties = params
            if (!metaRecordInstance.hasErrors() && metaRecordInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), metaRecordInstance.id])}"
                redirect(action: "show", id: metaRecordInstance.id)
            }
            else
            {
                render(view: "edit", model: [metaRecordInstance: metaRecordInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def metaRecordInstance = MetaRecord.get(params.id)
        if (metaRecordInstance)
        {
            try
            {
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"

                metaRecordInstance.myDelete()

                if (metaRecordInstance instanceof nl.of.catchplus.BaseRepository || nl.of.catchplus.WorkSpace)
                    redirect(action: "show", id: metaRecordInstance.id)
                else
                    redirect(action: "list")

                *//*if(MetaRecord == 'StoredFile')
                {
                    metaRecordInstance.content.storedFile=null
                }
                else if(MetaRecord == nl.of.catchplus.WorkSpace.class)
                {
                    metaRecordInstance.myDelete()


                    redirect(action: "list" ,controller:"workSpace")

                }
                else if(MetaRecord == nl.of.catchplus.Collection.class)
                {
                    metaRecordInstance.myDelete()

                    redirect(action: "show" ,controller:"workSpace",id:metaRecordInstance.workSpace.id)

                }
                else if(MetaRecord == nl.of.catchplus.Bundle.class)
                {


                    metaRecordInstance.collection.each {collection->
                        collection.removeFromBundle(metaRecordInstance)
                    }
                    metaRecordInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"collection",id:metaRecordInstance.ownerCollection.id)

                }
                else if(MetaRecord == nl.of.catchplus.BaseContent.class)
                {
                    System.out.println('b');
                    metaRecordInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"bundle",id:metaRecordInstance.bundle.id)
                }
                else if(MetaRecord == nl.of.catchplus.StoredFile.class)
                {
                    metaRecordInstance.content.storedFile=null
                    metaRecordInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"content",id:metaRecordInstance.content.id)
                }
                else
                {
                    metaRecordInstance.delete(flush: true)
                    redirect(action: "list")
                }*//*

            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                e.printStackTrace()
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'metaRecord.label', default: 'MetaRecord'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
