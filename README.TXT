StrutsTestCase for JUnit - release 1.9.1
----------------------------------------

This is a point release that resolves outstanding issues
with Struts 1.1b3, and provides other assorted enhancements
and bug fixes.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

The StrutsTestCase library supports Struts 1.1b3 and the
latest nightly builds, as well as Cactus v1.4.1 and JUnit
v3.8.

Change Log
==========

New Features:
------------

Bug Fixes:
---------

- Fixed bug where sub-application requests were not properly validated,
resulting in a NullPointerException.  (MockStrutsTestCase and CactusStrutsTestCase)

- Fixed bug where calls to HttpServletRequestSimulator.setAttribute() would
throw a NullPointerException if an argument was null.


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org