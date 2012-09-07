package nl.of.catchplus

import org.springframework.web.multipart.MultipartFile
import org.apache.shiro.SecurityUtils

class BaseContentController
{

    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def testService
    def gormService

    def scaffold = BaseContent

    /*def create={

        Content contentInstance = new Content(params)
        System.out.println(contentInstance.bundle);
        return [contentInstance: contentInstance]

    }*/

    def save = {


        //Collection c=new Collection()

        MultipartFile file = request.getFile("uploadFile")

        boolean errorOccured=false

        //System.out.println(params);
        BaseContent contentInstance = new BaseContent(params)

        contentInstance.title = contentInstance.title ? contentInstance.title : ""
        contentInstance.description = contentInstance.description ? contentInstance.description : ""
        //contentInstance.url = contentInstance.url ? contentInstance.url : ""



        StoredFile storedFileInstance=null

        contentInstance.owner = ShiroUser.read(SecurityUtils.subject.principal.id).institution

        if (contentInstance.validate() && file && !file?.empty)
        {
            try
            {

                storedFileInstance = createStoredFile(file/*,session.id*/)
                storedFileInstance.content=contentInstance
                contentInstance.storedFile = storedFileInstance
                //contentInstance.stored = true

                //Thumbnail thumbnail = Thumbnail.createThumbnail(klusser.klusserPhoto,session.id)
                //thumbnail.save()
            }
            catch (FileTooBigException e)
            {
                errorOccured=true
                e.printStackTrace()
                //jsonMap.put("message", g.message(code: 'klusserPhoto.upload.filetobig'))
            }
            catch (IncorrectFileTypeException e)
            {
                errorOccured=true
                e.printStackTrace()
                //jsonMap.put("message", g.message(code: 'klusserPhoto.upload.incorrectfiletype'))
            }
            catch (Exception e)
            {
                errorOccured=true
                //jsonMap.put("message", g.message(code: 'klusserPhoto.upload.othererror'))
                e.printStackTrace()
            }
        }

        if (!errorOccured && !contentInstance.hasErrors())
        {

            contentInstance.insert()

            /*if(storedFileInstance)
                testService.createStoredFilePermissions(storedFileInstance)*/

            flash.message = "${message(code: 'default.created.message', args: [message(code: 'content.label', default: 'BaseContent'), contentInstance.id])}"
            redirect(action: "show", id: contentInstance.id)
        }
        else
        {
            render(view: "create", model: [baseContentInstance: contentInstance])
        }


    }

    public StoredFile createStoredFile(final MultipartFile _file/*,def sessionid*/) throws IncorrectFileTypeException, FileTooBigException
    {
        if (_file)
        {
            if (false/*_file.bytes.length > 5000000*/)
            {
                throw new FileTooBigException(_file)
            }
            else if (true/*_file.contentType.startsWith("image/")*/ /*&& !_file.contentType.startsWith("image/png")*/)
            {
                StoredFile storedFile = new StoredFile()

/*
                File aFile = new File(sessionid+'-in.jpg')

                FileUtils.writeByteArrayToFile(aFile, _file.bytes);

                File oFile = new File(sessionid+'-out.jpg')

                ImageMagick convert = new ImageMagick(Arrays.asList("/opt/local/bin"));
                boolean ok=convert.in(aFile).option("-fuzz", "15%").option("-trim").out(oFile).run();

                if(!ok)
                    throw new Exception('ImageMagick convert ERROR')
*/

                storedFile.originalFilename = _file.getOriginalFilename()
                storedFile.setBytes(_file.bytes)
                storedFile.setContentType(_file.contentType)
                //storedFile.setContentType('image/jpeg')
                storedFile.setContentLength(_file.size)
                //content.stored=true

                //FileUtils.deleteQuietly(aFile)
                //FileUtils.deleteQuietly(oFile)
                //System.out.println(storedFile);

                return storedFile
            }
            else
                throw new IncorrectFileTypeException(_file)
        }

        return null
    }

    /*def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [contentInstanceList: Content.list(params), contentInstanceTotal: Content.count()]
    }

    def create = {
        def contentInstance = new Content()
        contentInstance.properties = params
        return [contentInstance: contentInstance]
    }

    def save = {
        def contentInstance = new Content(params)
        if (contentInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'content.label', default: 'Content'), contentInstance.id])}"
            redirect(action: "show", id: contentInstance.id)
        }
        else
        {
            render(view: "create", model: [contentInstance: contentInstance])
        }
    }

    def show = {
        def contentInstance = Content.get(params.id)
        if (!contentInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            [contentInstance: contentInstance]
        }
    }

    def edit = {
        def contentInstance = Content.get(params.id)
        if (!contentInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            return [contentInstance: contentInstance]
        }
    }

    def update = {
        def contentInstance = Content.get(params.id)
        if (contentInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (contentInstance.version > version)
                {

                    contentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'content.label', default: 'Content')] as Object[], "Another user has updated this Content while you were editing")
                    render(view: "edit", model: [contentInstance: contentInstance])
                    return
                }
            }
            contentInstance.properties = params
            if (!contentInstance.hasErrors() && contentInstance.save(flush: true))
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'content.label', default: 'Content'), contentInstance.id])}"
                redirect(action: "show", id: contentInstance.id)
            }
            else
            {
                render(view: "edit", model: [contentInstance: contentInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def contentInstance = Content.get(params.id)
        if (contentInstance)
        {
            try
            {
                contentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), params.id])}"
            redirect(action: "list")
        }
    }*/
}
