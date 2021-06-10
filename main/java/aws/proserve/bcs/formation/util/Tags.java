// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package aws.proserve.bcs.formation.util;

import software.amazon.awscdk.core.CfnTag;
import software.amazon.awscdk.core.Tag;

public class Tags {

    public static Tag name(String name) {
        return new Tag("Name", name);
    }

    public static CfnTag cfnName(String name) {
        return CfnTag.builder().key("Name").value(name).build();
    }
}
