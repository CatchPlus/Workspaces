package nl.of.catchplus

import grails.plugin.mail.MailService

class EmailService  {

    MailService mailService

    def grailsApplication

    def shiroService

    static transactional = true

    public void sendNewUserMail(Account  account) {

        String activationUrl = grailsApplication.config.grails.serverURL + "/account/activate/${account.activationCode}"

        def g= CatchplusUtils.getApplicationTagLib()




        mailService.sendMail {
            to "${account.username}"
            subject g.message(code:'email.activate.title',locale: new Locale(account.language))
            body(view: "/email/activate_${account.language}", model: [account: account,activationUrl:activationUrl])
        }


    }

    public void sendRequestNewPassword(Account  account) {

        def g= CatchplusUtils.getApplicationTagLib()

        String activationUrl = grailsApplication.config.grails.serverURL + "/account/password/${account.passwordCode}"

        mailService.sendMail {
            to "${account.username}"
            subject g.message(code:'email.forgotpassword.title',locale: new Locale(account.language))
            body(view: "/email/forgotpassword_${account.language}", model: [account: account,activationUrl:activationUrl])
        }


    }

}
