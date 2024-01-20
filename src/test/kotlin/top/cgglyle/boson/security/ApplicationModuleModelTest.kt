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

package top.cgglyle.boson.security

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import org.springframework.modulith.docs.Documenter.DiagramOptions

/**
 * @author: Lyle Liu
 */
class ApplicationModuleModelTest {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val application = ApplicationModules.of(BosonSecurityApplication::class.java)

    @Test
    fun createApplicationModuleModel() {
        application.forEach {
            logger.info(it.toString())
        }
        val withStyle = DiagramOptions.defaults()
            .withStyle(DiagramOptions.DiagramStyle.UML)
        Documenter(application)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml(
                withStyle
            )
    }

    @Test
    fun verify() {
        application.verify()
    }
}