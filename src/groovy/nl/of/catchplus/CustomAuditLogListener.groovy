package nl.of.catchplus

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogListener
import org.hibernate.collection.PersistentCollection
import org.hibernate.engine.CollectionEntry
import org.hibernate.engine.PersistenceContext
import org.hibernate.event.PostUpdateEvent

class CustomAuditLogListener extends AuditLogListener {

    @Override
    void onPostUpdate(final PostUpdateEvent event) {
        if (isAuditableEntity(event)) {
            log.trace "${event.getClass()} onChange handler has been called"
            onChange(event)
        }
    }

    private void onChange(final PostUpdateEvent event) {
        def entity = event.getEntity()
        String entityName = entity.getClass().getName()
        def entityId = event.getId()

        // object arrays representing the old and new state
        def oldState = event.getOldState()
        def newState = event.getState()

        List<String> propertyNames = event.getPersister().getPropertyNames()
        Map oldMap = [:]
        Map newMap = [:]

        if (propertyNames) {
            for (int index = 0; index < newState.length; index++) {
                if (propertyNames[index]) {
                    if (oldState) {
                        populateOldStateMap(oldState, oldMap, propertyNames[index], index)
                    }
                    if (newState) {
                        newMap[propertyNames[index]] = newState[index]
                    }
                }
            }
        }

        if (!significantChange(entity, oldMap, newMap)) {
            return
        }

        // allow user's to over-ride whether you do auditing for them.
        if (!callHandlersOnly(event.getEntity())) {
            logChanges(newMap, oldMap, event, entityId, 'UPDATE', entityName)
        }
        executeHandler(event, 'onChange', oldMap, newMap)
        return
    }

    private populateOldStateMap(def oldState, Map oldMap, String keyName, index) {
        def oldPropertyState = oldState[index]
        if (oldPropertyState instanceof PersistentCollection) {
            PersistentCollection pc = (PersistentCollection) oldPropertyState;
            PersistenceContext context = sessionFactory.getCurrentSession().getPersistenceContext();
            CollectionEntry entry = context.getCollectionEntry(pc);
            Object snapshot = entry.getSnapshot();
            /*System.out.println(entry);
            System.out.println(snapshot);*/
            if (pc instanceof List) {
                oldMap[keyName] = Collections.unmodifiableList((List) snapshot);
            }
            else if (pc instanceof Map) {
                oldMap[keyName] = Collections.unmodifiableMap((Map) snapshot);
            }
            else if (pc instanceof Set) {
                //Set snapshot is actually stored as a Map
                if(!snapshot)
                {
                    System.out.println("ERRRRRORRRRRR");
                    System.out.println(keyName);
                    oldMap[keyName]=null
                }
                else
                {
                    Map snapshotMap = (Map) snapshot;
                    oldMap[keyName] = Collections.unmodifiableSet(new HashSet(snapshotMap?.values()));
                    System.out.println(oldMap[keyName].class);
                }
            }
            else {
                oldMap[keyName] = pc;
            }
        }
        else {
            oldMap[keyName] = oldPropertyState
        }
    }
}