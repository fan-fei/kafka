package com.nordsoft.consumer;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class dd {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Snowflake snowflake = IdUtil.createSnowflake(0, 0);
            System.out.println(snowflake.nextId());
        }
    }
}

