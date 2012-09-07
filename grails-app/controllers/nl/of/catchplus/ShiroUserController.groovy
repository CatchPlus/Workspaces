package nl.of.catchplus

import org.apache.shiro.crypto.hash.Sha256Hash


class ShiroUserController {

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def scaffold = ShiroUser

/*
    def activate = {

        ShiroUser shiroUser = ShiroUser.findByActivationCode(params.id)

        if (shiroUser) {
            shiroUser.activated = true

            shiroUser.passwordCode =shiroUser.activationCode
            shiroUser.activationCode = null
            shiroUser.save(flush: true)
            System.out.println('ACTIVATED');
        }
        [shiroUser:shiroUser]

    }
*/




    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [shiroUserInstanceList: ShiroUser.list(params), shiroUserInstanceTotal: ShiroUser.count()]
    }

    def create = {
        def shiroUserInstance = new ShiroUser()
        shiroUserInstance.properties = params
        return [shiroUserInstance: shiroUserInstance]
    }

    def save = {
        def shiroUserInstance = new ShiroUser(params)
        if (shiroUserInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), shiroUserInstance.id])}"
            redirect(action: "show", id: shiroUserInstance.id)
        }
        else
        {
            render(view: "create", model: [shiroUserInstance: shiroUserInstance])
        }
    }

    def show = {
        def shiroUserInstance = ShiroUser.get(params.id)
        if (!shiroUserInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [shiroUserInstance: shiroUserInstance]
        }
    }

    def edit = {
        def shiroUserInstance = ShiroUser.get(params.id)
        if (!shiroUserInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [shiroUserInstance: shiroUserInstance]
        }
    }

    def update = {
        def shiroUserInstance = ShiroUser.get(params.id)
        if (shiroUserInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (shiroUserInstance.version > version)
                {

                    shiroUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'shiroUser.label', default: 'ShiroUser')] as Object[], "Another user has updated this ShiroUser while you were editing")
                    render(view: "edit", model: [shiroUserInstance: shiroUserInstance])
                    return
                }
            }
            shiroUserInstance.properties = params
            if (!shiroUserInstance.hasErrors() && shiroUserInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), shiroUserInstance.id])}"
                redirect(action: "show", id: shiroUserInstance.id)
            }
            else
            {
                render(view: "edit", model: [shiroUserInstance: shiroUserInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def shiroUserInstance = ShiroUser.get(params.id)
        if (shiroUserInstance)
        {
            try
            {
                shiroUserInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'ShiroUser'), params.id])}"
            redirect(action: "list")
        }
    }
*/
}
