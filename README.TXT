StrutsTestCase for JUnit - release 1.9
--------------------------------------

This release introduces improved support for Struts 1.1b2,
provides several requested enhancements, and fixes many
reported defects.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

The StrutsTestCase library supports Struts 1.1b2 and the
latest nightly builds, as well as Cactus v1.4.1 and JUnit
v3.8.

Change Log
==========

New Features:
------------

- Added support for sub-applications in Struts 1.1.  A new setConfigFile()
method associates a configuration file with a given sub-application name,
and a new setRequestPathInfo() method allows validating actions associated
with a given sub-application. (MockStrutsTestCase and CactusTestCase)

- Added getActionForm() method to directly access form used by the action
being tested. (MockStrutsTestCase and CactusTestCase)

- Added more support for Tiles plugin.  Previously, StrutsTestCase was
relying on the deprecated ActionComponentServlet; now the code uses
more up-to-date mechanisms for verifying Tiles forwards. (MockStrutsTestCase
and CactusStrutsTestCase)

Bug Fixes:
---------

- Fixed bug that led to a ConcurrentModificationException when using
CactusTestCase with Tomcat 4.x. (CactusStrutsTestCase)

- Fixed bug that led to an UnsupportedOperationException when using
MockStrutsTestCase and Struts 1.1b2. (MockStrutsTestCase)

- Fixed bug in HttpServletRequestSimulator that sometimes returned
an invalid HttpSession instance. (MockStrutsTestCase)

- Fixed a compatibility error between CactusStrutsTestCase and the
Servlet 2.2 API. (CactusStrutsTestCase)

- Fixed bug whereby forwards were being executed, rather than reported
for validation, sometimes leading to a spurious IllegalStateException
or ClassCastException, or reporting errors for resources that did not
exist. (CactusStrutsTestCase)


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org