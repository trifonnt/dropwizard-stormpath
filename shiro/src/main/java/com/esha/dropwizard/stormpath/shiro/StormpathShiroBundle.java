/*
 * Copyright 2012 ESHA Research, Inc.
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
package com.esha.dropwizard.stormpath.shiro;

import com.esha.dropwizard.stormpath.StormpathBundle;
import com.google.common.base.Optional;
import com.stormpath.shiro.realm.ApplicationRealm;
import com.stormpath.shiro.realm.GroupPermissionResolver;
import com.stormpath.shiro.realm.GroupRoleResolver;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StormpathShiroBundle<T extends Configuration>
    extends StormpathBundle<T>
    implements ConfigurationStrategy<T>, GroupResolutionStrategy<T> {

    private static final Logger logger =
        LoggerFactory.getLogger(StormpathShiroBundle.class);

    private SecurityManager securityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        super.initialize(bootstrap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final T configuration, final Environment environment)
        throws Exception {

        super.run(configuration, environment);

        final Optional<StormpathShiroConfiguration> stormpathShiroConfig =
            getStormpathShiroConfiguration(configuration);
        if (stormpathShiroConfig.isPresent()) {
            logger.debug("Stormpath Shiro is configured");
            initializeShiro(stormpathShiroConfig.get(),
                getGroupPermissionResolver(configuration),
                getGroupRoleResolver(configuration));
        } else {
            logger.debug("Stormpath Shiro is not configured");
        }
    }

    private void initializeShiro(final StormpathShiroConfiguration config,
        final Optional<GroupPermissionResolver> groupPermissionResolver,
        final Optional<GroupRoleResolver> groupRoleResolver) {

        if (config.isEnabled()) {
            logger.debug("Stormpath Shiro is enabled");
            this.securityManager =
                buildSecurityManager(config, groupPermissionResolver, groupRoleResolver);
            SecurityUtils.setSecurityManager(securityManager);
        } else {
            logger.debug("Stormpath Shiro is not enabled");
        }
    }

    private SecurityManager buildSecurityManager(final StormpathShiroConfiguration config,
        final Optional<GroupPermissionResolver> groupPermissionResolver,
        final Optional<GroupRoleResolver> groupRoleResolver) {

        final ApplicationRealm applicationRealm = new ApplicationRealm();
        applicationRealm.setApplicationRestUrl(config.getApplicationRestUrl());
        applicationRealm.setClient(getClient());

        if (groupPermissionResolver.isPresent()) {
            applicationRealm.setGroupPermissionResolver(groupPermissionResolver.get());
        }

        if (groupRoleResolver.isPresent()) {
            applicationRealm.setGroupRoleResolver(groupRoleResolver.get());
        }

        final DefaultSecurityManager securityManager =
            new DefaultSecurityManager(applicationRealm);
        ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)securityManager.getSubjectDAO())
            .getSessionStorageEvaluator()).setSessionStorageEnabled(config.isSessionStorageEnabled());
        return securityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GroupPermissionResolver> getGroupPermissionResolver(T configuration) {
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GroupRoleResolver> getGroupRoleResolver(T configuration) {
        return Optional.absent();
    }

    public SecurityManager getSecurityManager() {
        return this.securityManager;
    }

}