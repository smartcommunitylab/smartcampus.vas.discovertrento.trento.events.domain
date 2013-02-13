package eu.trentorise.smartcampus.domain.trento.events;

import it.sayservice.platform.core.domain.common.exception.DomainDataHandlerException;
import it.sayservice.platform.core.domain.DomainConst.DOMAIN_OBJECT_EVENT_TYPE;
import it.sayservice.platform.core.domain.DomainRelationTarget;
import it.sayservice.platform.core.domain.bundle.DomainEvent;
import it.sayservice.platform.core.domain.ext.AbstractDOEngineImpl;
import it.sayservice.platform.core.domain.ext.ActionInvoke;
import it.sayservice.platform.core.domain.ext.DomainObjectWrapper;
import it.sayservice.platform.core.domain.ext.LanguageHelper;
import it.sayservice.platform.core.domain.ext.Tuple;
import it.sayservice.platform.core.domain.rules.DomainSubscriptionRule;
import it.sayservice.platform.core.domain.rules.EvaluableDomainSubscriptionRule;
import it.sayservice.platform.core.domain.rules.EvaluableDomainOperation;
import it.sayservice.platform.core.domain.rules.DomainEventDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class TrentoEventsServiceDOEngine extends AbstractDOEngineImpl {

    public TrentoEventsServiceDOEngine() {
        super();
    }

    private static String[] actions = new String[]{
            "initialize",
            "_serviceNotification_update",
    };

    protected String[] getSortedActions() {
        return actions;
    }

    private static Map<String,Collection<java.io.Serializable>> extensions = new HashMap<String,Collection<java.io.Serializable>>();
    static {
        extensions.put("it.sayservice.platform.core.domain.actions.InvokeOperation",decodeExtension("rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAKc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAV0AAlvcGVyYXRpb250AA9zdWJzY3JpYmVFdmVudHN0AAlzZXJ2aWNlSWR0AD1ldS50cmVudG9yaXNlLnNtYXJ0Y2FtcHVzLnNlcnZpY2VzLnRyZW50by5ldmVudHMuVHJlbnRvRXZlbnRzdAAJY29udmVydGVycHQABHR5cGV0AAxTdWJzY3JpcHRpb250AAptZXRob2ROYW1ldAAJR2V0RXZlbnRzeHg="));
    }

     public  Collection<java.io.Serializable> getExtensionValues(String property) {
        return extensions.get(property);
     }  
    
    protected Object executeAction(String actionName, DomainObjectWrapper obj, Tuple t, Set<DomainEvent> outEvents, Set<EvaluableDomainOperation> ops, String securityToken, String bundleId) throws DomainDataHandlerException {
        if ("initialize".equals(actionName)) {
            return initialize(t, obj, outEvents, ops, securityToken, bundleId);
        }
        if ("_serviceNotification_update".equals(actionName)) {
            return _serviceNotification_update(t, obj, outEvents, ops, securityToken, bundleId);
        }
        return null;
    }

    private Object initialize(Tuple tuple, DomainObjectWrapper obj, Set<DomainEvent> evts, Set<EvaluableDomainOperation> ops, String securityToken, String bundleId) throws DomainDataHandlerException {
        {
{
{
Tuple body = new Tuple();
 body.put("cities","Trento");
getDomainObjectHandler().invokeOperation(obj, "subscribeEvents", body, ops, bundleId);}
}
return null;
}

    }
    private Object _serviceNotification_update(Tuple tuple, DomainObjectWrapper obj, Set<DomainEvent> evts, Set<EvaluableDomainOperation> ops, String securityToken, String bundleId) throws DomainDataHandlerException {
        	 try {
		Tuple body = new Tuple();
		body.putAll((Map)new eu.trentorise.smartcampus.domain.trento.events.converter.EventsDataConverter().fromMessage((java.io.Serializable)tuple.get("data")));
		getDomainObjectHandler().publishCustomEvent("update", body, obj, evts, bundleId);
	} catch (Exception e) {
		log.error("Failed to parse update message: "+e.getMessage(),e);
	}
return null;

    }
    
    
    
    
    public void handleObjectRelUpdate(String rName, DomainObjectWrapper obj, Set<DomainEvent> evts, String bundleId) throws DomainDataHandlerException {
    }
    public void handleObjectVarUpdate(String vName, DomainObjectWrapper obj, Set<DomainEvent> evts, String bundleId) throws DomainDataHandlerException {
    }

    public void handleObjectCreate(DomainObjectWrapper obj, Set<DomainEvent> outEvents, Set<EvaluableDomainOperation> ops, String bundleId) throws DomainDataHandlerException {
        __initialize(new Tuple(), obj, outEvents, ops, obj.getDomainObject().getSecurityToken(),bundleId);
    }

    private Object __initialize(Tuple tuple, DomainObjectWrapper obj, Set<DomainEvent> evts, Set<EvaluableDomainOperation> ops, String securityToken, String bundleId) throws DomainDataHandlerException {
        {
{
{
Tuple body = new Tuple();
 body.put("cities","Trento");
getDomainObjectHandler().invokeOperation(obj, "subscribeEvents", body, ops, bundleId);}
}
return null;
}

    }

    protected ActionInvoke ruleApplies(EvaluableDomainSubscriptionRule rule, DomainObjectWrapper obj, String bundleId) throws DomainDataHandlerException {
        if ("_subscribe_serviceNotification_update".equals(rule.getRule().getEngineName())) {
            Tuple payload = (Tuple) rule.getEvent().getPayload();
            if (true){
                Tuple tuple = new Tuple();
                tuple.put("data",payload.get("data"));
                return new ActionInvoke("_serviceNotification_update", tuple);
            }
        }
        return null;
    }


    @Override
    public boolean isRelation(String key) {
        return false
        ;    
    }

    @Override
    public String getType() {
        return "eu.trentorise.smartcampus.domain.trento.events.TrentoEventsService";
    }

    @Override
    public boolean isStatic() {
        return !false;
    }

    private static Collection<String> dependencies = new java.util.HashSet<String>();
    static {
    }

    public Collection<String> getDependencies() {
        return dependencies;
    }

    public Collection<DomainSubscriptionRule> getSubscriptions(DomainObjectWrapper obj, String bundleId) throws DomainDataHandlerException {
        List<DomainSubscriptionRule> rules = new ArrayList<DomainSubscriptionRule>();
        
        {
            DomainSubscriptionRule 
            rule = new DomainSubscriptionRule();
            rule.setName("_subscribe_serviceNotification_update");
            rule.setSourceId(null);
            rule.setSourceType("eu.trentorise.smartcampus.services.trento.events.TrentoEvents.GetEvents");
            rule.setTargetId(obj.getId());
            rule.setTargetType(obj.getType());
            rule.setEngineName("_subscribe_serviceNotification_update");
            rule.setEventType(DOMAIN_OBJECT_EVENT_TYPE.CUSTOM .toString());
            rule.setEventSubtype("serviceUpdate");
            rules.add(rule);
        }
        return rules;
    }

    
    public it.sayservice.platform.domain.model.interfaces.DomainTypeInterface getInterface() throws it.sayservice.platform.core.domain.common.exception.DomainDataHandlerException {
        return eu.trentorise.smartcampus.domain.trento.events .TrentoEventsServiceInterface .getInstance();
    }
    

    public List<DomainSubscriptionRule> findSubscriptionRules(DomainEvent event, String securityToken, String bundleId) throws DomainDataHandlerException {
        List<DomainSubscriptionRule> result = new ArrayList<DomainSubscriptionRule>();
        List<DomainSubscriptionRule> tmp = null;
        return result;
    }
    
    public Collection<DomainEventDescriptor> getEventsToQuery() {
        List<DomainEventDescriptor> result = new ArrayList<DomainEventDescriptor>();
        return result;
    }
    
}


