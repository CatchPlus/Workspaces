package nl.of.catchplus

class MetaRecord extends BaseDomain
{

    String value
    MetaRecordKey key

    BaseRepository baseRepository

    //static belongsTo = [baseRepository: BaseRepository]

    static searchable = {
        supportUnmarshall false
    }

    static constraints = {
        value(maxSize: 2000)
        key(nullable: false, validator: { val, obj ->
            //WorkSpace workSpace=WorkSpace.list().iterator().next()

            WorkSpace workSpace

            if (obj.baseRepository instanceof Collection)
            {
                workSpace = obj.baseRepository.ownerWorkSpace
            }
            else if (obj.baseRepository instanceof Bundle)
            {
                workSpace = obj.baseRepository.ownerCollection.ownerWorkSpace
            }
            else if (obj.baseRepository instanceof BaseContent)
            {
                workSpace = obj.baseRepository.ownerBundle.ownerCollection.ownerWorkSpace
            }
            //baseRepository.

            if (obj.baseRepository instanceof Collection)
            {
                /*System.out.println("X");
                System.out.println(workSpace.collectionMetaRecordKey);
                System.out.println(workSpace.collectionMetaRecordKey.contains(obj.key));
                System.out.println(obj.key);
                System.out.println(obj.key.class);

                workSpace.collectionMetaRecordKey.each {test->
                    System.out.println(test.class);
                }

                System.out.println("Y");*/
                return workSpace.collectionMetaRecordKey.contains(obj.key) ? true : ['invalid.collectionmetamecordKey']
            }
            else if (obj.baseRepository instanceof Bundle)
            {
                return workSpace.bundleMetaRecordKey.contains(obj.key) ? true : ['invalid.bundlemetaRecordkey']
            }
            else if (obj.baseRepository instanceof BaseContent)
            {
                return workSpace.baseContentMetaRecordKey.contains(obj.key) ? true : ['invalid.basecontentmetarecordKey']
            }

            //System.out.println("BLABLABLAB KUTTT");
            return false

        })
    }

    public String toString()
    {
        return "${key} : ${value ? value : ""}"
    }

}
