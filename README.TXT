StrutsTestCase for JUnit - release 2.0.1
----------------------------------------

This release resolves outstanding issues with Struts 1.1.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

PLEASE NOTE THAT THIS LATEST RELEASE WILL ONLY WORK WITH
STRUTS 1.1B3 OR LATER RELEASES!

Change Log
==========

New Features:
------------

- added verifyTilesForward and verifyInputTilesForward methods to
verify if a specific Tiles definition was used as the result of
executing an Action.  (MockStrutsTestCase and CactusStrutsTestCase)

- added clearRequestParameters method to clear out request parameters
when running multiple actions in a test.  Currently, this method
cannot reset parameters set using Cactus beginXXX methods. (MockStrutsTestCase
and CactusStrutsTestCase)

- changed HttpServletResponseSimulator.sendError() to set the status code
appropriately and added a getStatusCode() method to grab this value for
testing.  NOTE: the sendError() method still throws an AssertionFailedError
(we need it to, in cases of missing action mappings, and other bad Struts errors)
but that can be caught to test for status codes.  (MockStrutsTestCase only)

Bug Fixes:
---------

- fixed a bug where the servlet context directory was not being used
when searching for application resources (MockStrutsTestCase)

- fixed a bug where actions returning null -- a valid scenario -- could
not be tested properly.  (MockStrutsTestCase and CactusStrutsTestCase)

- fixed setServletConfigFile to use local web.xml DTD's located in the
Struts library.  (MockStrutsTestCase)

- fixed a bug where the contextRelative flag was not being checked when
verifying ActionForward's in sub-applications (MockStrutsTestCase and
CactusStrutsTestCase)

Deryl Seale
http://strutstestcase.sourceforge.net
