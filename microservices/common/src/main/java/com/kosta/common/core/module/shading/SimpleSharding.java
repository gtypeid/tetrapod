package com.kosta.common.core.module.shading;

import com.kosta.common.core.module.shading.abs.ShardingMatcher;

public class SimpleSharding implements ShardingMatcher {

    @Override
    public int getShardingIndex(Long id) {
        int numBuckets = 2;
        return (int)(id % numBuckets);
    }
}
