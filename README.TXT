StrutsTestCase for JUnit - release 2.0.2
----------------------------------------

This release resolves outstanding issues with Struts 1.1.

The StrutsTestCase library is available as four distinct
releases supporting both the 2.2 and 2.3 versions of the 
Servlet specification, and both the 1.0.x and 1.1 versions
of the Struts Framework.

Change Log
==========

New Features:
------------


Bug Fixes:
---------

- Fixed a bug where the jsessionid was not properly stripped
if it was longer than 44 characters.  (MockStrutsTestCase and
CactusStrutsTestCase)


Deryl Seale
http://strutstestcase.sourceforge.net
