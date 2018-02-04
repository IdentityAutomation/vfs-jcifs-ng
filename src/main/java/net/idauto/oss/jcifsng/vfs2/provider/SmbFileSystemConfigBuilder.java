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
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemOptions;

/**
 * The config builder for various Smb configuration options.
 */
public class SmbFileSystemConfigBuilder extends FileSystemConfigBuilder {
    private static final String _PREFIX = SmbFileSystemConfigBuilder.class.getName();

    private static final SmbFileSystemConfigBuilder BUILDER = new SmbFileSystemConfigBuilder();

    private static final String CIFSCONTEXT = _PREFIX + ".CIFSCONTEXT";

    private SmbFileSystemConfigBuilder() {
        super("jcifsng.");
    }

    /**
     * Create new config builder with specified prefix string.
     *
     * @param prefix prefix string to use for parameters of this config builder.
     */
    @SuppressWarnings("unused")
    protected SmbFileSystemConfigBuilder(final String prefix) {
        super(prefix);
    }

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance.
     */
    public static SmbFileSystemConfigBuilder getInstance() {
        return BUILDER;
    }

    @Override
    protected Class<? extends FileSystem> getConfigClass() {
        return SmbFileSystem.class;
    }

    /**
     * Gets the base CIFSContext to use for this file system.
     *
     * @param opts the FileSystemOptions
     * @return the base CIFSContext to use for this file system
     */
    @SuppressWarnings("unused")
    public CIFSContext getCIFSContext(final FileSystemOptions opts) {
        return (CIFSContext) getParam(opts, CIFSCONTEXT);
    }

    /**
     * Sets the base CIFSContext to use for this file system.
     * <p>
     * If you set cifsContext to {@code null} the default SingletonContext will be used.
     *
     * @param opts        The FileSystemOptions
     * @param cifsContext the base CIFSContext to use for this file system
     * @since 2.1
     */
    @SuppressWarnings("unused")
    public void setCIFSContext(final FileSystemOptions opts, final CIFSContext cifsContext) {
        setParam(opts, CIFSCONTEXT, cifsContext);
    }

}
