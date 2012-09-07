
import org.tmatesoft.svn.core.wc.*;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.io.*;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import groovy.xml.StreamingMarkupBuilder

eventWarStart = { type ->

    println "******************* eventWarStart *****************"
    try {
        // initialise SVNKit
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();

        SVNClientManager clientManager = SVNClientManager.newInstance();
        println "clientManager = " + clientManager.toString();
        SVNWCClient wcClient = clientManager.getWCClient();
        println "wcClient = " + wcClient.toString();

        // the svnkit equivalent of "svn info"
        File baseFile = new File(basedir);

        println "baseFile = " + baseFile.toString();
        SVNInfo svninfo = wcClient.doInfo(baseFile, SVNRevision.WORKING);
        println "svninfo = " + svninfo.toString();

        def version = svninfo.getRevision();
        println "Setting Version to: ${version}"
        metadata.'app.version' = "${version}".toString()
        metadata.persist()

    }
    catch (SVNException ex) {
        //something went wrong
        println "*************** SVN exception ***************"
        println ex.getMessage();
    }

} // End eventWarStart()


eventWebXmlEnd = {String tmpfile ->
  def root = new XmlSlurper().parse(webXmlFile)
  root.appendNode {
    'listener' {
      'listener-class' (
        'nl.of.catchplus.MyHttpSessionListener'
      )
    }
  }
  webXmlFile.text = new StreamingMarkupBuilder().bind {
    mkp.declareNamespace(
      "": "http://java.sun.com/xml/ns/j2ee")
    mkp.yield(root)
  }
}