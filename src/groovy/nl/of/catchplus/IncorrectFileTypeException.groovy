package nl.of.catchplus

import org.springframework.web.multipart.MultipartFile

class IncorrectFileTypeException extends Exception {

  String filename
  String detectedContentType

  public IncorrectFileTypeException( String filename, String detectedContentType ) {
    this.filename = filename
    this.detectedContentType = detectedContentType
  }

  public IncorrectFileTypeException( MultipartFile file ) {
    this.filename = file.getOriginalFilename()
    this.detectedContentType = file.getContentType()
  }

}
