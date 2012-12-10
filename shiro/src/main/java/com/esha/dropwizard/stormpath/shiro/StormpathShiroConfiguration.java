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

import com.esha.dropwizard.stormpath.StormpathConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class StormpathShiroConfiguration extends StormpathConfiguration {

    @JsonProperty
    private boolean sessionStorageEnabled = false;

    public boolean isSessionStorageEnabled() {
        return this.sessionStorageEnabled;
    }

    public void setSessionStorageEnabled(final boolean sessionStorageEnabled) {
        this.sessionStorageEnabled = sessionStorageEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Objects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("sessionStorageEnabled", this.sessionStorageEnabled);
    }

}
