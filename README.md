# HearthstoneBuilder

This example illustrates a common usage of the DrawerLayout widget in the Android
support library.

Pre-requisites
--------------

- Android SDK v23
- Android Build Tools v23.0.1
- Android Support Repository

Background
---------------

This is a project for CIS350: Software Engineering.

### Generating Test Reports ###

1. Place new test files under `src/androidTest/${dir}/java`
2. Start up an Android device to test
3. Use Gradle from the command line and enter the following, which will install the debug build onto the device:  
<pre><code>./gradlew installDebugAndroidTest</code></pre>
4. Use Gradle from the command line to generate the coverage report:
<pre><code>./gradlew createDebugCoverageReport</code></pre>   
Your report will be complete if you see the following:   
<pre><code>:app:createDebugAndroidTestCoverageReport
:app:createDebugCoverageReport
BUILD SUCCESSFUL</code></pre>
5. Go to `app/build/reports/..` to view the individual outputs from index.html files.

Contributors
-----------
* Chad Teitsma
* Nathan Allvin
* Josiah Campbell

License
-------

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
