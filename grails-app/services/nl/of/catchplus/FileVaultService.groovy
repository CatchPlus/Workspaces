package nl.of.catchplus

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils

class FileVaultService
{
    boolean transactional = false

    def grailsApplication

    def deleteAllFilesFromVault()
    {
        File[] subFiles = new File(grailsApplication.config.file.vault.location).listFiles()
        if (subFiles)
        {
            for (File subFile : subFiles)
            {
                if (subFile.isDirectory())
                    FileUtils.deleteDirectory(subFile)
                else
                    FileUtils.deleteQuietly(subFile)
            }
        }
    }

    public int getMaxUploadFileSize()
    {
        Integer.valueOf(grailsApplication.config.file.upload.max.size).intValue()
    }

    public File getFileFromVault(final StoredFile storedFile) throws IOException
    {
        StringBuilder sb = new StringBuilder(grailsApplication.config.file.vault.location);
        sb.append(File.separator).append(storedFile.id).append('.').append(StringUtils.substringAfterLast(storedFile.originalFilename, "."));
        return new File(sb.toString())
    }

    def deleteFileFromVault(final StoredFile storedFile)
    {
        StringBuilder sb = new StringBuilder(grailsApplication.config.file.vault.location);
        sb.append(File.separator).append(storedFile.id).append('.').append(StringUtils.substringAfterLast(storedFile.originalFilename, "."));
        FileUtils.deleteQuietly(new File(sb.toString()))
    }

    def saveFileToVault(final StoredFile storedFile)/* throws IOException*/
    {
        StringBuilder sb = new StringBuilder(grailsApplication.config.file.vault.location);
        sb.append(File.separator).append(storedFile.id).append('.').append(StringUtils.substringAfterLast(storedFile.originalFilename, "."));
        try
        {
            FileUtils.writeByteArrayToFile(new File(sb.toString()), storedFile.bytes);
        }
        catch (IOException e)
        {e.printStackTrace()}
    }
}