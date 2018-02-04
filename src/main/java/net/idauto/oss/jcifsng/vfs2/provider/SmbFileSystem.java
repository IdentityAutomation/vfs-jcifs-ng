/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.idauto.oss.jcifsng.vfs2.provider;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthentication;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import java.util.Collection;

/**
 * An SMB file system.
 */
public class SmbFileSystem
        extends AbstractFileSystem {
    private final CIFSContext cifsContext;

    protected SmbFileSystem(final FileName rootName, final FileSystemOptions fileSystemOptions) {
        super(rootName, null, fileSystemOptions);
        CIFSContext cifsContext = SmbFileSystemConfigBuilder.getInstance().getCIFSContext(fileSystemOptions);
        if (cifsContext == null) {
            cifsContext = SingletonContext.getInstance();
        }

        UserAuthenticationData authData = UserAuthenticatorUtils.authenticate(
                fileSystemOptions,
                SmbFileProvider.AUTHENTICATOR_TYPES);
        if (authData != null) {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(cifsContext,
                    UserAuthenticatorUtils.toString(
                            UserAuthenticatorUtils.getData(authData, UserAuthenticationData.DOMAIN, null)),
                    UserAuthenticatorUtils.toString(
                            UserAuthenticatorUtils.getData(authData, UserAuthenticationData.USERNAME, null)),
                    UserAuthenticatorUtils.toString(
                            UserAuthenticatorUtils.getData(authData, UserAuthenticationData.PASSWORD, null)));
            cifsContext = cifsContext.withCredentials(auth);
        }
        this.cifsContext = cifsContext;
    }

    /**
     * Creates a file object.
     */
    @Override
    protected FileObject createFile(final AbstractFileName name) throws FileSystemException {
        return new SmbFileObject(name, this);
    }

    /**
     * Returns the capabilities of this file system.
     */
    @Override
    protected void addCapabilities(final Collection<Capability> caps) {
        caps.addAll(SmbFileProvider.capabilities);
    }

    CIFSContext getCifsContext() {
        return cifsContext;
    }
}
