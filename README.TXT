StrutsTestCase for JUnit - release 1.8
--------------------------------------

This release introduces improved support for Struts 1.1,
provides several requested enhancements, and fixes many
reported defects.

The StrutsTestCase library is now available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

The StrutsTestCase library supports Struts 1.1b1 and the
latest nightly builds, as well as Cactus v1.3 and JUnit
v3.7.

Change Log
==========

New Features:
------------

- Improved support for Struts 1.1.  This release provides a
major refactoring of MockStrutsTestCase and CactusStrutsTestCase
to improve support for 1.1 features, including fixes for
reported problems with DynaActionForm and Tiles features,
and calling the appropriate Action perform or execute method.
(MockStrutsTestCase and CactusStrutsTestCase)

NOTE: This refactoring removes or deprecates the following
methods in CactusStrutsTestCase:

  get/setActionForm have been removed.
  setFailIfInvalid has been deprecated - it no longer
  affects the flow of control, and will be removed in
  a future release.

- Added addRequestParameter() method in that takes in a String
array. (MockStrutsTestCase only)

- Changed logging mechanism to use the Commons logging package,
as Struts does.  (MockStrutsTestCase only)

- Implemented the following Mock Servlet methods:

  ServletContextSimulator: setInitParameter()
  HttpSessionSimulator: getServletContext()
  HttpResponseSimulator: setDateHeader(), addDateHeader(),
    addCookie(), findCookie()

Bug Fixes:
---------

- Fixed bug where where calling setWrapper methods was not
checking to see if the wrapper passed in already had a valid
request or response. (MockStrutsTestCase and CactusStrutsTestCase)

- Fixed bug where verifyInputForward() would fail with a
NullPointerException if the input mapping was not defined.
(MockStrutsTestCase and CactusStrutsTestCase)

- Fixed a bug where context path was not being added to verify
forward paths. (MockStrutsTestCase and CactusStrutsTestCase)


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org