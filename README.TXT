StrutsTestCase for JUnit - release 2.1.1
----------------------------------------

This release resolves outstanding issues with Struts 1.1.

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
versions of WebLogic.




Deryl Seale
http://strutstestcase.sourceforge.net
