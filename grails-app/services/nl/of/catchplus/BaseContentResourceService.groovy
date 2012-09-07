package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException

import org.grails.jaxrs.provider.UnAuthorizedException
import org.grails.jaxrs.provider.DomainInstanceHasErrorsException
import org.xml.sax.SAXException

class BaseContentResourceService
{

    def permissionService
    def grailsApplication

    def fileVaultService

    static transactional = false



    def create(String xml, String fileName, byte[] content)
    {

        /*def authToken = new UsernamePasswordToken('instituut01@admin.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)*/
        //System.out.println(xml);

        BaseContent dto = new BaseContent()
        Node baseContentXML

        try
        {
            baseContentXML = new XmlParser().parseText(xml)
        }
        catch (SAXException e)
        {
            //Same error as other rest calls
            throw new NoSuchElementException()
        }

        def iter = baseContentXML.iterator()

        while (iter.hasNext())
        {
            Node node = (Node) iter.next()

            if (node.name() == 'title')
                dto.title = node.value().iterator().next()
            else if (node.name() == 'description')
                dto.description = node.value().iterator().next()
            else if (node.name() == 'url')
                dto.url = node.value().iterator().next()
            else if (node.name() == 'ownerBundle')
            {

                Long id = Long.parseLong(node.attribute('id').toString())
                dto.ownerBundle = Bundle.findByIdAndDeleted(id, false)

                if (!dto.ownerBundle)
                {
                    throw new DomainObjectNotFoundException(Bundle.class, id)
                }
            }
        }

        dto.title = dto.title ? dto.title : ""
        dto.description = dto.description ? dto.description : ""
        dto.url = dto.url ? dto.url : ""

        dto.validate()

        if (dto.hasErrors())
        {
            throw new DomainInstanceHasErrorsException(BaseContent.class, dto.retrieveErrors())
        }
        else

        if (fileName)
        {
            def maxSize = fileVaultService.getMaxUploadFileSize()
            if (content.size() > maxSize)
            {
                throw new DomainInstanceHasErrorsException(BaseContent.class, [CatchplusUtils.getApplicationTagLib().message(code: 'storedFile.bytes.maxSize.error', args: [Math.round(maxSize / 1024) + 'kB'])])
            }
            else
            {

                StoredFile storedFile = new StoredFile()
                storedFile.originalFilename = fileName
                storedFile.content = dto
                storedFile.setBytes(content)
                storedFile.setContentLength(storedFile.bytes.length)
                storedFile.setContentType(URLConnection.getFileNameMap().getContentTypeFor(fileName))
                storedFile.md5 = org.codehaus.groovy.grails.plugins.codecs.MD5Codec.encode(storedFile.bytes)

                StoredFile existingFile=StoredFile.findByMd5AndBundle(storedFile.md5, dto.ownerBundle.id)

                if (false && existingFile)
                {
                    throw new DomainInstanceHasErrorsException(BaseContent.class, [CatchplusUtils.getApplicationTagLib().message(code: 'storedFile.MD5.unique.error', args: [existingFile.originalFilename, existingFile.md5])])
                }
                else
                {
                    dto.store()
                    storedFile.save()
                }
            }
        }
        else
            dto.store()



        return dto
    }


    def read(def id)
    {

        def obj = BaseContent.read(id)
        if (!obj)
        {
            throw new DomainObjectNotFoundException(BaseContent.class, id)
        }

        if (!permissionService.baseContentRead(obj))
        {
            throw new UnAuthorizedException(BaseContent.class, id)
        }

        obj
    }

    def readAll()
    {
        permissionService.baseContentListRead()
    }

    def update(BaseContent dto)
    {


        def obj = BaseContent.findByIdAndDeleted(dto.id, false)
        if (!obj)
        {
            throw new DomainObjectNotFoundException(BaseContent.class, dto.id)
        }

        if (!permissionService.baseContentEdit(obj))
        {
            throw new UnAuthorizedException(BaseContent.class, obj.id)
        }

        obj.properties['title', 'description', 'url'] = dto.properties

        obj.title = obj.title ? obj.title : ""
        obj.description = obj.description ? obj.description : ""

        if (!obj.validate())
        {
            throw new DomainInstanceHasErrorsException(BaseContent.class, obj.retrieveErrors())
        }

        obj.store()

        obj
    }

    void delete(def id)
    {
        def obj = BaseContent.findByIdAndDeleted(id, false)

        if (!obj)
        {
            throw new DomainObjectNotFoundException(BaseContent.class, id)
        }

        if (!permissionService.baseContentDelete(obj))
        {
            throw new UnAuthorizedException(BaseContent.class, id)
        }

        obj.myDelete()


    }

}

