package nl.of.catchplus

class InstitutionController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def scaffold = Institution

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [institutionInstanceList: Institution.list(params), institutionInstanceTotal: Institution.count()]
    }

    def create = {
        def institutionInstance = new Institution()
        institutionInstance.properties = params
        return [institutionInstance: institutionInstance]
    }

    def save = {
        def institutionInstance = new Institution(params)
        if (institutionInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'institution.label', default: 'Institution'), institutionInstance.id])}"
            redirect(action: "show", id: institutionInstance.id)
        }
        else
        {
            render(view: "create", model: [institutionInstance: institutionInstance])
        }
    }

    def show = {
        def institutionInstance = Institution.get(params.id)
        if (!institutionInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [institutionInstance: institutionInstance]
        }
    }

    def edit = {
        def institutionInstance = Institution.get(params.id)
        if (!institutionInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [institutionInstance: institutionInstance]
        }
    }

    def update = {
        def institutionInstance = Institution.get(params.id)
        if (institutionInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (institutionInstance.version > version)
                {

                    institutionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'institution.label', default: 'Institution')] as Object[], "Another user has updated this Institution while you were editing")
                    render(view: "edit", model: [institutionInstance: institutionInstance])
                    return
                }
            }
            institutionInstance.properties = params
            if (!institutionInstance.hasErrors() && institutionInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'institution.label', default: 'Institution'), institutionInstance.id])}"
                redirect(action: "show", id: institutionInstance.id)
            }
            else
            {
                render(view: "edit", model: [institutionInstance: institutionInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def institutionInstance = Institution.get(params.id)
        if (institutionInstance)
        {
            try
            {
                institutionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'institution.label', default: 'Institution'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
