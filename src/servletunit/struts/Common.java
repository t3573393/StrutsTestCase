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
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ActionConfig;
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
    protected static ComponentDefinition getTilesForward(String forwardPath, HttpServletRequest request, ServletContext context, ServletConfig config) {

        if (logger.isTraceEnabled())
            logger.trace("Entering getTilesForward() : forwardPath = " + forwardPath + ", request = " + request + ", context = " + context + ", config = " + config);

        String result = null;
        try {
            ComponentDefinition definition;
            ComponentDefinition actionDefinition;

            // Get definition of tiles/component corresponding to uri.
            definition = TilesUtil.getDefinition(forwardPath, request, context);
            if (definition != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getTilesForward() : found tiles definition - '" + forwardPath + "' = '" + result + "'");
                }
            }

            actionDefinition = DefinitionsUtil.getActionDefinition(request);
            if (actionDefinition != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getTilesForward() : found tiles definition for action - '" + forwardPath + "' = '" + result + "'");
                }
            }

            if (actionDefinition != null) {
                if (logger.isDebugEnabled())
                    logger.debug("definition attributes: " + actionDefinition.getAttributes());
                return actionDefinition;
            } else {
                if (logger.isDebugEnabled() && (definition != null))
                    logger.debug("definition attributes: " + definition.getAttributes());
                return definition;
            }
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
     * Verifies that ActionServlet used this logical forward or input mapping with this tile definition.
     *
     * @throws AssertionFailedError if the expected and actual tiles definitions do not match.
     */
    protected static void verifyTilesForward(String actionPath, String forwardName, String expectedDefinition, boolean isInputPath, HttpServletRequest request, ServletContext context, ServletConfig config) {
        if (logger.isTraceEnabled())
            logger.trace("Entering verifyTilesForward() : actionPath = " + actionPath + ", forwardName = " + forwardName + ", expectedDefinition = " + expectedDefinition);

        String definitionName = null;

        if ((forwardName == null) && (isInputPath)) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyTilesForward() : processing an input forward");
            }
            forwardName = getActionConfig(actionPath, request, context).getInput();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyTilesForward() : retrieved input forward name = " + forwardName);
            }
            if (forwardName == null)
                throw new AssertionFailedError("Trying to validate against an input mapping, but none is defined for this Action.");
            ComponentDefinition definition = getTilesForward(forwardName, request, context, config);
            if (definition != null)
                definitionName = definition.getName();
        }

        if (!isInputPath) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyTilesForward() : processing normal forward");
            }
            ForwardConfig expectedForward = findForward(actionPath, forwardName, request, context);
            if (expectedForward == null)
                throw new AssertionFailedError("Cannot find forward '" + forwardName + "'  - it is possible that it is not mapped correctly.");
            forwardName = expectedForward.getPath();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyTilesForward() : retrieved forward name = " + forwardName);
            }

            ComponentDefinition definition = getTilesForward(forwardName, request, context, config);
            if (definition != null)
                definitionName = definition.getName();
        }
        if (definitionName == null)
            throw new AssertionFailedError("Could not find tiles definition mapped to forward '" + forwardName + "'");
        if (!definitionName.equals(expectedDefinition))
            throw new AssertionFailedError("Was expecting tiles definition '" + expectedDefinition + "' but received '" + definitionName + "'");
    }

    /**
     * Verifies that ActionServlet used this logical forward or input mapping.
     *
     * @throws AssertionFailedError if expected and actual paths do not match.
     */
    protected static void verifyForwardPath(String actionPath, String forwardName, String actualForwardPath, boolean isInputPath, HttpServletRequest request, ServletContext context, ServletConfig config) {

        if (logger.isTraceEnabled())
            logger.trace("Entering verifyForwardPath() : actionPath = " + actionPath + ", forwardName = " + forwardName + ", actualForwardPath = " + actualForwardPath);

        boolean usesTiles = false;
        boolean useModules = false;

        if ((forwardName == null) && (isInputPath)) {
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : processing an input forward");
            }
            forwardName = getActionConfig(actionPath, request, context).getInput();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : retrieved input forward name = " + forwardName);
            }
            if (forwardName == null)
                throw new AssertionFailedError("Trying to validate against an input mapping, but none is defined for this Action.");
            String tilesForward = null;
            ComponentDefinition definition = getTilesForward(forwardName, request, context, config);
            if (definition != null)
                tilesForward = definition.getPath();
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

            // check for a null forward, now allowed in Struts 1.1
            if (forwardName == null) {
                if (actualForwardPath == null)
                    return;
                else
                    throw new AssertionFailedError("Expected a null forward from action, but received '" + actualForwardPath + "'");
            }

            ForwardConfig expectedForward = findForward(actionPath, forwardName, request, context);
            if (expectedForward == null)
                throw new AssertionFailedError("Cannot find forward '" + forwardName + "'  - it is possible that it is not mapped correctly.");
            forwardName = expectedForward.getPath();
            if (logger.isDebugEnabled()) {
                logger.debug("verifyForwardPath() : retrieved forward name = " + forwardName);
            }

            String tilesForward = null;
            ComponentDefinition definition = getTilesForward(forwardName, request, context, config);
            if (definition != null)
                tilesForward = definition.getPath();
            if (tilesForward != null) {
                forwardName = tilesForward;

                usesTiles = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("verifyForwardPath() : retrieved tiles definition for forward = " + forwardName);
                }
            }
            // some fowards cross outside modules - check if we need the module
            // in the path or not.
            useModules = !expectedForward.getContextRelative ();
        }

        String moduleName = request.getServletPath() != null ? request.getServletPath() : "";
        if ((moduleName == null || moduleName.equalsIgnoreCase("")) && request.getAttribute(INCLUDE_SERVLET_PATH) != null)
            // check to see if this is a MockStrutsTestCase call
            moduleName = (String) request.getAttribute(INCLUDE_SERVLET_PATH);

        if ((moduleName != null) && (moduleName.length() > 0))
        //todo: think i can use first index 0 here, since it will start with a /
            moduleName = moduleName.substring(moduleName.indexOf('/'),moduleName.lastIndexOf('/'));
        if (!forwardName.startsWith("/"))
            forwardName = "/" + forwardName;

        if (usesTiles)
            forwardName = request.getContextPath() + forwardName;
        else if (useModules || isInputPath)
            forwardName = request.getContextPath() + moduleName + forwardName;
        else
            forwardName = request.getContextPath() + forwardName;
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
            StringBuffer buf = new StringBuffer(path);

            int queryIndex = pathCopy.indexOf("?");
            // Strip jsessionid from obtained path, but keep query string
            if (queryIndex > 0)
                path = buf.delete(jsess_idx, queryIndex).toString();
            // Strip jsessionid from obtained path
            else
                path = buf.delete(jsess_idx, buf.length()).toString();
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting stripJSessionID() - returning path = " + path);
        return path;
    }

    /**
     * Returns any ActionForm instance stored in the request or session, if available.
     */
    protected static ActionForm getActionForm(String actionPath, HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering getActionForm() : actionPath = " + actionPath + ", request = " + request + ", context = " + context);
        ActionForm form;
        ActionConfig actionConfig = getActionConfig(actionPath, request, context);
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

    /**
     * Returns a ForwardConfig for the given forward name.  This method first searches for the forward in the supplied
     * action mapping.  If it is not defined there, or if the mapping is not provided, it searches for it globally.
     */
    protected static ForwardConfig findForward(String mappingName, String forwardName, HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering findForward() : mappingName = " + mappingName + ", forwardName = " + forwardName + ", request = " + request + ", context = " + context);
        ForwardConfig forward = null;
        // first, look for forward in mapping (if it's defined)
        if (mappingName != null) {
            ActionConfig mapping = getActionConfig(mappingName, request, context);
            forward =  mapping == null ? null : mapping.findForwardConfig(forwardName);
        }
        // if it's not there, check for global forwards
        if (forward == null) {
            if (logger.isDebugEnabled())
                logger.debug("findForward() : looking for forward globally");
            ModuleConfig moduleConfig = getModuleConfig(request,context);
            forward = moduleConfig.findForwardConfig(forwardName);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("findForward() : retrieved forward = " + forward);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting findForward()");
        return forward;
    }

    /**
     * Returns the configuration for the given action mapping.
     */
    protected static ActionConfig getActionConfig(String mappingName, HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering getActionConfig() : mappingName = " + mappingName + ", request = " + request + ", context = " + context);
        ModuleConfig config = getModuleConfig(request, context);
        ActionMapping actionMapping = (ActionMapping) config.findActionConfig(mappingName);
        if (logger.isDebugEnabled()) {
            logger.debug("getActionConfig() : retrieved mapping = " + actionMapping);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting getActionConfig()");
        return actionMapping;
    }

    /**
     * Returns the configuration for the current module.
     */
    protected static ModuleConfig getModuleConfig(HttpServletRequest request, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering getModuleConfig() : request = " + request + ", context = " + context);
        if (logger.isDebugEnabled()) {
            logger.debug("getModuleConfig() : looking for config in request context");
        }
        ModuleConfig config = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        if (config == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("getModuleConfig() : looking for config in application context");
            }
            config = (ModuleConfig) context.getAttribute(Globals.MODULE_KEY);
        }
        if (logger.isTraceEnabled())
            logger.trace("Exiting getModuleConfig() : returning " + config);
        return config;

    }

    /**
     * Sets an ActionForm instance in the request.
     */
    protected static void setActionForm(ActionForm form, HttpServletRequest request, String actionPath, ServletContext context) {
        if (logger.isTraceEnabled())
            logger.trace("Entering setActionForm() : form = " + form + ", request = " + request + ", actionPath = " + actionPath + ", context = " + context);

        if (actionPath == null || actionPath.equalsIgnoreCase(""))
            throw new IllegalStateException("You must call setRequestPathInfo() before calling setActionForm()!");
        ActionConfig actionConfig = getActionConfig(actionPath, request, context);
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
