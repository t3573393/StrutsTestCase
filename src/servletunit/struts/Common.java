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

package servletunit.struts;

import junit.framework.AssertionFailedError;
import org.apache.struts.action.*;
import org.apache.struts.tiles.*;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.Globals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;


/**
 * Contains code common to both MockStrutsTestCase and CactusStrutsTestCase.
 * It's always good to get rid of redundancy!
 */
public class Common {

    protected static final String INCLUDE_SERVLET_PATH = RequestProcessor.INCLUDE_SERVLET_PATH;
    protected static Log logger = LogFactory.getLog(Common.class);

    /**
     * Common method to verify action errors and action messages.
     */
    protected static void verifyNoActionMessages(HttpServletRequest request, String key, String messageLabel) {
        if (logger.isTraceEnabled())
            logger.trace("Entering verifyNoActionMessages() : request = " + request + ", key = " + key + ", messageLabel = " + messageLabel);
        ActionMessages messages = (ActionMessages) request.getAttribute(key);
        if (logger.isDebugEnabled()) {
            logger.debug("verifyNoActionMessages() : retrieved ActionMessages = " + messages);
        }
        if (messages != null) {
            Iterator iterator = messages.get();
            if (iterator.hasNext()) {
                StringBuffer messageText = new StringBuffer();
                while (iterator.hasNext()) {
                    messageText.append(" \"");
                    messageText.append(((ActionMessage) iterator.next()).getKey());
                    messageText.append("\"");
                }
                throw new AssertionFailedError("was expecting no " + messageLabel + " messages, but received: " + messageText.toString());
            }
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting verifyNoActionMessages()");
    }

    /**
     * Common method to verify action errors and action messages.
     */
    protected static void verifyActionMessages(HttpServletRequest request, String[] messageNames, String key, String messageLabel) {
        if (logger.isTraceEnabled())
            logger.trace("Entering verifyActionMessages() : request = " + request + ", messageNames = " + messageNames + ", key = " + key + ", messageLabel = " + messageLabel);
        int actualLength = 0;

        ActionMessages messages = (ActionMessages) request.getAttribute(key);
        if (logger.isDebugEnabled()) {
            logger.debug("verifyNoActionMessages() : retrieved ActionMessages = " + messages);
        }
        if (messages == null) {
            throw new AssertionFailedError("was expecting some " + messageLabel + " messages, but received none.");
        } else {
            Iterator iterator = messages.get();
            while (iterator.hasNext()) {
                actualLength++;
                boolean throwError = true;
                ActionMessage message = (ActionMessage) iterator.next();
                for (int x = 0; x < messageNames.length; x++) {
                    if (message.getKey().equals(messageNames[x]))
                        throwError = false;
                }
                if (throwError) {
                    throw new AssertionFailedError("received unexpected " + messageLabel + " message \"" + message.getKey() + "\"");
                }
            }
            if (actualLength != messageNames.length) {
                throw new AssertionFailedError("was expecting " + messageNames.length + " " + messageLabel + " message(s), but received " + actualLength + " " + messageLabel + " message(s)");
            }
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting verifyActionMessages()");
    }

    /**
     * Retrieves a forward uri for tile - this is required for applications
     * using the tiles framework, since the actual forward URI must
     * be fetched from the tile definition.
     */
    protected static String getTilesForward(String forwardPath, HttpServletRequest request, ServletContext context, ServletConfig config) {

        if (logger.isTraceEnabled())
            logger.trace("Entering getTilesForward() : forwardPath = " + forwardPath + ", request = " + request + ", context = " + context + ", config = " + config);

        String result = null;
        try {
            ComponentDefinition definition;
            // Get definition of tiles/component corresponding to uri.
            definition = TilesUtil.getDefinition(forwardPath, request, context);
            if (definition != null) {
                // We have a definition.
                // We use it to complete missing attribute in context.
                // We also get uri, controller.
                result = definition.getPath();
                if (logger.isDebugEnabled()) {
                    logger.debug("getTilesForward() : found tiles definition - '" + forwardPath + "' = '" + result + "'");
                }
            }
            definition = DefinitionsUtil.getActionDefinition(request);
            if (definition != null) {
                // We have a definition.
                // We use it to complete missing attribute in context.
                // We also overload uri and controller if set in definition.
                if (definition.getPath() != null)
                    result = definition.getPath();
                if (logger.isDebugEnabled()) {
                    logger.debug("getTilesForward() : found tiles definition for action - '" + forwardPath + "' = '" + result + "'");
                }
            }
            return result;
        } catch (NoSuchDefinitionException nsde) {
            if (logger.isTraceEnabled())
                logger.trace("Exiting getTilesForward(): caught NoSuchDefinitionException");
            return null;
        } catch (DefinitionsFactoryException dfe) {
            if (logger.isTraceEnabled())
                logger.trace("Exiting getTilesForward(): caught DefinitionsFactoryException");
            return null;
        } catch (NullPointerException npe) {
            // can happen if tiles is not at all used.
            if (logger.isDebugEnabled()) {
                logger.debug("Exiting getTilesForward() : caught NullPointerException");
            }
            return null;
        }
    }

    /**
     * Verifies that ActionServlet used this logical forward or input mapping.
     *
     * @throws AssertionFailedError if expected and actual paths do not match.
     */
    protected static void verifyForwardPath(ActionServlet actionServlet, String actionPath, String forwardName, String actualForwardPath, boolean isInputPath, HttpServletRequest request, ServletContext context, ServletConfig config) {

        if (logger.isTraceEnabled())
            logger.trace("Entering verifyForwardPath() : actionServlet = " + actionServlet + ", actionPath = " + actionPath + ", forwardName = " + forwardName + ", actualForwardPath = " + actualForwardPath);

        boolean usesTiles = false;

        if ((forwardName == null) && (isInputPath)) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : processing an input forward");
            }
            forwardName = getActionConfig(actionServlet, actionPath, request, context).getInput();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : retrieved input forward name = " + forwardName);
            }
            if (forwardName == null)
                throw new AssertionFailedError("Trying to validate against an input mapping, but none is defined for this Action.");
            String tilesForward = getTilesForward(forwardName, request, context, config);
            if (tilesForward != null) {
                forwardName = tilesForward;
                usesTiles = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("verifyForwardPath() : retrieved tiles definition for forward = " + forwardName);
                }
            }
        }
        if (!isInputPath) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : processing normal forward");
            }
            ActionForward expectedForward = findForward(actionPath, forwardName, request, context, actionServlet);
            if (expectedForward == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("verifyForwardPath() : looking for global forward declaration");
                }
                expectedForward = actionServlet.findForward(forwardName);
            }
            if (expectedForward == null)
                throw new AssertionFailedError("Cannot find forward '" + forwardName + "'  - it is possible that it is not mapped correctly.");
            forwardName = expectedForward.getPath();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : retrieved forward name = " + forwardName);
            }

            String tilesForward = getTilesForward(forwardName, request, context, config);
            if (tilesForward != null) {
                forwardName = tilesForward;

                usesTiles = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("verifyForwardPath() : retrieved tiles definition for forward = " + forwardName);
                }
            }
        }
        String moduleName = (String) request.getAttribute(INCLUDE_SERVLET_PATH);
        if ((moduleName != null) && (moduleName.length() > 0))
        //todo: think i can use first index 0 here, since it will start with a /
            moduleName = moduleName.substring(moduleName.indexOf('/'),moduleName.lastIndexOf('/'));
        if (!forwardName.startsWith("/"))
            forwardName = "/" + forwardName;

        if (usesTiles)
            forwardName = request.getContextPath() + forwardName;
        else
            forwardName = request.getContextPath() + moduleName + forwardName;
        if (logger.isDebugEnabled()) {
            logger.debug("verifyForwardPath() : added context path and module name to forward = " + forwardName);
        }
        if (actualForwardPath == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : actualForwardPath is null - this usually means it is not mapped properly.");
            }
            throw new AssertionFailedError("Was expecting '" + forwardName + "' but it appears the Action has tried to return an ActionForward that is not mapped correctly.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("verifyForwardPath() : expected forward = '" + forwardName + "' - actual forward = '" + actualForwardPath + "'");
        }
        if (!forwardName.equals(stripJSessionID(actualForwardPath)))
            throw new AssertionFailedError("was expecting '" + forwardName + "' but received '" + actualForwardPath + "'");
        if (logger.isTraceEnabled())
            logger.trace("Exiting verifyForwardPath()");
    }

    /**
     * Strips off *.do from action paths specified as such.
     */
    protected static String stripActionPath(String path) {
        if (logger.isTraceEnabled())
            logger.trace("Entering stripActionPath() : path = " + path);
        if (path == null)
            return null;

        int slash = path.lastIndexOf("/");
        int period = path.lastIndexOf(".");
        if ((period >= 0) && (period > slash))
            path = path.substring(0, period);
        if (logger.isTraceEnabled())
            logger.trace("Exiting stripActionPath() - returning path = " + path);
        return path;
    }

    /**
     * Strip ;jsessionid=<sessionid> from path.
     * @return stripped path
     */
    protected static String stripJSessionID(String path) {
        if (logger.isTraceEnabled())
            logger.trace("Entering stripJSessionID() : path = " + path);
        if (path == null)
            return null;

        String pathCopy = path.toLowerCase();
        int jsess_idx = pathCopy.indexOf(";jsessionid=");
        if (jsess_idx > 0) {
            // Strip jsessionid from obtained path
            StringBuffer buf = new StringBuffer(path);
            path = buf.delete(jsess_idx, jsess_idx + 44).toString();
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting stripJSessionID() - returning path = " + path);
        return path;
    }

    /**
     * Returns any ActionForm instance stored in the request or session, if available.
     */
    protected static ActionForm getActionForm(ActionServlet actionServlet, String actionPath, HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering getActionForm() : actionServlet = " + actionServlet + ", actionPath = " + actionPath + ", request = " + request + ", context = " + context);
        ActionForm form;
        ActionMapping actionConfig = getActionConfig(actionServlet, actionPath, request, context);
        if ("request".equals(actionConfig.getScope())) {
            if (logger.isDebugEnabled()) {
                logger.debug("getActionForm() : looking for form in request scope");
            }
            form = (ActionForm) request.getAttribute(actionConfig.getAttribute());
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("getActionForm() : looking for form in session scope");
            }
            HttpSession session = request.getSession();
            form = (ActionForm) session.getAttribute(actionConfig.getAttribute());
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting getActionForm()");
        return form;
    }

    protected static ActionForward findForward(String mappingName, String forwardName, HttpServletRequest request, ServletContext context, ActionServlet actionServlet) {
        if (logger.isTraceEnabled())
            logger.trace("Entering findForward() : mappingName = " + mappingName + ", forwardName = " + forwardName + ", request = " + request + ", context = " + context + ", actionServlet = " + actionServlet);
        ActionForward forward = getActionConfig(actionServlet, mappingName, request, context).findForward(forwardName);
        if (logger.isDebugEnabled()) {
            logger.debug("findForward() : retrieved forward = " + forward);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting findForward()");
        return forward;
    }

    protected static ActionMapping getActionConfig(ActionServlet actionServlet, String mappingName, HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering getActionConfig() : actionServlet = " + actionServlet + ", mappingName = " + mappingName + ", request = " + request + ", context = " + context);
        if (logger.isDebugEnabled()) {
            logger.debug("getActionConfig() : looking for config in request context");
        }
        ModuleConfig config = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        if (config == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("getActionConfig() : looking for config in application context");
            }
            config = (ModuleConfig) context.getAttribute(Globals.MODULE_KEY);
        }
        ActionMapping actionMapping = (ActionMapping) config.findActionConfig(mappingName);
        if (logger.isDebugEnabled()) {
            logger.debug("getActionConfig() : retrieved mapping = " + actionMapping);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting getActionConfig()");
        return actionMapping;
    }

    protected static void setActionForm(ActionForm form, HttpServletRequest request, String actionPath, ServletContext context, ActionServlet actionServlet) {
        if (logger.isTraceEnabled())
            logger.trace("Entering setActionForm() : form = " + form + ", request = " + request + ", actionPath = " + actionPath + ", context = " + context + ", actionServlet = " + actionServlet);
        ActionMapping actionConfig = getActionConfig(actionServlet, actionPath, request, context);
        if (actionConfig.getScope().equals("request"))  {
            if (logger.isDebugEnabled()) {
                logger.debug("setActionForm() : setting form in request context");
            }
            request.setAttribute(actionConfig.getAttribute(), form);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("setActionForm() : setting form in session context");
            }
            request.getSession().setAttribute(actionConfig.getAttribute(), form);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting setActionForm()");
    }

}
