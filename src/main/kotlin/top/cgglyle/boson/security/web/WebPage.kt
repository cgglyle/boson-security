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

package top.cgglyle.boson.security.web

import org.springframework.data.domain.Page

data class WebPage<T>(
    val content: List<T?>,
    val pageNumber: Int,
    val pageSize: Int,
) {
    companion object {
        fun <T> form(page: Page<T>): WebPage<T> {
            if (page.isEmpty) {
                return newEmptyWebPage()
            }

            return WebPage(page.content, page.number + 1, page.totalPages)
        }

        private fun <T> newEmptyWebPage(): WebPage<T> {
            return WebPage(listOf(), 0, 0)
        }
    }
}
