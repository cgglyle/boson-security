/*
 * Copyright 2024 Lyle Liu<cgglyle@outlook.com> and all contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.cgglyle.boson.security.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author: Lyle Liu
 */
@Configuration
class SpringdocConfig(
    @Value("\${version}")
    val version: String
) {

    @Bean
    fun springdocOpenApi(): OpenAPI {
        val openAPI = OpenAPI()
        openAPI
            .info = Info().title("Boson IAM")
            .description("Boson Identity and Access Management API")
            .version(version)
            .license(License().name("Apache-2.0 license").url("https://www.apache.org/licenses/LICENSE-2.0"))
        return openAPI
    }

    @Bean
    fun bosonSecurityApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("Boson API")
            .pathsToMatch("/**")
            .build()
    }
}