package Trie;




import static org.junit.Assert.*;

import org.junit.Test;

public class TrieTest {

    Trie.SearchTree simpsons, empty, left, right, rootOnly, duplicates;

    public void setup() {

        // Simpsons tree
        simpsons = new Trie.SearchTree();
        char[] values = { 'l', 'h', 'm', 'M', 'b', 'o'};
        int[] counts = { 8, 42, 1, 39, 10, 42 };
        for (int i = 0; i < values.length; ++i) {
            simpsons.inc(values[i], counts[i]);
        }

        // empty tree
        empty = new Trie.SearchTree();

        // left
        left = new Trie.SearchTree();
        left.inc('d', 3);
        left.inc('c', 3);
        left.inc('b', 3);
        left.inc('a', 3);

        // right
        right = new Trie.SearchTree();
        right.inc('a', 3);
        right.inc('b', 3);
        right.inc('c', 3);
        right.inc('d', 3);

        // root only
        rootOnly = new Trie.SearchTree();
        rootOnly.inc('a', 3);

        // duplicates
        duplicates = new Trie.SearchTree();
        duplicates.inc('b', 2);
        duplicates.inc('a', 3);
        duplicates.inc('c', 1);
        duplicates.inc('b', 4);
        duplicates.inc('a', 5);

    }



    @Test
    public void testSearchTree1() {
        setup();
        assertEquals ( 'l', simpsons.root.key );
        assertEquals ( 'h', simpsons.root.left.key );
        assertEquals ( 'm', simpsons.root.right.key );
        assertEquals ( 'M', simpsons.root.left.left.key );
        assertEquals ( 'b', simpsons.root.left.left.right.key );
        assertEquals ( 'o', simpsons.root.right.right.key );
        assertEquals ( null, simpsons.root.left.right);
    }

    @Test
    public void testSearchTree2() {
        setup();
        assertEquals ( null, empty.root );
    }

    @Test
    public void testSearchTree3() {
        setup();
        assertEquals ( null, rootOnly.root.left );
        assertEquals ( null, rootOnly.root.right );
    }

    @Test
    public void testSearchTree4() {
        setup();
        assertEquals ( 'd', left.root.key );
        assertEquals ( 'c', left.root.left.key );
        assertEquals ( 'b', left.root.left.left.key );
        assertEquals ( 'a', left.root.left.left.left.key );
        assertEquals ( 3, left.root.count );
        assertEquals ( 3, left.root.left.count );
        assertEquals ( 3, left.root.left.left.count );
        assertEquals ( 3, left.root.left.left.left.count );
    }

    @Test
    public void testSearchTree5() {
        setup();
        assertEquals ( 'a', right.root.key );
        assertEquals ( 'b', right.root.right.key );
        assertEquals ( 'c', right.root.right.right.key );
        assertEquals ( 'd', right.root.right.right.right.key );
        assertEquals ( 3, right.root.count );
        assertEquals ( 3, right.root.right.count );
        assertEquals ( 3, right.root.right.right.count );
        assertEquals ( 3, right.root.right.right.right.count );
    }

    @Test
    public void testSearchTree6() {
        setup();
        assertEquals ( 'b', duplicates.root.key );
        assertEquals ( 'a', duplicates.root.left.key );
        assertEquals ( 'c', duplicates.root.right.key );
        assertEquals ( 6, duplicates.root.count );
        assertEquals ( 8, duplicates.root.left.count );
        assertEquals ( 1, duplicates.root.right.count );
    }

    @Test
    public void testSearchTree9() {
        setup();

        assertEquals ( 8, simpsons.getCount('l') );
        assertEquals ( 42, simpsons.getCount('h') );
        assertEquals ( 1, simpsons.getCount('m') );
        assertEquals ( 39, simpsons.getCount('M') );
        assertEquals ( 10, simpsons.getCount('b') );
        assertEquals ( 42, simpsons.getCount('o') );

        assertEquals ( 42, simpsons.getCount('o') );
    }

    @Test
    public void testSearchTree10() {
        setup();
        assertTrue( empty.getCount('a') <= 0);
        assertEquals ( 3, rootOnly.getCount('a') );
    }

    @Test
    public void testTrie1() {
        Trie trie = new Trie();
        trie.add("abc", 1);
        assertEquals( "abc", trie.predict("") );
        assertEquals( "abc", trie.predict("a") );
        assertEquals( "abc", trie.predict("ab") );
        assertEquals( "abc", trie.predict("abc") );
        assertEquals( null, trie.predict("abcd") );
        assertEquals( null, trie.predict("bc") );
    }

    @Test
    public void testTrie2() {
        Trie trie = new Trie();
        System.out.println(trie.predict(""));
        assertEquals( "", trie.predict("") );
        assertEquals( null, trie.predict("A") );
    }

    @Test
    public void testTrie3() {
        Trie trie = new Trie();
        trie.add("aa", 5);
        trie.add("aaa", 4);
        assertEquals( "aa", trie.predict("") );
        assertEquals( "aa", trie.predict("a") );
        assertEquals( "aa", trie.predict("aa") );
        assertEquals( "aaa", trie.predict("aaa") );
        assertEquals( null, trie.predict("aaaa") );
    }

    @Test
    public void testTrie4() {
        Trie trie = new Trie();
        trie.add("ab", 10);
        trie.add("abc", 2);
        trie.add("cd", 6);
        trie.add("cde", 7);
        assertEquals( "cde", trie.predict("") );
        assertEquals( "cde", trie.predict("c") );
        assertEquals( "cde", trie.predict("cd") );
        assertEquals( "cde", trie.predict("cde") );
        assertEquals( "ab", trie.predict("a") );
    }

}

