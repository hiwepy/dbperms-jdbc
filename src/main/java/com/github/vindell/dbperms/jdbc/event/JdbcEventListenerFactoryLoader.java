/**
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.vindell.dbperms.jdbc.event;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Loads the {@link JdbcEventListenerFactory} from a {@link ServiceLoader} or
 * gets the {@link DefaultJdbcEventListenerFactory}.
 */
public class JdbcEventListenerFactoryLoader {

	private static final JdbcEventListenerFactory jdbcEventListenerFactory;

	static {
		final Iterator<JdbcEventListenerFactory> iterator = ServiceLoader
				.load(JdbcEventListenerFactory.class, JdbcEventListenerFactory.class.getClassLoader()).iterator();
		if (iterator.hasNext()) {
			jdbcEventListenerFactory = iterator.next();
		} else {
			jdbcEventListenerFactory = new DefaultJdbcEventListenerFactory();
		}
	}

	private JdbcEventListenerFactoryLoader() {
	}

	public static JdbcEventListenerFactory load() {
		return jdbcEventListenerFactory;
	}

}
