StrutsTestCase for JUnit - release 1.7
--------------------------------------

This release introduces support for Struts 1.1, provides
several requested enhancements, and fixes many reported
defects.

The StrutsTestCase library is now available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

Change Log
==========

New Features:
------------

- StrutsTestCase now supports both Struts 1.1 and Struts
1.0.x in two distinct distributions, and has been fully tested
against the 1.1b1 milestone release.  This requires all
Struts dependency libraries to be in your CLASSPATH,
including commons-beanutils.jar, commons-collections.jar,
commons-digester.jar, and commons-logging.jar.

- StrutsTestCase now supports both Servlet 2.2 and 2.3 specs
in two distinct distributions.

- Added setServletConfigFile() method for loading web.xml files, 
and setting ServletConfig and ServletContext parameters accordingly.  
Supports both init-param and context-param tags. (MockStrutsTestCase
only)

- Added setFailIfInvalid() method to allow overriding default behavior
of failing a test case if ActionForm validation fails.  
(CactusStrutsTestCase only)

- Added explicit checks to ensure that the base class setUp() method
has been called, and failing test cases when it has not.  Previously,
sub-classes that overrode setUp() without calling super.setUp() were
subject to confusing errors, and so proper initialization is now
enforced.

- Un-deprecated getActionServlet() in MockStrutsTestCase, and
added the same in CactusStrutsTestCase.  This can be useful for
complex tests.

- Added setInitParameter() method to set ActionServlet
initialization parameters.  (MockStrutsTestCase only)

- Implemented the following Mock Servlet methods:

  ServletContextSimulator: setInitParameter()
  HttpSessionSimulator: getServletContext()
  HttpResponseSimulator: setStatus(), reset(), sendRedirect(), 
   encodeURL(), encodeRedirectURL(), addHeader(), setHeader(), 
   containsHeader(), addIntHeader(), setIntHeader()

Bug Fixes:
---------

- Fixed bug where an action forward declared as a redirect
(ie: redirect="true" in struts-config.xml) was causing
a NullPointerException.  (MockStrutsTestCase only)

- Fixed bug where request URL's with jessionid appended were
failing the validateForward() method.  This portion of the
URL is now stripped before validation.

- Fixed bug where validating an Action without an associated
ActionForm caused a NullPointerException.  (CactusStrutsTestCase
only)

- Fixed bug where validating an action that is a simple
forward (and so does not have an associated Action class)
throws a NullPointerException.  (CactusStrutsTestCase only)

- Removed tearDown() methods.  They were not useful and 
sometimes caused spurious errors.



Questions and comments are always welcome!

Deryl Seale - deryl@acm.org