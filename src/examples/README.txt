How to run the MockStrutsTestCase examples
------------------------------------------
In order to run the tests, you must have the following libraries
in your CLASSPATH:

junit.jar
servlet.jar
struts.jar
commons-digester.jar
commons-collections.jar
commons-logging.jar
jaxp.jar
crimson.jar
strutstest.jar

Additionally, you must set the CLASSPATH to point to the 'examples'
directory, so that StrutsTestCase can find the struts-config.xml
file.

You can run the TestLoginAction class directly, as its main method
invokes the JUnit TestRunner:

% java examples.TestLoginAction

.ActionServlet: init
[INFO] RequestProcessor - -Processing a 'POST' for path '/login'
.ActionServlet: init
[INFO] RequestProcessor - -Processing a 'POST' for path '/login'

Time: 1.292

OK (2 tests)

How to run the CactusStrutsTestCase examples
--------------------------------------------

If you are going to run the Cactus tests, you must have the
following libraries in your CLASSPATH, in addition to the ones
described above:

cactus.jar
httpclient.jar
aspectjrt.jar (only if you are using Cactus v1.3)

Additionally, you must set the CLASSPATH to point to the 'examples/cactus'
directory, so that the Cactus test harness can find the cactus.properties
file.

Finally, you must have your Servlet engine running, with the test classes
contained in a web application with the appropriate support libraries (all
those mentioned above), and with configured using the web.xml provided.  See
http://jakarta.apache.org/cactus for more details.

You can run the TestCactusLoginAction class directly, as its main
method invokes the JUnit TestRunner:

% java examples.cactus.TestCactusLoginAction

..
Time: 1.502

OK (2 tests)
