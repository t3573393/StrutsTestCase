How to run the MockStrutsTestCase examples
------------------------------------------
In order to run the tests, you must have the following libraries in your CLASSPATH:

junit.jar
servlet.jar
struts.jar
commons-digester.jar
commons-collections.jar
commons-logging.jar
commons-beanutils.jar
jaxp.jar
crimson.jar
strutstest.jar

Additionally, you must set the CLASSPATH to point to the 'examples' directory, so that StrutsTestCase can find the
struts-config.xml file.

You can run the TestLoginAction class directly, as its main method invokes the JUnit TestRunner:

% java examples.TestLoginAction

.ActionServlet: init
[INFO] RequestProcessor - -Processing a 'POST' for path '/login'
.ActionServlet: init
[INFO] RequestProcessor - -Processing a 'POST' for path '/login'

Time: 1.292

OK (2 tests)

How to run the CactusStrutsTestCase examples
--------------------------------------------

If you are going to run the Cactus tests, you must have the following libraries in your CLASSPATH, in addition to the
ones described above:

cactus.jar
commons-httpclient.jar
aspectjrt.jar (only if you are using Cactus v1.3 or later)

Additionally, you must set the CLASSPATH to point to the 'examples/cactus' directory, so that the Cactus test harness
can find the cactus.properties file.

Finally, you must have your servlet engine running, with the example web application, examples/test.war, deployed.
Usually this can be done by dropping test.war in your servlet engine's webapps directory and starting it up.  Note that
for this example, your servlet engine *MUST BE RUNNING ON PORT 8080*.  Also note that CLASSPATH issues can be
particularly tricky when running a servlet engine, most especially when it comes to XML libraries.  To be on the safe
side, make sure your CLASSPATH is unset when starting your servlet engine, so as to avoid any class loading conflicts.
Of course, don't forget to reset your CLASSPATH as decribed above before running the tests.

You can run the TestCactusLoginAction class directly, as its main method invokes the JUnit TestRunner:

% java examples.cactus.TestCactusLoginAction

..
Time: 1.502

OK (2 tests)
