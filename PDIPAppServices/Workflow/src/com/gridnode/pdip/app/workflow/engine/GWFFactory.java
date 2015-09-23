package com.gridnode.pdip.app.workflow.engine;


import java.util.Hashtable;

import com.gridnode.pdip.app.workflow.impl.bpss.BpssActivityEngine;
import com.gridnode.pdip.app.workflow.impl.bpss.BpssProcessEngine;
import com.gridnode.pdip.app.workflow.impl.bpss.BpssRestrictionEngine;
 

public class GWFFactory {
    public static final String ENGINE_TYPE = "ENGINE_TYPE";
    public static final String XPDL_ENGINE = "XPDL";
    public static final String BPSS_ENGINE = "BPSS";

    private static Hashtable routeDispatchHt = new Hashtable();
    private static Hashtable activityEngineHt = new Hashtable();
    private static Hashtable processEngineHt = new Hashtable();
    private static Hashtable restrictionEngineHt = new Hashtable();

    public static IGWFRouteDispatcher getRouteDispatcher(String engineType) {
        IGWFRouteDispatcher dispatcher = (IGWFRouteDispatcher) routeDispatchHt.get(engineType);

        if (dispatcher == null) {
            synchronized (routeDispatchHt) {
                if (BPSS_ENGINE.equals(engineType))
                   dispatcher = new GWFJMSRouteDispatcher(engineType);
                routeDispatchHt.put(engineType, dispatcher);
            }
        }
        return dispatcher;
    }

    public static GWFAbstractActivityEngine getActivityEngine(String engineType) {
        GWFAbstractActivityEngine activityEngine = (GWFAbstractActivityEngine) activityEngineHt.get(engineType);

        if (activityEngine == null) {
            synchronized (activityEngineHt) {
                if (BPSS_ENGINE.equals(engineType))
                    activityEngine = new BpssActivityEngine();
                if (activityEngine != null)
                    activityEngineHt.put(engineType, activityEngine);
            }
        }
        return activityEngine;
    }

    public static GWFAbstractProcessEngine getProcessEngine(String engineType) {
        GWFAbstractProcessEngine processEngine = (GWFAbstractProcessEngine) processEngineHt.get(engineType);

        if (processEngine == null) {
            synchronized (processEngineHt) {
                if (BPSS_ENGINE.equals(engineType))
                    processEngine = new BpssProcessEngine();
                if (processEngine != null)
                    processEngineHt.put(engineType, processEngine);
            }
        }
        return processEngine;
    }

    public static GWFAbstractRestrictionEngine getRestrictionEngine(String engineType) {
        GWFAbstractRestrictionEngine restrictionEngine = (GWFAbstractRestrictionEngine) restrictionEngineHt.get(engineType);

        if (restrictionEngine == null) {
            synchronized (restrictionEngineHt) {
                if (BPSS_ENGINE.equals(engineType))
                    restrictionEngine = new BpssRestrictionEngine();
                if (restrictionEngine != null)
                    restrictionEngineHt.put(engineType, restrictionEngine);
            }
        }
        return restrictionEngine;
    }

}
