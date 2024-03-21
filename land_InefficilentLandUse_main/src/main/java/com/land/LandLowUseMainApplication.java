/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.land;

import cn.stylefeng.roses.core.config.MybatisDataSourceAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot方式启动类
 *
 * @author stylefeng
 * @Date 2017/5/21 12:06
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication(exclude = {MybatisDataSourceAutoConfiguration.class})
public class LandLowUseMainApplication {

    private final static Logger logger = LoggerFactory.getLogger(LandLowUseMainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LandLowUseMainApplication.class, args);
        logger.info(LandLowUseMainApplication.class.getSimpleName() + " is success!");
    }

}
