# vfs-jcifs-ng
SMB Provider for Apache commons-vfs (Virtual File System) based on jcifs-ng

This is pretty much the code for the CIFS provider available in the [Commons VFS](https://commons.apache.org/proper/commons-vfs/) Sandbox changed
slightly to account for the API changes between the original [JCIFS](https://jcifs.samba.org/) and [JCIFS-NG](https://github.com/AgNO3/jcifs-ng).

## Maven
```xml
<dependency>
    <groupId>net.idauto.oss.jcifs</groupId>
    <artifactId>vfs-jcifs-ng</artifactId>
    <version>1.0.1-jdk17-SNAPSHOT</version>
</dependency>
```

## Notes

* You must provide the versions of Commons VFS (tested with 2.1 and 2.2), and jcifs-ng (tested with 2.0.4, and 2.1.0-SNAPSHOT) that you wish to use.
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-vfs2</artifactId>
    <version>2.2</version>
</dependency>
<dependency>
    <groupId>eu.agno3.jcifs</groupId>
    <artifactId>jcifs-ng</artifactId>
    <version>2.1.11-idauto-jdk17-SNAPSHOT</version>
</dependency>
```
* Commons VFS uses Apache Commons Logging and JCIFS-NG used SLF4J, so to get full logging you need to account for both.
* JCIFS-NG apparently needs Unlimited Crypto enabled for the JVM, but that may depend on servers you are connecting to.
* I didn't implement support for the deprecated practice of putting the credentials in the url. You can provide the
credentials in either the CIFSContext or using StaticUserAuthenticator.
* By default the SingletonContext will be used, but you can provide a customized CIFSContext using
SmbFileSystemConfigBuilder.setCIFSContext()
* An example of using StaticUserAuthenticator for authentication and a custom CIFSContext:
```java
import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class Example {
    public static void main(String[] args) throws CIFSException, FileSystemException, URISyntaxException {
        if (args.length != 3) {
            System.err.println(" Usage: Example <server> <username> <password>");
            System.exit(-1);
        }

        final String host = args[0];
        final String username = args[1];
        final String password = args[2];

        final URI uri = new URI("smb", host, "/C$", null);

        // authentication
        StaticUserAuthenticator auth = new StaticUserAuthenticator(null, username, password);

        // jcifs configuration
        Properties jcifsProperties = new Properties();

        // these settings are needed for 2.0.x to use anything but SMB1, 2.1.x enables by default and will ignore
        jcifsProperties.setProperty("jcifs.smb.client.enableSMB2", "true");
        jcifsProperties.setProperty("jcifs.smb.client.useSMB2Negotiation", "true");

        CIFSContext jcifsContext = new BaseContext(new PropertyConfiguration(jcifsProperties));

        // pass in both to VFS
        FileSystemOptions options = new FileSystemOptions();
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(options, auth);
        SmbFileSystemConfigBuilder.getInstance().setCIFSContext(options, jcifsContext);

        final FileSystemManager fsManager = VFS.getManager();
        try (FileObject file = fsManager.resolveFile(uri.toString(), options)) {
            for (FileObject child : file.getChildren()) {
                System.out.println(child.getName());
            }
        }
    }
}
```

