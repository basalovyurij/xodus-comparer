package org.xoduscomparer.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;

/**
 *
 * @author yurij
 */
public class CompareDb {

    public CompareDbResult compare(String pathDb1, String pathDb2) {
        Environment env1 = Environments.newInstance(pathDb1);
        Environment env2 = Environments.newInstance(pathDb2);

        CompareResult<String> tableCompareResults = compare(
                new HashSet<>(getTables(env1)), new HashSet<>(getTables(env2)));

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
