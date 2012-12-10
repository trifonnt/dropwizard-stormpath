# Dropwizard Stormpath - Core

*NOTE:* Due to the way the Stormpath SDK HTTP client determines its version, your Dropwizard service **MUST** include a corresponding `Implementation-Version` entry in the shaded JAR's manifest. For example,

    ...
    <properties>
        <stormpath-sdk.version>0.6.0</stormpath-sdk.version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>com.stormpath.sdk</groupId>
            <artifactId>stormpath-sdk-httpclient</artifactId>
            <version>${stormpath-sdk.version}</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    ...
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.example.MyService</mainClass>
                        <manifestEntries>
                            <Implementation-Version>${stormpath-sdk.version}</Implementation-Version>
                        </manifestEntries>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>

## Usage

Add an instance of the Stormpath bundle to your service. For example,

    ...
    import com.esha.dropwizard.stormpath.StormpathBundle;
    import com.esha.dropwizard.stormpath.StormpathConfiguration;
    ...
    private final StormpathBundle stormpathBundle =
        new StormpathBundle<MyConfiguration>() {
            @Override
            public Optional<StormpathConfiguration> getStormpathConfiguration(final MyConfiguration configuration) {
                return Optional.fromNullable(configuration.getStormpathConfiguration());
            }
        };
    ...
    @Override
    public void initialize(final Bootstrap<MyConfiguration> bootstrap) {
        ...
        bootstrap.addBundle(this.stormpathBundle);
        ...
    }
    ...

This module provides an HTTP Basic authenticator. Initialize it with the Stormpath client instance provided by the bundle and add it to your service's environment.

    ...
    import com.esha.dropwizard.stormpath.BasicCredentialsAuthenticator;
    import com.stormpath.sdk.account.Account;
    import com.stormpath.sdk.client.Client;
    ...
    @Override
    public void run(final MyConfiguration configuration, final Environment environment) throws Exception {
        ...
        final Client stormpathClient = this.stormpathBundle.getClient();
        final String applicationRestUrl = configuration.getStormpathConfiguration().getApplicationRestUrl();
        final BasicCredentialsAuthenticator authenticator = new BasicCredentialsAuthenticator(stormpathClient, applicationRestUrl);
        environment.addProvider(new BasicAuthProvider<Account>(authenticator, "myrealm"));
        ...
    }
    ...
