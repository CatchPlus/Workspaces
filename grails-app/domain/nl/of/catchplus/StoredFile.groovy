package nl.of.catchplus

class StoredFile extends BaseDomain
{

    FileVaultService fileVaultService

    def testService

    byte[] bytes
    String contentType
    long contentLength
    String originalFilename

    String md5

    boolean deleted=false

    //Institution owner

    //Content content




    static transients = ['fileVaultService', 'bytes', 'testService']

    BaseContent content

    static constraints = {
        content(nullable: true)
        contentType(nullable: true, blank: true)
    }

    String toString()
    {
        this.originalFilename
    }

    public static StoredFile findByMd5AndBundle(String MD5, long bundleID)
        {
            return StoredFile.createCriteria().get {
                and {
                    content {
                        ownerBundle {
                            idEq(bundleID)
                        }
                    }
                    eq('md5', MD5)
                    maxResults(1)
                }
                /*projections {
                    countDistinct "id"
                }*/
            }
        }

    /*public String url()
    {

    }*/

    public void treeDelete()
    {
        this.fileVaultService.deleteFileFromVault(this)
        this.contentType = null
        this.contentLength = 0
        this.originalFilename = null
        this.md5 = null
        this.deleted=true


    }


    public void mySave()
    {
        StoredFile.withTransaction {
            this.save()
            //testService.createStoredFileRolePermission(this)

        }
    }

    /*public void insert()
    {
        testService.createOwnerPermission(this)
        testService.createRolePermission(this)

    }*/

    @Override
    protected void onAfterInsert()
    {
        //System.out.println('onAfterInsert werkt weeeeerrr');
        if (fileVaultService && bytes && id)
        {
            fileVaultService.saveFileToVault(this)
        }
    }


    @Override
    protected void onAfterDelete()
    {
        if (fileVaultService && id)
            fileVaultService.deleteFileFromVault(this)
    }
}

