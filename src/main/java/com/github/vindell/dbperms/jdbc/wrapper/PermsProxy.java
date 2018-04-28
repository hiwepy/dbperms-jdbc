/*
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
package com.github.vindell.dbperms.jdbc.wrapper;

public interface PermsProxy {

	/**
	   * Returns the underlying object for the proxy.
	   * <p>
	   * WARNING: This is an internal method for dbperms.  This method should not be called directly.  Use the methods
	   * on the {@link java.sql.Wrapper} interface instead!
	   * @return the underlying object being proxied
	   */
	  Object unwrapProxy();
	
}
