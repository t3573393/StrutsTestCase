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

- StrutsTestCase is now distributed under the Apache Software Foundation license.
This change comes in advance of StrutsTestCase's inclusion in the Struts 1.2
development road map.

- Added setActionForm() method for setting an ActionForm instance to be used in
a unit test, allowing for setting complex objects in an ActionForm.  It's important
to note that this method merely makes the ActionForm instance available for the
unit test; it does not subvert how Struts manages the ActionForm for a given Action.
In particular, if your ActionForm class overrides the reset() method, then you could
potentially reset anything you've set in the ActionForm instance you pass to this
method.

Bug Fixes:
---------

- Fixed bug where sub-application requests were not properly validated,
resulting in a NullPointerException.  (MockStrutsTestCase and CactusStrutsTestCase)

- Fixed bug where calls to HttpServletRequestSimulator.setAttribute() would
throw a NullPointerException if an argument was null.


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org