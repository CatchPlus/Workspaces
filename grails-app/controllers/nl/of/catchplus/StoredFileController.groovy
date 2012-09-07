package nl.of.catchplus


class StoredFileController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService

    def fileVaultService

    PermissionService permissionService

    def scaffold = StoredFile

    def download = {

        Long id; try
        {id = Long.parseLong(params.id)}
        catch (NumberFormatException e)
        {render(view: '/error404'); return;}

        StoredFile storedFile = StoredFile.read(id)

        if (storedFile)
        {
            if (storedFile.deleted)
                render "file has been deleted"

            else if (permissionService.baseContentRead(storedFile.content))
            {
                response.contentType = "application/octet-stream"
                //response.setHeader("Content-Disposition", "attachment; filename=\"" + ${storedFile.originalFilename} + "\"");
                response.setHeader("Content-disposition", "filename=\"${storedFile.originalFilename}\"")
                response.outputStream << new FileInputStream(fileVaultService.getFileFromVault(storedFile))
            }
            else
                render "access denied"
        }
        else
            render "file not found"

        /*else
        {
            redirect(uri: '/images/geen_foto.jpg')
        }*/
    }

    def render = {

        Long id; try
        {id = Long.parseLong(params.id)}
        catch (NumberFormatException e)
        {render(view: '/error404'); return;}

        StoredFile storedFile = StoredFile.read(id)

        if (storedFile)
        {
            if (storedFile.deleted)
                render "file has been deleted"
            else if (permissionService.baseContentRead(storedFile.content))
            {
                response.contentType = storedFile.contentType
                response.setHeader("Content-disposition", "filename=\"${storedFile.originalFilename}\"")
                response.outputStream << new FileInputStream(fileVaultService.getFileFromVault(storedFile))
            }
            else
                render "access denied"
        }
        else
            render "file not found"

        /*else
        {
            redirect(uri: '/images/geen_foto.jpg')
        }*/
    }

/*def delete = {
    def storedFileInstance = StoredFile.get(params.id)
    if (storedFileInstance)
    {
        try
        {
            storedFileInstance.content.storedFile=null
            storedFileInstance.delete(flush: true)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
            //redirect(action: "list")
            redirect(action: 'show',controller: 'content' ,id:storedFileInstance.content.id)
        }
        catch (org.springframework.dao.DataIntegrityViolationException e)
        {
            flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
            redirect(action: "show", id: params.id)
        }
    }
    else
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
        redirect(action: "list")
    }
}*/

/*def index = {
    redirect(action: "list", params: params)
}

def list = {
    params.max = Math.min(params.max ? params.int('max') : 20, 100)
    [storedFileInstanceList: StoredFile.list(params), storedFileInstanceTotal: StoredFile.count()]
}

def create = {
    def storedFileInstance = new StoredFile()
    storedFileInstance.properties = params
    return [storedFileInstance: storedFileInstance]
}

def save = {
    def storedFileInstance = new StoredFile(params)

    if (storedFileInstance instanceof nl.of.catchplus.BaseRepository)
    {
        storedFileInstance.owner = nl.of.catchplus.ShiroUser.findByUsername(org.apache.shiro.SecurityUtils.subject.principal.toString()).institution
    }

    if (storedFileInstance.save(flush: true))
    {
        if (storedFileInstance instanceof nl.of.catchplus.BaseRepository)
        {
            testService.createOwnerPermission(storedFileInstance)
            testService.createRolePermission(storedFileInstance)
        }
        flash.message = "${message(code: 'default.created.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), storedFileInstance.id])}"
        redirect(action: "show", id: storedFileInstance.id)
    }
    else
    {
        render(view: "create", model: [storedFileInstance: storedFileInstance])
    }
}

def show = {
    def storedFileInstance = StoredFile.get(params.id)
    if (!storedFileInstance)
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
        redirect(action: "list")
    }
    else
    {
        [storedFileInstance: storedFileInstance]
    }
}

def edit = {
    def storedFileInstance = StoredFile.get(params.id)
    if (!storedFileInstance)
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
        redirect(action: "list")
    }
    else
    {
        return [storedFileInstance: storedFileInstance]
    }
}

def update = {
    def storedFileInstance = StoredFile.get(params.id)
    if (storedFileInstance)
    {
        if (params.version)
        {
            def version = params.version.toLong()
            if (storedFileInstance.version > version)
            {

                storedFileInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'storedFile.label', default: 'StoredFile')] as Object[], "Another user has updated this StoredFile while you were editing")
                render(view: "edit", model: [storedFileInstance: storedFileInstance])
                return
            }
        }
        storedFileInstance.properties = params
        if (!storedFileInstance.hasErrors() && storedFileInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), storedFileInstance.id])}"
            redirect(action: "show", id: storedFileInstance.id)
        }
        else
        {
            render(view: "edit", model: [storedFileInstance: storedFileInstance])
        }
    }
    else
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
        redirect(action: "list")
    }
}

def delete = {
    def storedFileInstance = StoredFile.get(params.id)
    if (storedFileInstance)
    {
        try
        {
            storedFileInstance.delete(flush: true)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
            redirect(action: "list")
        }
        catch (org.springframework.dao.DataIntegrityViolationException e)
        {
            flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
            redirect(action: "show", id: params.id)
        }
    }
    else
    {
        flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'storedFile.label', default: 'StoredFile'), params.id])}"
        redirect(action: "list")
    }
}*/
}
