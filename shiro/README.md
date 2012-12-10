# Dropwizard Stormpath - Shiro

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

Add an instance of the Stormpath-Shiro bundle to your service. For example,

    ...
    import com.esha.dropwizard.stormpath.StormpathConfiguration;
    import com.esha.dropwizard.stormpath.shiro.StormpathShiroBundle;
    import com.esha.dropwizard.stormpath.shiro.StormpathShiroConfiguration;
    ...
    private final StormpathShiroBundle stormpathBundle =
        new StormpathBundle<MyConfiguration>() {
            @Override
            public Optional<StormpathConfiguration> getStormpathConfiguration(final MyConfiguration configuration) {
                return Optional.<StormpathConfiguration>fromNullable(configuration.getStormpathConfiguration());
            }
            @Override
            public Optional<StormpathShiroConfiguration> getStormpathShiroConfiguration(final MyConfiguration configuration) {
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

This module provides an HTTP Basic authenticator. It requires no configuration. (It also has no Stormpath dependency and is designed to be used for any Shiro-integrated Dropwizard service.)

    ...
    import com.esha.dropwizard.shiro.BasicCredentialsAuthenticator;
    import org.apache.shiro.subject.Subject;
    ...
    @Override
    public void run(final MyConfiguration configuration, final Environment environment) throws Exception {
        ...
        final BasicCredentialsAuthenticator authenticator = new BasicCredentialsAuthenticator();
        environment.addProvider(new BasicAuthProvider<Subject>(authenticator, "myrealm"));
        ...
    }
    ...