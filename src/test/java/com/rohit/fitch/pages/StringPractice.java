package com.rohit.fitch.pages;

public class StringPractice {

    public static void main(String args[]) {
        // String str = "test";
        // String str1 = "testing";
        // System.out.println(str == str1);// true
        // System.out.println(str.equals(str1));// false

        // System.out.println(str);

        // String strobj = new String("testingwithobj");
        // String strobj1 = new String("testingwithobj");
        // System.out.println(strobj == strobj1);// false
        // System.out.println(strobj.equals(strobj1));// false

        // String originalString = "Hello";
        // System.out.println("Original String: " + originalString);

        // String modifiedString = originalString.concat(" World");
        // System.out.println("Modified String: " + modifiedString);

        // System.out.println("Original String after modification attempt: " +
        // originalString);

        // StringBuffer sb = new StringBuffer("Hello");
        // sb.append(" World");
        // System.out.println("sb:" + sb);

        // StringBuilder sb1 = new StringBuilder("Hello");
        // sb1.append(" World");
        // System.out.println("sb1:" + sb1);

        String strMethod = "testing";
        System.out.println(strMethod.charAt(0));
        System.out.println("ABC".equalsIgnoreCase("abc"));
        System.out.println("ABC".equals("abc"));
        System.out.println("ABC".replace("C", "A"));
        System.out.println("ABCDEFGHIJ".toLowerCase());
        System.out.println("abcdefghi".toUpperCase());
        System.out.println(" abcd ".trim().equals("abcd"));
        System.out.println("apple".substring(0));
        System.out.println("apple".substring(1, 4));
        System.out.println("apple".substring(1, 5));
        int index = "aplpe".indexOf("p");
        System.out.println("aplpe".indexOf("p", index + 1));
    }

}
