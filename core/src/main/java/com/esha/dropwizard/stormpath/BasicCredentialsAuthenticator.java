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
package com.esha.dropwizard.stormpath;

import com.google.common.base.Optional;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.resource.ResourceException;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicCredentialsAuthenticator
    implements Authenticator<BasicCredentials, Account> {

    private static final Logger logger =
        LoggerFactory.getLogger(BasicCredentialsAuthenticator.class);

    private final Application application;

    /**
     * Creates a new instance.
     *
     * @param client the Stormpath client
     * @param applicationRestUrl the Stormpath API URL for the application
     * @throws NullPointerException if {@code client} is null
     */
    public BasicCredentialsAuthenticator(final Client client, final String applicationRestUrl) {
        this.application = client.getDataStore().getResource(applicationRestUrl, Application.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> authenticate(final BasicCredentials credentials)
        throws AuthenticationException {

        Optional<Account> principal = Optional.absent();
        try {
            final UsernamePasswordRequest request =
                new UsernamePasswordRequest(
                    credentials.getUsername(), credentials.getPassword());
            final Account account =
                this.application.authenticateAccount(request).getAccount();
            principal = Optional.fromNullable(account);
        } catch (final ResourceException e) {
            // The Stormpath SDK doesn't currently distinguish authentication
            // errors from general resource errors, so log the exception and
            // fall through with an absent principal.
            if (logger.isDebugEnabled()) {
                logger.debug("\"{}\", status={}, code={}, moreInfo=\"{}\"", new Object[] {e.getDeveloperMessage(), e.getStatus(), e.getCode(), e.getMoreInfo()});
            }
        }
        return principal;
    }

}
