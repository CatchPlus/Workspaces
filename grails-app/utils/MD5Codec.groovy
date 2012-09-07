/*

import java.security.MessageDigest
import sun.misc.BASE64Encoder

class MD5Codec {
	// Returns the byte[] of the digest
  */
/*  static encode = { theTarget ->
		if (theTarget == null) {
			return null
		} else {
			def md = MessageDigest.getInstance("MD5")
			md.update(theTarget.toString().getBytes()) // This probably needs to use the thread's Locale encoding
			return md.digest()
		}
	}*//*


    static encode = { str ->
        MessageDigest md = MessageDigest.getInstance('md5')
        md.update(str.getBytes('UTF-8'))
        return (new BASE64Encoder()).encode(md.digest())
    }

	static decode = { theTarget ->
		throw new UnsupportedOperationException("Cannot decode md5 hashes")
	}
}*/
