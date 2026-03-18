package org.example.hm1;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;


public class CustomHashMapTest {
    @Test
    public void simplePut() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 16; i++) {
            customHashMap.put("key_" + i, i);
        }
        Assertions.assertEquals(16, customHashMap.size());

        for (int i = 16; i < 32; i++) {
            customHashMap.put("key_" + i, i);
        }
        Assertions.assertEquals(32, customHashMap.size());
    }

    @Test
    public void simpleRemove() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 16; i++) {
            customHashMap.put("key_" + i, i);
        }
        var removeResult1 = customHashMap.remove("1");
        Assertions.assertEquals(null, removeResult1);
    }

    @Test
    public void simpleRemove1() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 16; i++) {
            customHashMap.put("key_" + i, i);
        }
        var removeResult1 = customHashMap.remove("key_1");
        Assertions.assertEquals((Integer) 1, removeResult1);
    }

    @Test
    public  void  removeNestedElement() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 100; i++) {
            customHashMap.put("key_" + i, i);
        }
        // Для захода в debug. Коллизия по индексу 0, ключ "key_11", nextNode=key_99
        customHashMap.put("key_6666", 666);
        customHashMap.remove("key_99");

        Assertions.assertEquals(100, customHashMap.size());
    }

    @Test
    public void simpleSize() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 2; i++) {
            customHashMap.put("key_" + i, i);
        }
        customHashMap.remove("key_1");
        Assertions.assertEquals(1, customHashMap.size());

        customHashMap.remove("key_2");
        Assertions.assertEquals(1, customHashMap.size());

        customHashMap.remove("key_0");
        Assertions.assertEquals(0, customHashMap.size());
    }

    @Test
    public void advancedSize() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 100; i++) {
            customHashMap.put("key_" + i, i);
        }
        // Коллизия по индексу 0, ключ "key_11"
        customHashMap.remove("key_11");

        Assertions.assertEquals(99, customHashMap.size());
    }

    @Test
    public void clear() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 66; i++) {
            customHashMap.put("key_" + i, i);
        }
        Assertions.assertEquals(66, customHashMap.size());

        customHashMap.clear();
        Assertions.assertEquals(0, customHashMap.size());
    }

    @Test
    public  void  getNestedElement() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 100; i++) {
            customHashMap.put("key_" + i, i);
        }
        // Коллизия по индексу 0, ключ "key_11", nextNode=key_99
        customHashMap.put("key_6666", 666);

        var result = customHashMap.get("key_99");
        Assertions.assertEquals((Integer) 99, result);

        var result1 = customHashMap.get("key_11");
        Assertions.assertEquals((Integer) 11, result1);
    }

    @Test
    public void isEmpty() {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>(16);

        Assertions.assertEquals(0, customHashMap.size());
        Assertions.assertEquals(true, customHashMap.isEmpty());

        customHashMap.putAll(Map.of("1", 22, "2", 33));
        Assertions.assertEquals(false, customHashMap.isEmpty());
    }
}
