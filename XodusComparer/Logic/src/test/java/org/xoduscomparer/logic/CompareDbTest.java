/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xoduscomparer.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yurij
 */
public class CompareDbTest {

    public CompareDbTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {
        String db1 = createDb1();
        String db2 = createDb2();

        try {
            CompareDb cmp = new CompareDb(db1, db2);
            CompareDbResult result = cmp.compare();

            assertEquals(result.getTables().size(), 1);
        } finally {
            new File(db1).delete();
            new File(db2).delete();
        }
    }

    private String createDb1() {
        return createDb(new HashMap<String, List<Map<String, Comparable>>>() {
            {
                put("test", Arrays.asList(
                        new HashMap<String, Comparable>() {
                    {
                        put("123", "test");
                    }
                }
                ));
            }
        });
    }

    private String createDb2() {
        return createDb(new HashMap<String, List<Map<String, Comparable>>>() {
            {
                put("test", Arrays.asList(
                        new HashMap<String, Comparable>() {
                    {
                        put("123", "test2");
                    }
                }
                ));
            }
        });
    }

    private String createDb(Map<String, List<Map<String, Comparable>>> data) {
        String path = "/tmp/" + UUID.randomUUID().toString();

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
