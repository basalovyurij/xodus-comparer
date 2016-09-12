package org.xoduscomparer.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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

    private final Environment env1;
    private final Environment env2;
    
    private final PersistentEntityStore store1;
    private final PersistentEntityStore store2;

    public CompareDb(String pathDb1, String pathDb2) {
        env1 = Environments.newInstance(pathDb1);
        env2 = Environments.newInstance(pathDb2);

        store1 = PersistentEntityStores.newInstance(pathDb1);
        store2 = PersistentEntityStores.newInstance(pathDb2);
    }
            
    public CompareDbResult compare() {
        return compareTables(new HashSet<>(getTables(env1)), new HashSet<>(getTables(env2)));
    }

    private CompareDbResult compareTables(Set<String> s1, Set<String> s2) {
        CompareResult<String> tableCompareResults = compare(s1, s2);

        CompareDbResult result = new CompareDbResult();
        result.setTables(new HashMap<>());

        tableCompareResults.onlyFirst.forEach(t -> {
            CompareTableResult cmp = compareObjects(new HashSet<>(getTableContent(store1, t)), new HashSet<>());
            cmp.setState(CompareState.EXIST_ONLY_FIRST);
            result.getTables().put(t, cmp);
        });
        
        tableCompareResults.intersection.forEach(t -> {
            CompareTableResult cmp = compareObjects(new HashSet<>(getTableContent(store1, t)), new HashSet<>(getTableContent(store2, t)));
            cmp.setState(CompareState.EXIST_BOTH);
            result.getTables().put(t, cmp);
        });
        
        tableCompareResults.onlySecond.forEach(t -> {
            CompareTableResult cmp = compareObjects(new HashSet<>(), new HashSet<>(getTableContent(store1, t)));
            cmp.setState(CompareState.EXIST_ONLY_SECOND);
            result.getTables().put(t, cmp);
        });
        
        return result;
    }

    private CompareTableResult compareObjects(Set<Entity> s1, Set<Entity> s2) {
        CompareResult<Entity> tableCompareResults = compare(s1, s2);

        CompareTableResult result = new CompareTableResult();
        result.setObjects(new LinkedList<>());

        // TODO
        tableCompareResults.onlyFirst.forEach(t -> {
            result.getObjects().add(new CompareObjectResult(CompareState.EXIST_ONLY_FIRST, t));
        });
        
        tableCompareResults.intersection.forEach(t -> {
            result.getObjects().add(new CompareObjectResult(CompareState.EXIST_ONLY_FIRST, t));
        });
        
        tableCompareResults.onlySecond.forEach(t -> {
            result.getObjects().add(new CompareObjectResult(CompareState.EXIST_ONLY_FIRST, t));
        });
        
        return result;
    }

    private Collection<Entity> getTableContent(PersistentEntityStore store, String tableName) {
        Collection< Entity> result = new LinkedList<>();
        
        StoreTransaction txn = store.beginReadonlyTransaction();
        EntityIterable entities = txn.getAll(tableName);
        for (Entity entity : entities) {
            result.add(entity);
        }
                
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
