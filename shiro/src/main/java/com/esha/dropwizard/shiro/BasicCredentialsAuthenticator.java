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
package com.esha.dropwizard.shiro;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
// import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicCredentialsAuthenticator
    implements Authenticator<BasicCredentials, Subject> {

    private static final Logger logger =
        LoggerFactory.getLogger(BasicCredentialsAuthenticator.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Subject> authenticate(final BasicCredentials credentials)
        throws AuthenticationException {

        Optional<Subject> principal = Optional.absent();
        try {
            final Subject subject = SecurityUtils.getSubject();
            subject.login(
                new UsernamePasswordToken(
                    credentials.getUsername(), credentials.getPassword()));
            principal = Optional.of(subject);
        } catch (final CredentialsException ce) {
            logger.debug("Invalid credentials", ce);
        } catch (final UnknownAccountException uae) {
            logger.debug("Unknown account", uae);
        } catch (final org.apache.shiro.authc.AuthenticationException ae) {
            logger.debug("Unspecified error", ae);
        } catch (final ShiroException se) {
            throw new AuthenticationException(se);
        }
        return principal;
    }

}
