package org.xoduscomparer.logic;

import java.io.ByteArrayInputStream;
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
import jetbrains.exodus.entitystore.PersistentEntityId;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import jetbrains.exodus.env.Environments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xoduscomparer.logic.helpers.model.EntityBlob;
import org.xoduscomparer.logic.helpers.model.EntityLink;
import org.xoduscomparer.logic.helpers.model.EntityProperty;
import org.xoduscomparer.logic.helpers.model.EntityView;

/**
 *
 * @author yurij
 */
@RunWith(Parameterized.class)
public class CompareDbTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            getEqualDbs(),
            getDbWithOneEqualTableAndOneDiffObject(),
            getDbWithDiffTables(),
            getDbWithDiffTables2(),
            getDbWithDiffObjects(),
            getDbWithEqualLinks(),
            getDbWithDiffLinks(),
            getDbWithEqualBlobs(), 
            getDbWithDiffBlobs()
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
    public void test() {
        CompareDb cmp = new CompareDb(db1, db2);
        CompareDbResult result = cmp.compare();

        assertEquals(result, expected);
    }

    private static Object[] getEqualDbs() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithOneEqualTableAndOneDiffObject() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test2")
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    )),
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test2")
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithDiffTables() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test2", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test3", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test2", new CompareTableResult(CompareState.EXIST_ONLY_FIRST, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_ONLY_FIRST,
                                    new EntityView("0", "test2", "test2[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                    put("test3", new CompareTableResult(CompareState.EXIST_ONLY_SECOND, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_ONLY_SECOND,
                                    new EntityView("0", "test3", "test3[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithDiffTables2() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                    put("test2", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                    put("test3", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL,
                                    new EntityView("0", "test", "test[0]", "1", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                    put("test2", new CompareTableResult(CompareState.EXIST_ONLY_FIRST, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_ONLY_FIRST,
                                    new EntityView("0", "test2", "test2[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                    put("test3", new CompareTableResult(CompareState.EXIST_ONLY_SECOND, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_ONLY_SECOND,
                                    new EntityView("0", "test3", "test3[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithDiffObjects() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test1"),
                                    new EntityProperty("1234", "test")
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test2"),
                                    new EntityProperty("1235", "test")
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test1"),
                                            new EntityProperty("1234", "test")
                                    )),
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test2"),
                                            new EntityProperty("1235", "test")
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithEqualLinks() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ), Arrays.asList(
                                    new EntityLink("123", 0, 0)
                            ), null)
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ), Arrays.asList(
                                    new EntityLink("123", 0, 0)
                            ), null)
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), Arrays.asList(
                                            new EntityLink("123", 0, 0)
                                    ), null)
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithDiffLinks() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            )),
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ), Arrays.asList(
                                    new EntityLink("123", 0, 0)
                            ), null)
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            )),
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ), Arrays.asList(
                                    new EntityLink("123", 0, 1)
                            ), null)
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ))
                            ));
                            put(1L, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF,
                                    new EntityView("1", "test", "test[1]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), Arrays.asList(
                                            new EntityLink("123", 0, 0)
                                    ), null),
                                    new EntityView("1", "test", "test[1]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), Arrays.asList(
                                            new EntityLink("123", 0, 1)
                                    ), null)
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithEqualBlobs() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ),
                            null,
                            Arrays.asList(
                                    new EntityBlob("123", new byte[0])
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ),
                            null,
                            Arrays.asList(
                                    new EntityBlob("123", new byte[0])
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_EQUAL,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), 
                                    null,
                                    Arrays.asList(
                                            new EntityBlob("123", new byte[0])
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static Object[] getDbWithDiffBlobs() {
        return new Object[]{
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ),
                            null,
                            Arrays.asList(
                                    new EntityBlob("123", new byte[0])
                            ))
                    ));
                }
            }),
            createDb(new HashMap<String, List<EntityView>>() {
                {
                    put("test", Arrays.asList(
                            new EntityView(Arrays.asList(
                                    new EntityProperty("123", "test")
                            ),
                            null,
                            Arrays.asList(
                                    new EntityBlob("123", new byte[1])
                            ))
                    ));
                }
            }),
            new CompareDbResult(new HashMap<String, CompareTableResult>() {
                {
                    put("test", new CompareTableResult(CompareState.EXIST_BOTH, new HashMap<Long, CompareObjectResult>() {
                        {
                            put(0L, new CompareObjectResult(CompareState.EXIST_BOTH_DIFF,
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), 
                                    null,
                                    Arrays.asList(
                                            new EntityBlob("123", new byte[0])
                                    )),
                                    new EntityView("0", "test", "test[0]", "0", Arrays.asList(
                                            new EntityProperty("123", "test")
                                    ), 
                                    null,
                                    Arrays.asList(
                                            new EntityBlob("123", new byte[1])
                                    ))
                            ));
                        }
                    }));
                }
            })
        };
    }

    private static String createDb(Map<String, List<EntityView>> data) {
        String path = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString();

        new File(path).mkdir();

        final PersistentEntityStore entityStore = PersistentEntityStores.newInstance(Environments.newInstance(path), null);
        entityStore.executeInTransaction(txn -> {
            data.entrySet().forEach(table -> {
                table.getValue().forEach(item -> {
                    final Entity message = txn.newEntity(table.getKey());

                    item.getProperties().forEach(property -> {
                        message.setProperty(property.getName(), property.getValue());
                    });

                    if (item.getLinks() != null) {
                        item.getLinks().forEach(link -> {
                            final Entity entity = txn.getEntity(new PersistentEntityId(link.getTypeId(), link.getEntityId()));
                            message.setLink(link.getName(), entity);
                        });
                    }

                    if (item.getBlobs() != null) {
                        item.getBlobs().forEach(blob -> {
                            message.setBlob(blob.getName(), new ByteArrayInputStream(blob.getData()));
                        });
                    }
                });
            });
        });
        entityStore.close();

        return path;
    }
}
