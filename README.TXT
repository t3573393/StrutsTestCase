StrutsTestCase for JUnit - release 2.0
---------------------------------------

This release resolves outstanding issues with Struts 1.1.

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

New Features:
------------

- added verifyTilesForward and verifyInputTilesForward methods to
verify if a specific Tiles definition was used as the result of
executing an Action.  (MockStrutsTestCase and CactusStrutsTestCase)

- added clearRequestParameters method to clear out request parameters
when running multiple actions in a test.  Currently, this method
cannot reset parameters set using Cactus beginXXX methods. (MockStrutsTestCase
and CactusStrutsTestCase)

Bug Fixes:
---------

- Fixed a bug where the servlet context directory was not being used
when searching for application resources (patch #698096 - MockStrutsTestCase)

- Fixed a bug where actions returning null -- a valid scenario -- could
not be tested properly.  (MockStrutsTestCase and CactusStrutsTestCase)


Questions and comments are always welcome!

Deryl Seale - deryl@acm.org
