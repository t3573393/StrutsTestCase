StrutsTestCase for JUnit - release 2.1.0
----------------------------------------

This release resolves outstanding issues with Struts 1.1.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and the 1.1 version of the Struts
Framework.

Change Log
==========

New Features:
------------

- The StrutsTestCase library is no longer backwards compatible
with Struts 1.0, and as such, has removed all deprecated references.
The last release to support Struts 1.0 is StrutsTestCase v2.0.

- Greatly improved error messages for common cases such as missing
configuration files.  As well, all exceptions that are thrown in
a test will be passed directly to the the test case, and will include
the full exception stack trace for debugging.  Many thanks to Sean
Pritchard for his excellent work on this.  (MockStrutsTestCase and
CactusStrutsTestCase)

- Added no argument constructors, which is now supported in
the latest JUnit release. (MockStrutsTestCase and
CactusStrutsTestCase)

- Made getActualForward() a protected method, so that it can
be used in test cases. (MockStrutsTestCase and
CactusStrutsTestCase)

Bug Fixes:
---------

- Fixed a bug where the jsessionid was not properly stripped
if it was longer than 44 characters. (MockStrutsTestCase and
CactusStrutsTestCase)

- Fixed NullPointerException when using OC4J (patch 831139 for
CactusStrutsTestCase)

- Added patch to fix problem where running Cactus tests renders
a web application unusable.  NOTE: This is still a work in progress,
so the fix may work for you, or it may not.  (CactusStrutsTestCase
only)


Deryl Seale
http://strutstestcase.sourceforge.net
