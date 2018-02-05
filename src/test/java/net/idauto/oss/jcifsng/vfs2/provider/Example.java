/*
 * Copyright 2018, Identity Automation, LP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.idauto.oss.jcifsng.vfs2.provider;

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

/**
 * @author svella
 */
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
