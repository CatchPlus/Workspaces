package nl.of.catchplus

import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.grails.jaxrs.provider.UnAuthorizedException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import org.grails.jaxrs.provider.DomainInstanceHasErrorsException

class MetaRecordResourceService {

    def permissionService

    def create(MetaRecord dto) {

       /* def authToken = new UsernamePasswordToken('super@cp.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)
*/
        dto.value = dto.value ? dto.value : ""

        if (BaseRepository.countById(dto.baseRepository?.id)==0) {
            throw new DomainObjectNotFoundException(BaseRepository.class, dto.baseRepository?.id)
        }

        if (MetaRecordKey.countById(dto.key?.id)==0) {
            throw new DomainObjectNotFoundException(MetaRecordKey.class, dto.key?.id)
        }

        if (!permissionService.baseRepositoryEdit(dto.baseRepository)) {
            throw new UnAuthorizedException(BaseRepository.class, dto.baseRepository?.id)
        }

        dto.merge()

        if(!dto.validate())
        {
            throw new DomainInstanceHasErrorsException(MetaRecord.class, dto.retrieveErrors())
        }

        dto.store()

        return dto
    }


    def read(def id) {

 /*       def authToken = new UsernamePasswordToken('super@cp.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)
*/
        def obj = MetaRecord.read(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MetaRecord.class, id)
        }

        if (!permissionService.baseRepositoryRead(obj.baseRepository)) {
            throw new UnAuthorizedException(MetaRecord.class, id)
        }

        obj
    }

    def readAll() {
   /*     def authToken = new UsernamePasswordToken('super@cp.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)
*/
        permissionService.metaRecordListRead()
    }

    def update(MetaRecord dto) {

      /*  def authToken = new UsernamePasswordToken('super@cp.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)
*/
        def obj = MetaRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MetaRecord.class, dto.id)
        }

        if (!permissionService.baseRepositoryEdit(dto.baseRepository)) {
            throw new UnAuthorizedException(MetaRecord.class, obj.id)
        }

        obj.properties['value'] = dto.properties

        obj.value = obj.value ? obj.value : ""

        if(!obj.validate())
        {
            throw new DomainInstanceHasErrorsException(MetaRecord.class, obj.retrieveErrors())
        }

        obj.store()

        obj
    }

    void delete(def id) {

        /*def authToken = new UsernamePasswordToken('super@cp.nl', 'qwerty')
        SecurityUtils.subject.login(authToken)
*/
        def obj = MetaRecord.get(id)

        if (!obj) {
            throw new DomainObjectNotFoundException(MetaRecord.class, id)
        }

        if (!permissionService.baseRepositoryDelete(obj.baseRepository)) {
            throw new UnAuthorizedException(MetaRecord.class, id)
        }

        obj.delete()

    }

}

