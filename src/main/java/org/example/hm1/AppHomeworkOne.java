package org.example.hm1;

public class AppHomeworkOne {
    public static void main( String[] args )
    {
        CustomHashMap<String, Integer> customHashMap = new CustomHashMap<>();
        for (int i = 0; i < 10; i++) customHashMap.put("key_" + i, i);

        System.out.println("CustomHashMap пустой? - " + customHashMap.isEmpty());

        System.out.println("\nВывод customHashMap:");
        System.out.println(customHashMap.toString());

        System.out.println(customHashMap.size());
    }
}
