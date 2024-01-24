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

package top.cgglyle.boson.security.authentication.domain

import org.mapstruct.*
import top.cgglyle.boson.security.authentication.LocalAuthDto

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class LocalAuthEntityMapper {

    abstract fun toEntity(localAuthDto: LocalAuthDto): LocalAuthEntity

    abstract fun toDto(localAuthEntity: LocalAuthEntity): LocalAuthDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(
        localAuthDto: LocalAuthDto,
        @MappingTarget localAuthEntity: LocalAuthEntity
    ): LocalAuthEntity
}