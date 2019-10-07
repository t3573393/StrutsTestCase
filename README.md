# StrutsTestCase for JUnit v2.1.4

### Now supporting Struts 1.2 and 1.3, including [Tiles](#tiles) and [Sub-Applications!](#subapp)

Questions? Comments? Check out the [user forums](http://sourceforge.net/forum/?group_id=39190).

## What is it?

StrutsTestCase for JUnit is an extension of the standard JUnit TestCase class that provides facilities for testing code based on the Struts framework. StrutsTestCase provides both a [Mock Object approach](api/servletunit/package-summary.html) and a [Cactus approach](http://jakarta.apache.org/cactus) to actually run the Struts ActionServlet, allowing you to test your Struts code with or without a running servlet engine. Because StrutsTestCase uses the ActionServlet controller to test your code, you can test not only the implementation of your Action objects, but also your mappings, form beans, and forwards declarations. And because StrutsTestCase already provides validation methods, it's quick and easy to write unit test cases.

StrutsTestCase is compliant with the Java Servlet 2.2, 2.3, and 2.4 specifications, and supports Struts 1.2/1.3, and Cactus 1.7 and JUnit 3.8.1.

**Please note** that StrutsTestCase is no longer backwards compatible with Struts 1.0. The last release compatible with Struts 1.0 is [StrutsTestCase v2.0](http://sourceforge.net/project/showfiles.php?group_id=39190).

## Mock Testing vs. In-Container Testing

There are two popular approaches to testing server-side classes: **mock objects**, which test classes by simulating the server container, and **in-container testing**, which tests classes running in the actual server container. StrutsTestCase for JUnit allows you to use either approach, with very minimal impact on your actual unit test code. In fact, because the StrutsTestCase setup and validation methods are exactly the same for both approaches, choosing one approach over the other simply effects which base class you use!

StrutsTestCase for JUnit provides two base classes, both of which are extensions of the standard JUnit `TestCase`. [`MockStrutsTestCase`](api/servletunit/struts/MockStrutsTestCase.html) uses a set of `HttpServlet` mock objects to simulate the container environment without requiring a running servlet engine. [`CactusStrutsTestCase`](api/servletunit/struts/CactusStrutsTestCase.html) uses the [Cactus testing framework](http://attic.apache.org/projects/jakarta-cactus.html) to test Struts classes in the actual server container, allowing for a testing environment more in line with the actual deployment environment.

**Please note** that while the following examples use the `MockStrutsTestCase` approach, you could choose to use the Cactus approach by simply subclassing from `CactusStrutsTestCase` without changing another line of code!

## How does it work?

**Please note** that the StrutsTestCase distribution comes with these and other examples, as well as step-by-step instructions for running them -- see the README file in the examples directory for more details. As well, [there are many more methods](api/index.html) in the StrutsTestCase library than are illustrated here -- check out the javadocs for a complete picture of what you can do with StrutsTestCase!

Using the popular cookbook approach, let's consider the following code snippet:

```java
public class LoginAction extends Action {

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    {

        String username = ((LoginForm) form).getUsername();
        String password = ((LoginForm) form).getPassword();

        ActionErrors errors = new ActionErrors();

        if ((!username.equals("deryl")) || (!password.equals("radar")))
            errors.add("password",new ActionError("error.password.mismatch"));

        if (!errors.empty()) {
            saveErrors(request,errors);
            return mapping.findForward("login");
        }

        // store authentication info on the session
        HttpSession session = request.getSession();
        session.setAttribute("authentication", username);

        // Forward control to the specified success URI
        return mapping.findForward("success");

}
```

So, what are we doing here? Well, we receive an `ActionForm bean` which should contain login information. First, we try to get the username and password information, and then check to see if it is valid. If there is a mismatch in the username or password values, we then create an `ActionError` message with a key to a message catalogue somewhere, and then try to forward to the login screen so we can log in again. If the username and password match, however, we store some authentication information in the session, and we try to forward to the next page.

There are several things we can test here:

*   Does the `LoginForm` bean work properly? If we place the appropriate parameters in the request, does this bean get instantiated correctly?
*   If the username or password doesn't match, do the appropriate errors get saved for display on the next screen? Are we sent back to the login page?
*   If we supply the correct login information, do we get to the correct page? Are we sure there are no errors reported? Does the proper authentication information get saved in the session?

StrutsTestCase gives you the ability to test all of these conditions within the familiar JUnit framework. All of the Struts setup -- which really amounts to starting up the `ActionServlet` -- is taken care of for you.

So, how do we actually do it? Let's start by creating an empty test case, which we extend from the base StrutsTestCase class:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public void setUp() { super.setUp(); }

    public void tearDown() { super.tearDown(); }

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {}
}
```

**NOTE:** If you choose to override the `setUp()` method, you **must** explicitly call `super.setUp()`. This method performs some important initialization routines, and StrutsTestCase will not work if it is not called.

The first thing we need to do is to tell Struts which mapping to use in this test. To do so, we specify a path that is associated with a Struts mapping; this is the same mechanism that the Struts tag library method uses.

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
    }
}
```

**NOTE:** By default, the Struts `ActionServlet` will look for the file `WEB-INF/struts-config.xml`, so you must place the directory that _contains_ `WEB-INF` in your `CLASSPATH`. If you would like to use an alternate configuration file, please see the [`setConfigFile()`](api/servletunit/struts/MockStrutsTestCase.html#setConfigFile(java.lang.String)) method for details on how this file is located.

Next we need to pass form bean properties, which we send via the request object (again, just as Struts does):

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
    }
}
```

Finally, we need to get the `Action` to do its thing, which just involves executing the `actionPerform` method:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
    }
}
```

That's all you have to do to get the `ActionServlet` to process your request, and if all goes well, then nothing will happen. But we're not done yet -- we still need to verify that everything happened as we expected it to. First, we want to make sure we got to the right page:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyForward("success");
    }
}
```

It's worth noting here that when you verify which page you ended up at, you can use the Struts forward mapping. You don't have to hard code filenames -- the StrutsTestCase framework takes care of this for you. Thus, if you were to change where "success" pointed to, your tests would still work correctly. All in the spirit of Struts.

Next, we want to make sure that authentication information was stored properly:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyForward("success");
       assertEquals("deryl",(String) getSession().getAttribute("authentication"));
    }
}
```

Here we're getting the session object from the request, and checking to see if it has the proper attribute and value. You could just as easily place an object on the session that your `Action` object expects to find. All of the servlet classes available in the StrutsTestCase base classes are fully functioning objects.

Finally, we want to make sure that no `ActionError` messages were sent along. We can use a built in method to make sure of this condition:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setRequestPathInfo("/login");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyForward("success");
       assertEquals("deryl",(String) getSession().getAttribute("authentication"));
       verifyNoActionErrors();
    }
}
```

So, now that we've written one test case, it's easy to write another. For example, we'd probably want to test the case where a user supplies incorrect login information. We'd write such a test case like the following:

```java
public void testFailedLogin() {

    addRequestParameter("username","deryl");
    addRequestParameter("password","express");
    setRequestPathInfo("/login");
    actionPerform();
    verifyForward("login");
    verifyActionErrors(new String\[\] {"error.password.mismatch"});
    assertNull((String) getSession().getAttribute("authentication"));
}
```

Now, this looks quite similar to our first test case, except that we're passing incorrect information. Also, we're checking to make sure we used a different forward, namely one that takes us back to the login page, and that the authentication information is _not_ on the session.

We're also verifying that the correct error messages were sent. Note that we used the symbolic name, not the actual text. Because the `verifyActionErrors()` method takes a `String` array, we can verify more than one error message, and StrutsTestCase will make sure there is an exact match. If the test produced more error messages than we were expecting, it will fail; if it produced fewer, it will also fail. Only an exact match in name and number will pass.

It's that easy! As you can see, StrutsTestCase not only tests the implementation of your `Action` objects, but also the mappings that execute them, the `ActionForm` beans that are passed as arguments, and the error messages and forward statements that result from execution. It's the whole enchilada!

## Testing Tiles in Struts 1.2

The Tiles framework, which is integrated into Struts 1.2, is a flexible templating mechanism designed to easily re-use common user experience elements. StrutsTestCase now provides support for testing applications that use Tiles, allowing any test case to verify that an `Action` object uses the correct Tiles definition. Tiles testing is similar to calling `verifyActionForward`, with a twist:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setConfigFile("/WEB-INF/struts-config.xml");
       setRequestPathInfo("/login.do");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyTilesForward("success","success.tiles.def");
    }
}
```

This is similar to our previous test cases, except that we're additionally passing in a definition name to verify that this `Action` uses a given Tiles definition when resolving the expected forward. If it uses a different Tiles definition, or if the expected definition does not exist, then this test will fail. Additionally, you can call `verifyInputTilesForward` to verify that an `Action` uses an input mapping, and that input mapping is the expected Tiles definition:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setConfigFile("/WEB-INF/struts-config.xml");
       setRequestPathInfo("/login.do");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyInputTilesForward("success.tiles.def");
    }
}
```

## Testing Sub-Applications in Struts 1.2

Struts 1.2 introduces the concept of sub-applications, or modules -- a powerful mechanism for dividing an application into functional components. StrutsTestCase now provides support for testing sub-applications, which extends the concepts discussed in the previous examples. The general idea is still the same: you can point a unit test to a configuration file, execute an action, and validate the results. The methods for setting the configuration file and executing an action are a little different, however, as the following example shows:

```java
public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) { super(testName); }

    public void testSuccessfulLogin() {
       setConfigFile("mymodule","/WEB-INF/struts-config-mymodule.xml");
       setRequestPathInfo("/mymodule","/login.do");
       addRequestParameter("username","deryl");
       addRequestParameter("password","radar");
       actionPerform();
       verifyForward("success");
       assertEquals("deryl",(String) getSession().getAttribute("authentication"));
       verifyNoActionErrors();
    }
}
```

As you can see, this looks very similar to our other test cases. The first important difference is in setting the configuration file. Here, we set the files _and_ associate this configuration file with a given sub-application name. This allows the StrutsTestsCase library to pass this configuration information so that the `ActionServlet` can correctly identify the sub-application. Note that the same rules apply to setting the `CLASSPATH` to locate the configuration file as those mentioned above.

The other important difference is in setting the request path information. Here, we set not only the path information, but also the sub-application names. The combination of these two parameters is equivalent to the entire request path; in our test case above, this would be equivalent to a path like this: `/mymodule/login.do`. It is important to note that when using this method, the first argument _must contain only the sub-application name_, and the second argument must contain the rest of the path _not including the sub-application name_. Otherwise, the request path will be incorrectly constructed, resulting in a spurious test failure.

Copyright (C) 2004 [Deryl Seale](mailto:deryl.at.acm.org)
