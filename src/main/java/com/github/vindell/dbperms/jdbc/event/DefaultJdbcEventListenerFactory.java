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
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Default {@link JdbcEventListenerFactory} implementation providing all the
 * {@link JdbcEventListener}s supplied by the {@link P6Factory}ies as well as
 * those registered by {@link ServiceLoader}s.
 * 
 * @author Peter Butkovic
 * @since 3.3.0
 *
 */
public class DefaultJdbcEventListenerFactory implements JdbcEventListenerFactory {

	private static ServiceLoader<JdbcEventListener> jdbcEventListenerServiceLoader = //
			ServiceLoader.load(JdbcEventListener.class, DefaultJdbcEventListenerFactory.class.getClassLoader());

	private static JdbcEventListener jdbcEventListener;

	@Override
	public JdbcEventListener createJdbcEventListener() {
		if (jdbcEventListener == null) {
			synchronized (DefaultJdbcEventListenerFactory.class) {
				if (jdbcEventListener == null) {
					CompoundJdbcEventListener compoundEventListener = new CompoundJdbcEventListener();
					compoundEventListener.addListender(DefaultEventListener.INSTANCE);
					registerEventListenersFromServiceLoader(compoundEventListener);
					jdbcEventListener = compoundEventListener;
				}
			}
		}

		return jdbcEventListener;
	}

	public void clearCache() {
		jdbcEventListener = null;
	}

	protected void registerEventListenersFromServiceLoader(CompoundJdbcEventListener compoundEventListener) {
		for (Iterator<JdbcEventListener> iterator = jdbcEventListenerServiceLoader.iterator(); iterator.hasNext();) {
			try {
				compoundEventListener.addListender(iterator.next());
			} catch (ServiceConfigurationError e) {
				e.printStackTrace();
			}
		}
	}

}
