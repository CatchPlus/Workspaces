package nl.of.catchplus

import org.springframework.web.multipart.MultipartFile

class FileTooBigException extends Exception {

  String filename
  int fileSize

  public FileTooBigException( String filename, int fileSize ) {
    this.filename = filename
    this.fileSize = fileSize
  }

  public FileTooBigException( MultipartFile file ) {
    this.filename = file.getOriginalFilename()
    this.fileSize = file.getBytes().size()
  }

}