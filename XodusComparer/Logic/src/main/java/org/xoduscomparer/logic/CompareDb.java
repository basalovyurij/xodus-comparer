package org.xoduscomparer.logic;

import org.xoduscomparer.logic.model.CompareObjectResult;
import org.xoduscomparer.logic.model.CompareDbResult;
import org.xoduscomparer.logic.model.CompareTableResult;
import org.xoduscomparer.logic.model.CompareState;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.EntityIterable;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import jetbrains.exodus.env.Environments;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xoduscomparer.logic.helpers.Transform;
import org.xoduscomparer.logic.helpers.model.EntityView;

/**
 *
 * @author yurij
 */
public class CompareDb {

    private final static Logger logger = LogManager.getLogger(CompareDb.class);
    
    private final String pathDb1;
    private final String pathDb2;
    private final String key1;
    private final String key2;

    private PersistentEntityStore store1;
    private PersistentEntityStore store2;

    public CompareDb(String pathDb1, String pathDb2) {
        this(pathDb1, null, pathDb2, null);
    }
    
    public CompareDb(String pathDb1, String key1, String pathDb2, String key2) {
        this.pathDb1 = pathDb1;
        this.pathDb2 = pathDb2;
        this.key1 = key2;
        this.key2 = key2;
    }

    public CompareDbResult compare() {
        store1 = PersistentEntityStores.newInstance(Environments.newInstance(pathDb1), key1);
        store2 = PersistentEntityStores.newInstance(Environments.newInstance(pathDb2), key2);

        try {
            return compareTables(new HashSet<>(getTables(store1)), new HashSet<>(getTables(store2)));
        } finally {
            store1.close();
            store2.close();
        }
    }

    private CompareDbResult compareTables(Set<String> s1, Set<String> s2) {
        CompareResult<String> tableCompareResults = compare(s1, s2);
        
        CompareDbResult result = new CompareDbResult();
        result.setTables(new HashMap<>());

        int count = tableCompareResults.onlyFirst.size();
        int i = 1;
        logger.info(String.format("Found %s tables only in first DB", count));
        for(String t : tableCompareResults.onlyFirst) {
            CompareTableResult cmp = compareObjects(getTableContent(store1, t), new HashMap<>());
            cmp.setState(CompareState.EXIST_ONLY_FIRST);
            result.getTables().put(t, cmp);
            logger.info(String.format("Compared %s of %s tabled", i++, count));
        };

        count = tableCompareResults.intersection.size();
        i = 1;
        logger.info(String.format("Found %s tables interstion", count));
        for(String t : tableCompareResults.intersection) {
            CompareTableResult cmp = compareObjects(getTableContent(store1, t), getTableContent(store2, t));
            cmp.setState(CompareState.EXIST_BOTH);
            result.getTables().put(t, cmp);
            logger.info(String.format("Compared %s of %s tabled", i++, count));
        }

        count = tableCompareResults.onlySecond.size();
        i = 1;
        logger.info(String.format("Found %s tables only in second DB", count));
        for(String t : tableCompareResults.onlySecond) {
            CompareTableResult cmp = compareObjects(new HashMap<>(), getTableContent(store1, t));
            cmp.setState(CompareState.EXIST_ONLY_SECOND);
            result.getTables().put(t, cmp);
            logger.info(String.format("Compared %s of %s tabled", i++, count));
        }

        return result;
    }

    private CompareTableResult compareObjects(Map<Long, EntityView> s1, Map<Long, EntityView> s2) {
        CompareResult<Long> objectCompareResults = compare(s1, s2);

        CompareTableResult result = new CompareTableResult();
        result.setObjects(new HashMap<>());

        // TODO
        objectCompareResults.onlyFirst.forEach(t -> {
            result.getObjects().put(t, new CompareObjectResult(CompareState.EXIST_ONLY_FIRST, s1.get(t)));
        });

        objectCompareResults.intersection.forEach(t -> {
            EntityView o1 = s1.get(t);
            EntityView o2 = s2.get(t);

            if (o1.equals(o2)) {
                result.getObjects().put(t, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL, o1));
            } else {
                result.getObjects().put(t, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF, o1, o2));
            }
        });

        objectCompareResults.onlySecond.forEach(t -> {
            result.getObjects().put(t, new CompareObjectResult(CompareState.EXIST_ONLY_SECOND, s2.get(t)));
        });

        return result;
    }

    private Map<Long, EntityView> getTableContent(PersistentEntityStore store, String tableName) {
        Map<Long, EntityView> result = new HashMap<>();

        store.computeInReadonlyTransaction(txn -> {
            EntityIterable entities = txn.getAll(tableName);
            for (Entity entity : entities) {
                result.put(entity.getId().getLocalId(), Transform.asLightView(entity));
            }
            return true;
        });

        return result;
    }

    private Collection<String> getTables(PersistentEntityStore store) {
        return store.computeInReadonlyTransaction(txn -> 
                txn.getEntityTypes());
    }

    private <T> CompareResult<T> compare(Set<T> s1, Set<T> s2) {
        List<T> onlyFirst = s1.stream()
                .filter(i -> !s2.contains(i))
                .collect(Collectors.toList());

        Set<T> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        List<T> onlySecond = s2.stream()
                .filter(i -> !s1.contains(i))
                .collect(Collectors.toList());

        return new CompareResult<>(onlyFirst, intersection, onlySecond);
    }

    private <K, T> CompareResult<K> compare(Map<K, T> s1, Map<K, T> s2) {
        List<K> onlyFirst = s1.keySet().stream()
                .filter(i -> !s2.containsKey(i))
                .collect(Collectors.toList());

        Set<K> intersection = s1.keySet();
        intersection.retainAll(s2.keySet());

        List<K> onlySecond = s2.keySet().stream()
                .filter(i -> !s1.containsKey(i))
                .collect(Collectors.toList());

        return new CompareResult<>(onlyFirst, intersection, onlySecond);
    }

    private static class CompareResult<T> {

        private final Collection<T> onlyFirst;
        private final Collection<T> intersection;
        private final Collection<T> onlySecond;

        public CompareResult(Collection<T> onlyFirst, Collection<T> intersection, Collection<T> onlySecond) {
            this.onlyFirst = onlyFirst;
            this.intersection = intersection;
            this.onlySecond = onlySecond;
        }

        public Collection<T> getOnlyFirst() {
            return onlyFirst;
        }

        public Collection<T> getIntersection() {
            return intersection;
        }

        public Collection<T> getOnlySecond() {
            return onlySecond;
        }
    }
}
