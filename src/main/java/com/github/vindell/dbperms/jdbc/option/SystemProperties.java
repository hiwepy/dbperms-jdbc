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
package com.github.vindell.dbperms.jdbc.option;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class SystemProperties implements PermsOptionsSource {

	public static final String DBPERMS_PREFIX = "dbperms.config.";

	@Override
	public Map<String, String> getOptions() {
		final Map<String, String> result = new HashMap<String, String>();

		for (Entry<Object, Object> entry : new HashSet<Entry<Object, Object>>(
				((Properties) System.getProperties().clone()).entrySet())) {
			final String key = entry.getKey().toString();
			if (key.startsWith(DBPERMS_PREFIX)) {
				result.put(key.substring(DBPERMS_PREFIX.length()), (String) entry.getValue());
			}
		}

		return result;
	}
}