package org.xoduscomparer.logic;

import org.xoduscomparer.logic.model.CompareTableResult;
import org.xoduscomparer.logic.model.CompareDbResult;
import org.xoduscomparer.logic.model.CompareState;
import org.xoduscomparer.logic.model.CompareObjectResult;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xoduscomparer.logic.helpers.model.EntityProperty;
import org.xoduscomparer.logic.helpers.model.EntityView;
import org.xoduscomparer.logic.helpers.model.PropertyType;

/**
 *
 * @author yurij
 */
@RunWith(Parameterized.class)
public class CompareDbTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {
                createDb(new HashMap<String, List<Map<String, Comparable>>>() {
                    {
                        put("test", Arrays.asList(
                                new HashMap<String, Comparable>() {
                            {
                                put("123", "test");
                            }
                        }
                        ));
                    }
                }),
                createDb(new HashMap<String, List<Map<String, Comparable>>>() {
                    {
                        put("test", Arrays.asList(
                                new HashMap<String, Comparable>() {
                            {
                                put("123", "test2");
                            }
                        }
                        ));
                    }
                }),
                new CompareDbResult(new HashMap<String, CompareTableResult>() {
                    {
                        put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                            {
                                put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF,
                                        new EntityView("0", "test", null, "0", Arrays.asList(
                                                new EntityProperty("123", new PropertyType(false, String.class.getName(), String.class.getSimpleName()), "test")
                                        ), null, null),
                                        new EntityView("0", "test", null, "0", Arrays.asList(
                                                new EntityProperty("123", new PropertyType(false, String.class.getName(), String.class.getSimpleName()), "test2")
                                        ), null, null)
                                ));
                            }
                        }));
                    }
                })
            }
        });
    }

    private final String db1;
    private final String db2;
    private final CompareDbResult expected;

    public CompareDbTest(String db1, String db2, CompareDbResult expected) {
        this.db1 = db1;
        this.db2 = db2;
        this.expected = expected;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        new File(db1).delete();
        new File(db2).delete();
    }

    @Test
    public void test1() {
        CompareDb cmp = new CompareDb(db1, db2);
        CompareDbResult result = cmp.compare();

        assertEquals(result, expected);
    }

    private static String createDb(Map<String, List<Map<String, Comparable>>> data) {
        String path = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString();

        new File(path).mkdir();

        final PersistentEntityStore entityStore = PersistentEntityStores.newInstance(path);
        entityStore.executeInTransaction(txn -> {
            data.entrySet().forEach(table -> {
                table.getValue().forEach(item -> {
                    final Entity message = txn.newEntity(table.getKey());

                    item.entrySet().forEach(field -> {
                        message.setProperty(field.getKey(), field.getValue());
                    });
                });
            });
        });
        entityStore.close();

        return path;
    }
}
