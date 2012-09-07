package nl.of.catchplus

import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.commons.lang.RandomStringUtils


class AccountController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    //"65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5"

    def testService

    def emailService

    def scaffold = Account

    def activate = {

        Account account = Account.findByActivationCode(params.id)
        if (account)
        {
            account.activated = true
            account.passwordCode = account.activationCode
            account.activationCode = null

            //System.out.println('ACTIVATE');
            //System.out.println(account.passwordCode);

            //Account.withTransaction {
            // account.save(flush: true)
            //}
        }
        [account: account]

    }

    def password =
        {
            Account account = Account.findByPasswordCode(params.id)



            if (request.method == 'POST')
            {
                if (!params.password || !params.password2 || params.password == "" || params.password2 == "")
                    flash.message = 'Graag alle velden invullen'
                else if (params.password != params.password2)
                    flash.message = 'Wachtwoord niet gelijk'
                else if (params.password.length() < 6)
                    flash.message = 'Wachtwoord moet ten minste 6 teken bevatten'

                if (flash.message)
                    [account: account]
                else
                {
                    account.password = new Sha256Hash(params.password)
                    account.save()
                    flash.message = 'Wachtwoord is opgeslagen uw kunt zich nu aanmelden'
                    redirect(action: 'login', controller: 'auth')

                }
            }
            if (!account)
            {
                flash.message = 'Geen account gevonden'
                redirect(action: 'login', controller: 'auth')
            }

            [account: account]
        }

    def forgotpassword =
        {


            if (request.method == 'POST')
            {
                if (!params.email || params.email == "")
                    flash.message = 'Graag alle velden invullen'

                if (!flash.message)
                {
                    Account account = Account.findByUsername(params.email)

                    if (account)
                    {
                        if (!account.activated)
                            flash.message = 'Gebruiker is nog niet geactiveerd'
                        else
                        {

                            account.passwordCode = RandomStringUtils.random(5, true, true)
                            //account.save(flush:true)
                            emailService.sendRequestNewPassword(account)
                            flash.message = 'Er is een email verstuurd met instructies  voor het keizen van een nieuw wachtwoord'
                            redirect(action: 'login', controller: 'auth')
                        }
                    }
                    else
                        flash.message = 'Email adres is niet bekend'
                }
            }
        }

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        [accountInstanceList: Account.list(params), accountInstanceTotal: Account.count()]
    }

    def create = {
        def accountInstance = new Account()
        accountInstance.properties = params
        return [accountInstance: accountInstance]
    }

    def save = {
        def accountInstance = new Account(params)

        if (accountInstance instanceof nl.of.catchplus.BaseRepository) {
            accountInstance.owner = nl.of.catchplus.ShiroUser.read(org.apache.shiro.SecurityUtils.subject.principal.id).institution
        }

        if (accountInstance.validate()) {

            if (accountInstance instanceof nl.of.catchplus.BaseRepository || accountInstance instanceof nl.of.catchplus.WorkSpace) {
                accountInstance.insert()
                *//*testService.createOwnerPermission(accountInstance)
                testService.createRolePermission(accountInstance)*//*
            }
            else
                accountInstance.save(flush: true)



            flash.message = "${message(code: 'default.created.message', args: [message(code: 'account.label', default: 'Account'), accountInstance.id])}"
            redirect(action: "show", id: accountInstance.id)
        }
        else {
            render(view: "create", model: [accountInstance: accountInstance])
        }
    }

    def show = {
        def accountInstance = Account.read(params.id)
        if (!accountInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }
        else {
            if (params.tagged == "true") {
                [accountInstance: accountInstance]
            }
            else
                render(view: '/show', model: [domainInstance: accountInstance])

        }
    }

    def view = {
        def accountInstance = Account.read(params.id)
        if (!accountInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }
        else {
            render(view: "/view", model: [domainObject: accountInstance])
        }
    }

    def edit = {
        def accountInstance = Account.read(params.id)
        if (!accountInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [accountInstance: accountInstance]
        }
    }

    def update = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (accountInstance.version > version) {

                    accountInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'account.label', default: 'Account')] as Object[], "Another user has updated this Account while you were editing")
                    render(view: "edit", model: [accountInstance: accountInstance])
                    return
                }
            }
            accountInstance.properties = params
            if (!accountInstance.hasErrors() && accountInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'account.label', default: 'Account'), accountInstance.id])}"
                redirect(action: "show", id: accountInstance.id)
            }
            else {
                render(view: "edit", model: [accountInstance: accountInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }
    }

    def pin = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            session.pin = accountInstance

            flash.message = "${message(code: 'default.pinned.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "show", id: accountInstance.id)
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }

    }

    def unpin = {
        def accountInstance = Account.get(params.id)
        session.pin = null
        flash.message = "${message(code: 'default.unpinned.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
        redirect(action: "show", id: accountInstance.id)


    }

    def delete = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            try {
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"

                accountInstance.myDelete()

                if (accountInstance instanceof nl.of.catchplus.BaseRepository || nl.of.catchplus.WorkSpace)
                    redirect(action: "show", id: accountInstance.id)
                else
                    redirect(action: "list")

                *//*if(Account == 'StoredFile')
                {
                    accountInstance.content.storedFile=null
                }
                else if(Account == nl.of.catchplus.WorkSpace.class)
                {
                    accountInstance.myDelete()


                    redirect(action: "list" ,controller:"workSpace")

                }
                else if(Account == nl.of.catchplus.Collection.class)
                {
                    accountInstance.myDelete()

                    redirect(action: "show" ,controller:"workSpace",id:accountInstance.workSpace.id)

                }
                else if(Account == nl.of.catchplus.Bundle.class)
                {


                    accountInstance.collection.each {collection->
                        collection.removeFromBundle(accountInstance)
                    }
                    accountInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"collection",id:accountInstance.ownerCollection.id)

                }
                else if(Account == nl.of.catchplus.BaseContent.class)
                {
                    System.out.println('b');
                    accountInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"bundle",id:accountInstance.bundle.id)
                }
                else if(Account == nl.of.catchplus.StoredFile.class)
                {
                    accountInstance.content.storedFile=null
                    accountInstance.delete(flush: true)
                    redirect(action: "show" ,controller:"content",id:accountInstance.content.id)
                }
                else
                {
                    accountInstance.delete(flush: true)
                    redirect(action: "list")
                }*//*

            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                e.printStackTrace()
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'account.label', default: 'Account'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
