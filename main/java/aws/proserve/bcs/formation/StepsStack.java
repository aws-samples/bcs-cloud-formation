// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.stepfunctions.Fail;
import software.amazon.awscdk.services.stepfunctions.JsonPath;
import software.amazon.awscdk.services.stepfunctions.TaskInput;
import software.amazon.awscdk.services.stepfunctions.TaskStateBase;
import software.amazon.awscdk.services.stepfunctions.tasks.EcsRunTask;
import software.amazon.awscdk.services.stepfunctions.tasks.LambdaInvoke;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @apiNote Do NOT use LambdaInvoke, which is extremely difficult to use.
 */
public abstract class StepsStack extends Stack {

    private final Map<String, TaskStateBase> stateMap = new HashMap<>();
    private final Map<String, EcsRunTask> fargateMap = new HashMap<>();
    private final Map<String, Fail> failMap = new HashMap<>();
    private Map<String, Function> functionMap = new HashMap<>();

    protected StepsStack(Construct scope, String id) {
        super(scope, id);
    }

    protected void mergeFunctionMap(Map<String, Function> functionMap) {
        this.functionMap.putAll(functionMap);
    }

    protected Fail fail(String error) {
        return failMap.computeIfAbsent(error, n -> Fail.Builder.create(this, n).error(n).build());
    }

    protected TaskStateBase f(String name) {
        return f(name, "$", "$");
    }

    protected TaskStateBase f(String name, String resultPath) {
        return f(name, "$", resultPath);
    }

    protected TaskStateBase fInput(String name, String inputPath) {
        return f(name, inputPath, "$");
    }

    protected TaskStateBase f(String name, String inputPath, String resultPath) {
        return f(name, inputPath, resultPath, null);
    }

    protected TaskStateBase f(String name, Map<String, Object> parameters) {
        return f(name, "$", "$", parameters);
    }

    protected TaskStateBase f(String name, String resultPath, Map<String, Object> parameters) {
        return f(name, "$", resultPath, parameters);
    }

    protected TaskStateBase f(String name, String inputPath, String resultPath, Map<String, Object> parameters) {
        return fTask(name, name + "Task", inputPath, resultPath, parameters);
    }

    protected TaskStateBase fDiscard(String name) {
        return f(name, "$", JsonPath.DISCARD);
    }

    protected TaskStateBase fDiscard(String name, Map<String, Object> parameters) {
        return f(name, "$", JsonPath.DISCARD, parameters);
    }

    protected TaskStateBase fTaskDiscard(String name, String taskName) {
        return fTask(name, taskName, "$", JsonPath.DISCARD, null);
    }

    protected TaskStateBase fTaskDiscard(String name, String taskName, String inputPath) {
        return fTask(name, taskName, inputPath, JsonPath.DISCARD, null);
    }

    protected TaskStateBase fTaskDiscard(String name, String taskName, Map<String, Object> parameters) {
        return fTask(name, taskName, "$", JsonPath.DISCARD, parameters);
    }

    protected TaskStateBase fTask(String name, String taskName, String resultPath) {
        return fTask(name, taskName, "$", resultPath, null);
    }

    protected TaskStateBase fTask(
            String name,
            String taskName,
            String inputPath,
            String resultPath) {
        return fTask(name, taskName, inputPath, resultPath, null);
    }

    protected TaskStateBase fTask(
            String name,
            String taskName,
            String resultPath,
            Map<String, Object> parameters) {
        return fTask(name, taskName, "$", resultPath, parameters);
    }

    protected TaskStateBase fTask(
            String name,
            String taskName,
            String inputPath,
            String resultPath,
            Map<String, Object> parameters) {
        return fTask(name, taskName, inputPath, resultPath, parameters, "$");
    }

    protected TaskStateBase fTask(
            String name,
            String taskName,
            String inputPath,
            String resultPath,
            Map<String, Object> parameters,
            String outputPath) {
        Objects.requireNonNull(functionMap.get(name), "Unable to find lambda " + name);
        return stateMap.computeIfAbsent(taskName,
                n -> LambdaInvoke.Builder.create(this, n)
                        .lambdaFunction(functionMap.get(name))
                        .inputPath(inputPath)
                        .resultPath(resultPath)
                        .outputPath(outputPath)
                        .payloadResponseOnly(true)
                        .payload(parameters == null ? null : TaskInput.fromObject(parameters))
                        .build());
    }

    protected EcsRunTask fargate(String name, EcsRunTask fargateTask) {
        return fargateMap.computeIfAbsent(name, n -> fargateTask);
    }
}
