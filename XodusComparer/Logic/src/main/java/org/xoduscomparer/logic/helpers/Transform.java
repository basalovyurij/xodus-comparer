package org.xoduscomparer.logic.helpers;

import java.util.stream.Collectors;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl;
import org.xoduscomparer.logic.helpers.model.EntityView;
import org.xoduscomparer.logic.helpers.model.EntityProperty;
import org.xoduscomparer.logic.helpers.model.PropertyType;

/**
 *
 * @author yurij
 */
public class Transform {

    public static EntityView asLightView(Entity entity) {
        PersistentEntityStoreImpl store = (PersistentEntityStoreImpl) entity.getStore();

        EntityView res = new EntityView();

        res.setId(((Long) entity.getId().getLocalId()).toString());
        res.setProperties(
                entity.getPropertyNames().stream()
                .map(it -> propertyView(entity, it))
                .collect(Collectors.toList()));
        Integer typeId = entity.getId().getTypeId();
        String entityType = store.getEntityType(store.getCurrentTransaction(), typeId);
        //labeled(entityType)
        res.setTypeId(typeId.toString());
        res.setType(entityType);
                
        return res;
    }

    private static EntityProperty propertyView(Entity entity, String name) {
        Comparable value = entity.getProperty(name);
        PersistentEntityStoreImpl store = (PersistentEntityStoreImpl) entity.getStore();

        PropertyType typeVO = new PropertyType();

        Class clazz;
        if (value != null) {
            clazz = store.getPropertyTypes().getPropertyType(value.getClass()).getClazz();
        } else {
            clazz = String.class;
        }
        typeVO.setReadonly(!UIPropertyTypes.isSupported(clazz));
        typeVO.setClazz(clazz.getName());
        typeVO.setDisplayName(clazz.getSimpleName());
                    
        EntityProperty res = new EntityProperty();

        res.setName(name);
        res.setType(typeVO);
        res.setValue(value2string(value));

        return res;
    }

    private static <T extends Comparable> String value2string(T value) {
        if (value == null) {
            return null;
        }
        try {
            Class clazz = value.getClass();
            UIPropertyTypes.UIPropertyType<T> type = UIPropertyTypes.uiTypeOf(clazz);
            return type.toString(value);
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }

    }

}
