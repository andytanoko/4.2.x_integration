package com.gridnode.pdip.app.workflow.engine;

import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.framework.util.*;

public class GWFAbstractWorkflowEngine {

    protected GWFRtProcess getRtProcess(Long rtProcessUId) throws Throwable {
        return (GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
    }

    protected GWFRtActivity getRtActivity(Long rtActivityUId) throws Throwable {
        return (GWFRtActivity)UtilEntity.getEntityByKey(rtActivityUId,GWFRtActivity.ENTITY_NAME,true);
    }

    protected GWFRtRestriction getRtRestriction(Long rtRestrictionUId) throws Throwable {
        return (GWFRtRestriction)UtilEntity.getEntityByKey(rtRestrictionUId,GWFRtRestriction.ENTITY_NAME,true);
    }

}