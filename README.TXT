StrutsTestCase for JUnit - release 2.1.2
----------------------------------------

This is a point release to resolve a number of outstanding bugs.

The StrutsTestCase library is available as two distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and the 1.1 version of the Struts
Framework. PLEASE NOTE that StrutsTestCase no longer supports
the Struts 1.0 release.

Change Log
==========

New Features:
------------

- The StrutsTestCase library is no longer backwards compatible
with Struts 1.0, and as such, has removed all deprecated references.
The last release to support Struts 1.0 is StrutsTestCase v2.0.

- Added ability to fully process a request when using CactusStrutsTestCase,
which allows for validating results using tools such as HTTPUnit (bug #846322).
By default, CactusStrutsTestCase will not fully process the resulting forward
from the Action being tested, in order to minimize side effects that can
produce spurious results.  By calling CactusStrutsTestCase.processRequest(),
callers can change this behavior such that the request is fully processed
by the container.  (CactusStrutsTestCase only)

Bug Fixes:
---------

- Fixed bug (#790530) where web applications would no longer run after
running tests.  Note that you must call the CactusStrutsTestCase.tearDown
method in order to restore the web application to its functioning state.
(CactusStrutsTestCase only)

- Fixed bug (#849772) where NullPointerException was thrown in certain
versions of WebLogic.  (CactusStrutstestCase only)

- Fixed a bug (#968270) where visibility of setup() and tearDown() methods
was inconistent with the JUnit library.

- Fixed a bug (#949591) where Location header was being preserved between
actionPerform() calls.  (MockStrutsTestCase only)

- Fixed a bug (#940262) where the HttpServletRequestSimulator.getParameter()
method would throw a NullPointerException if the requested key was null.
(MockStrutsTestCase only)

- Fixed a bug (#923197) where calling HttpServletRequestSimulator.getRequestDispacther()
would throw a NullPointerException.
(MockStrutsTestCase only)

- Fixed a bug (#889946) where parameters added through addRequestParameter() would
not be available from getParameterMap().  (CactusStrutstestCase only)

- Fixed a bug (#871307) where verifyActionErrors() would occassionally pass when it
really should fail.  (Both CactusStrutsTestCase and MockStrutsTestCase)

- Fixed a bug (#868716) where some build parameters were not clearly marked in the
build.xml file.

- Fixed a bug (#821477) where StrutsTestCase classes were not easily extendable; now
all methods are protected.  (Both CactusStrutsTestCase and MockStrutsTestCase)

- Fixed a bug (#821326) where verifyForward() would sometimes fail to add the correct
context path.  (Both CactusStrutsTestCase and MockStrutsTestCase)

- Fixed a bug (#758161) where Struts resources were not being properly cleaned up after test
execution.


Deryl Seale
http://strutstestcase.sourceforge.net
