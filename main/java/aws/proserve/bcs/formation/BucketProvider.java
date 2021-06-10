// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation;

import software.amazon.awscdk.services.s3.Bucket;

@FunctionalInterface
public interface BucketProvider {

    Bucket getBucket();
}
