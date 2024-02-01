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

package top.cgglyle.boson.security.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringBootVersion
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Lyle Liu
 */
@Service
class SystemInfoPrintfService(
    private val applicationContext: ApplicationContext,

    @Value("#{buildProperties.get('time')}")
    private val buildTime: Long,

    @Value("\${server.port}")
    private val localServerPort: String,

    @Value("\${spring.profiles.active}")
    private val profile: String,

    @Value("\${version}")
    private val version: String,
) : ApplicationRunner {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    override fun run(args: ApplicationArguments) {
        printBaseInfo()
    }

    private fun printBaseInfo() {
        val hostAddress: String = try {
            InetAddress.getLocalHost().hostAddress
        } catch (e: UnknownHostException) {
            log.warn("Could not get local address")
            "localhost"
        }


        val heardInfo =
            """
             
             +---------------------------------------------------------+
             |##################   SYSTEM STARTED   ###################|
             +---------------------------------------------------------+
             |>>> Spring boot version: [${SpringBootVersion.getVersion()}]
             |>>> Application version: [$version]
             |>>> Build date:          [${timestampToDate(buildTime)}]
             |>>> Startup date:        [${timestampToDate(applicationContext.startupDate)}]
             |>>> Profile:             [$profile]
             |>>> Port:                [$localServerPort]
             |>>> Java version:        [${System.getProperty("java.version")}]
             |>>> Java home:           [${System.getProperty("java.home")}]
             |>>> Java JVM version:    [${System.getProperty("java.vm.version")}]
             |>>> Java class version:  [${System.getProperty("java.class.version")}]
             |>>> OS:                  [${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${
                System.getProperty("os.version")
            }]
             +----------------------------------------------------------
        """.trimIndent()

        val finalInfo = if (profile != "prod") {
            val swaggerDoc =
                "Online doc url: [http://$hostAddress:$localServerPort/swagger-ui/index.html]"
            buildString {
                append(heardInfo).append("\n")
                append("|>>> ").append(swaggerDoc).append("\n")
                append("+----------------------------------------------------------\n")
            }
        } else {
            heardInfo
        }

        log.info(finalInfo)
    }

    private fun timestampToDate(buildTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS")
            .format(Date(buildTime))
    }
}