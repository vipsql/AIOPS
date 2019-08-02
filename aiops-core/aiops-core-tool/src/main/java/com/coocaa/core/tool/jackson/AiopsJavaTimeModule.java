/**
 * Copyright (c) 2018-2028, DreamLu 卢春梦 (qq596392912@gmail.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coocaa.core.tool.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.coocaa.core.tool.utils.DateTimeUtil;

import java.time.*;

/**
 * java 8 时间默认序列化
 *
 * @author L.cm
 */
public class AiopsJavaTimeModule extends SimpleModule {

	public AiopsJavaTimeModule() {
		super(PackageVersion.VERSION);
		this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeUtil.DATETIME_FORMAT));
		this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeUtil.DATE_FORMAT));
		this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeUtil.TIME_FORMAT));
		this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeUtil.DATETIME_FORMAT));
		this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeUtil.DATE_FORMAT));
		this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeUtil.TIME_FORMAT));
	}

}
