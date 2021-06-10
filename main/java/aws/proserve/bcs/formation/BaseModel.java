// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import org.yaml.snakeyaml.Yaml;
import software.amazon.awscdk.core.App;

import javax.inject.Singleton;

@Module
@Singleton
public class BaseModel {

    @Provides
    @Singleton
    static App app() {
        return new App();
    }

    @Provides
    @Singleton
    static Yaml yaml() {
        return new Yaml();
    }

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
