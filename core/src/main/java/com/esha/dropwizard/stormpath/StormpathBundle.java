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

import java.util.Properties;
import com.google.common.base.Optional;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StormpathBundle<T extends Configuration>
    implements ConfiguredBundle<T>, ConfigurationStrategy<T> {

    private static final Logger logger =
        LoggerFactory.getLogger(StormpathBundle.class);

    private Client client;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final T configuration, final Environment environment)
        throws Exception {

        final Optional<StormpathConfiguration> stormpathConfig =
            getStormpathConfiguration(configuration);
        if (stormpathConfig.isPresent()) {
            logger.debug("Stormpath is configured");
            initializeStormpath(stormpathConfig.get());
        } else {
            logger.debug("Stormpath is not configured");
        }
    }

    private void initializeStormpath(final StormpathConfiguration config) {
        if (config.isEnabled()) {
            logger.debug("Stormpath is enabled");
            this.client = buildClient(config);
        } else {
            logger.debug("Stormpath is not enabled");
        }
    }

    private static final String API_KEY_ID     = "apiKey.id";
    private static final String API_KEY_SECRET = "apiKey.secret";

    private Client buildClient(final StormpathConfiguration config) {
        final Properties apiKeyProperties = new Properties();
        apiKeyProperties.setProperty(API_KEY_ID, config.getApiKeyId());
        apiKeyProperties.setProperty(API_KEY_SECRET, config.getApiKeySecret());

        final ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.setApiKeyIdPropertyName(API_KEY_ID);
        clientBuilder.setApiKeySecretPropertyName(API_KEY_SECRET);
        clientBuilder.setApiKeyProperties(apiKeyProperties);
        return clientBuilder.build();
    }

    /**
     * Returns the Stormpath client.
     *
     * @return the Stormpath client
     */
    public Client getClient() {
        return this.client;
    }

}
