StrutsTestCase for JUnit - release 1.9.6
----------------------------------------

This is a point release that resolves outstanding issues
with Struts 1.1b3 and module support.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

The StrutsTestCase library supports Struts 1.1b3 and the
latest nightly builds, as well as Cactus v1.4.1 and JUnit
v3.8.  PLEASE NOTE THAT THIS LATEST RELEASE WILL NOT WORK
WITH STRUTS 1.1B2!

Change Log
==========

Bug Fixes:
---------

- Fixed a bug where verifyForward() would not run module aware
comparisons.  (CactusStrutsTestCase and MockStrutsTestCase)

- Fixed a bug where actions and forwards not prefixed with a '/'
would not verify correctly.  (CactusStrutsTestCase and MockStrutsTestCase)


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org
