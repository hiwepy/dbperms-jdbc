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
package com.github.vindell.dbperms.jdbc;

import java.util.Set;

public interface PermsOptionsMBean {
	
	String getDriverlist();

	void setDriverlist(String driverlist);

	Set<String> getDriverNames();

	void setJNDIContextFactory(String jndicontextfactory);

	String getJNDIContextFactory();

	void unSetJNDIContextFactory();

	void setJNDIContextProviderURL(String jndicontextproviderurl);

	void unSetJNDIContextProviderURL();

	String getJNDIContextProviderURL();

	void setJNDIContextCustom(String jndicontextcustom);

	void unSetJNDIContextCustom();

	String getJNDIContextCustom();

	void setRealDataSource(String realdatasource);

	void unSetRealDataSource();

	String getRealDataSource();

	void setRealDataSourceClass(String realdatasourceclass);

	void unSetRealDataSourceClass();

	String getRealDataSourceClass();

	void setRealDataSourceProperties(String realdatasourceproperties);

	void unSetRealDataSourceProperties();

	String getRealDataSourceProperties();

	boolean getJmx();

	void setJmx(boolean jmx);

	String getJmxPrefix();

	void setJmxPrefix(String jmxPrefix);

}
