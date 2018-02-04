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

import junit.framework.Test;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.test.VfsClassLoaderTests;
import org.apache.commons.vfs2.test.*;

/**
 * Tests for the SMB file system.
 */
public class SmbProviderTestCase
        extends AbstractProviderTestConfig
        implements ProviderTestConfig {
    private static final String TEST_URI = "test.smb.uri";
    private static final String TEST_USER = "test.smb.user";
    private static final String TEST_PASSWORD = "test.smb.password";

    public static Test suite() throws Exception {
        if (System.getProperty(TEST_URI) != null &&
                System.getProperty(TEST_USER) != null &&
        System.getProperty(TEST_PASSWORD) != null)
        {
            return new ProviderTestSuite(new SmbProviderTestCase());
        }
        else
        {
            return notConfigured(SmbProviderTestCase.class);
        }
    }

    /**
     * Prepares the file system manager.
     */
    @Override
    public void prepare(final DefaultFileSystemManager manager)
            throws Exception {
        manager.addProvider("smb", new SmbFileProvider());
    }

    /**
     * Returns the base folder for tests.
     */
    @Override
    public FileObject getBaseTestFolder(final FileSystemManager manager) throws Exception {
        final String uri = System.getProperty(TEST_URI);
        final String user = System.getProperty(TEST_USER);
        final String password = System.getProperty(TEST_PASSWORD);
        StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, password);
        FileSystemOptions options = new FileSystemOptions();
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(options, auth);
        return manager.resolveFile(uri, options);
    }
}
