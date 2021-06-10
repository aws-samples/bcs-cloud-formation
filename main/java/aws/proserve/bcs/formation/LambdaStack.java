// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.Bucket;

import java.util.HashMap;
import java.util.Map;

public abstract class LambdaStack extends Stack {
    private final Map<String, Function> functionMap = new HashMap<>();

    private final BucketProvider bucketProvider;
    private IRole role;

    protected LambdaStack(
            Construct scope,
            String id,
            BucketProvider bucketProvider) {
        super(scope, id);
        this.bucketProvider = bucketProvider;
    }

    protected void setRole(IRole role) {
        this.role = role;
    }

    protected Bucket getBucket() {
        return bucketProvider.getBucket();
    }

    protected IRole getRole() {
        return role;
    }

    protected Function createFunction(
            Construct parent,
            IRole role,
            String key,
            String functionName,
            String handler,
            String description,
            Bucket bucket,
            String file) {
        return createFunction(parent, role, key, functionName, handler, description, bucket, file, null);
    }

    protected Function createFunction(
            Construct parent,
            IRole role,
            String key,
            String functionName,
            String handler,
            String description,
            Bucket bucket,
            String file,
            Map<String, String> environment) {
        final var function = Function.Builder
                .create(parent, functionName)
                .functionName(functionName)
                .handler(handler)
                .role(role)
                .memorySize(1024)
                .timeout(Duration.minutes(10))
                .runtime(Runtime.JAVA_11)
                .code(Code.fromBucket(bucket, file))
                .description(description)
                .logRetention(RetentionDays.ONE_WEEK)
                .environment(environment)
                .build();
        functionMap.put(key, function);
        return function;
    }

    public Map<String, Function> getFunctionMap() {
        return functionMap;
    }
}
