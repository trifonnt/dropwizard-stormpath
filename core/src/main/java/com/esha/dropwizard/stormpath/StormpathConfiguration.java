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

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class StormpathConfiguration {

    @JsonProperty
    private boolean enabled = false;

    @NotNull
    @JsonProperty
    private String apiKeyId = null;

    @NotNull
    @JsonProperty
    private String apiKeySecret = null;

    @NotNull
    @JsonProperty
    private String applicationRestUrl = null;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKeyId() {
        return this.apiKeyId;
    }

    public void setApiKeyId(final String apiKeyId) {
        this.apiKeyId = apiKeyId;
    }

    public String getApiKeySecret() {
        return this.apiKeySecret;
    }

    public void setApiKeySecret(final String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }

    public String getApplicationRestUrl() {
        return this.applicationRestUrl;
    }

    public void setApplicationRestUrl(final String applicationRestUrl) {
        this.applicationRestUrl = applicationRestUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
            .add("enabled", this.enabled)
            .add("apiKeyId", this.apiKeyId)
            .add("apiKeySecret",
                (this.apiKeySecret != null) ? "********" : null)
            .add("applicationRestUrl", this.applicationRestUrl);
    }

}
