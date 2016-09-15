package org.xoduscomparer.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.EntityIterable;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import jetbrains.exodus.entitystore.StoreTransaction;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;

/**
 *
 * @author yurij
 */
public class CompareDb {

    private final String pathDb1;
    private final String pathDb2;

    private Environment env1;
    private Environment env2;

    private PersistentEntityStore store1;
    private PersistentEntityStore store2;

    public CompareDb(String pathDb1, String pathDb2) {
        this.pathDb1 = pathDb1;
        this.pathDb2 = pathDb2;
    }

    public CompareDbResult compare() {
        env1 = Environments.newInstance(pathDb1);
        env2 = Environments.newInstance(pathDb2);

        Set<String> tables1 = new HashSet<>(getTables(env1));
        Set<String> tables2 = new HashSet<>(getTables(env2));

        env1.close();
        env2.close();        

        return compareTables(tables1, tables2);
    }

    private CompareDbResult compareTables(Set<String> s1, Set<String> s2) {
        store1 = PersistentEntityStores.newInstance(pathDb1);
        store2 = PersistentEntityStores.newInstance(pathDb2);

        try {
            CompareResult<String> tableCompareResults = compare(s1, s2);

            CompareDbResult result = new CompareDbResult();
            result.setTables(new HashMap<>());

            tableCompareResults.onlyFirst.forEach(t -> {
                CompareTableResult cmp = compareObjects(getTableContent(store1, t), new HashMap<>());
                cmp.setState(CompareState.EXIST_ONLY_FIRST);
                result.getTables().put(t, cmp);
            });

            tableCompareResults.intersection.forEach(t -> {
                CompareTableResult cmp = compareObjects(getTableContent(store1, t), getTableContent(store2, t));
                cmp.setState(CompareState.EXIST_BOTH);
                result.getTables().put(t, cmp);
            });

            tableCompareResults.onlySecond.forEach(t -> {
                CompareTableResult cmp = compareObjects(new HashMap<>(), getTableContent(store1, t));
                cmp.setState(CompareState.EXIST_ONLY_SECOND);
                result.getTables().put(t, cmp);
            });

            return result;
        } finally {
            store1.close();
            store2.close();
        }
    }

    private CompareTableResult compareObjects(Map<Long, Entity> s1, Map<Long, Entity> s2) {
        CompareResult<Long> objectCompareResults = compare(s1, s2);

        CompareTableResult result = new CompareTableResult();
        result.setObjects(new LinkedList<>());

        // TODO
        objectCompareResults.onlyFirst.forEach(t -> {
            result.getObjects().add(new CompareObjectResult(CompareState.EXIST_ONLY_FIRST, s1.get(t)));
        });

        objectCompareResults.intersection.forEach(t -> {
            Entity o1 = s1.get(t);
            Entity o2 = s2.get(t);

            if (o1.compareTo(o2) == 0) {
                result.getObjects().add(new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL, o1));
            } else {
                result.getObjects().add(new CompareObjectResult(CompareState.EXIST_BOTH_DIFF, o1, o2));
            }
        });

        objectCompareResults.onlySecond.forEach(t -> {
            result.getObjects().add(new CompareObjectResult(CompareState.EXIST_ONLY_SECOND, s2.get(t)));
        });

        return result;
    }

    private Map<Long, Entity> getTableContent(PersistentEntityStore store, String tableName) {
        Map<Long, Entity> result = new HashMap<>();

        StoreTransaction txn = store.beginReadonlyTransaction();
        EntityIterable entities = txn.getAll(tableName);
        for (Entity entity : entities) {
            result.put(entity.getId().getLocalId(), entity);
        }        
        txn.abort();

        return result;
    }

    private Collection<String> getTables(Environment env) {
        return env.computeInTransaction(t -> env.getAllStoreNames(t));
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
