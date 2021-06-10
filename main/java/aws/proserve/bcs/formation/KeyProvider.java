// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation;

import software.amazon.awscdk.services.kms.Key;

@FunctionalInterface
public interface KeyProvider {

    Key getKey();
}
