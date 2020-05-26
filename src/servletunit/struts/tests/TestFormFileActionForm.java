//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import java.io.File;

import org.apache.struts.action.ActionForm;

import examples.FormFileForm;
import servletunit.struts.MockStrutsTestCase;
import servletunit.struts.StrutsFormFileBuilder;

public class TestFormFileActionForm extends MockStrutsTestCase {

    public TestFormFileActionForm(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        setContextDirectory(new File("D:/strutstestcase/build/examples"));
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testSuccessfulUpload() {
        setRequestPathInfo("/upload");
        FormFileForm uploadForm =  new FormFileForm();
        uploadForm.setUploadFile(StrutsFormFileBuilder.buildFormFile("test.txt", "text", new byte[0]));
        setActionForm(uploadForm);
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        verifyNoActionErrors();
    }
    
    public void testSuccessfulUploadByFilePath() {
        setRequestPathInfo("/upload");
        FormFileForm uploadForm =  new FormFileForm();
        uploadForm.setUploadFile(StrutsFormFileBuilder.buildFormFile("D:/test.txt", "text"));
        setActionForm(uploadForm);
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        verifyNoActionErrors();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestFormFileActionForm.class);
    }


}

