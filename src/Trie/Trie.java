package Trie;




import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Trie {

    // Stufe 2
    static class TrieNode {

        SearchTree searchTree;

        // Konstruktor für leere TrieNode
        TrieNode() {
            // neuen search tree initializieren
            searchTree = new SearchTree();

        }

        // Konstruktor, erzeugt mit jedem TrieNode einen neuen searchTree
        TrieNode(char c, int count) {
            // neuen search tree initializieren
            searchTree = new SearchTree();
            add(c, count);

        }

        // a), fügt c zum SearchTree hinzu,
        public void add(char c, int count) {
            searchTree.inc(c, count);
        }

        // b)
        public TrieNode get(char c) {
            return searchTree.getNode(c);
        }

        // Kind mit höchsten Count merken
        public TrieNode maxCountNode() {
            if (searchTree.updateNode == null) {
                return null;
            }
            return searchTree.updateNode.trieNode;
        }
    }

    // Stufe 1
    public static class SearchTree {

        Node root; // wurzel
        Node updateNode;// hält Knoten mit höchstem count

        class Node {

            Node left; // Referenz auf linkes Kind
            Node right; // Referenz auf rechtes Kind
            char key; // Buchstabe als Schlüssel
            long count; // Zähler
            TrieNode trieNode;

            // Konstruktor for Nodes
            Node(char key, int count) {
                this.key = key;
                this.count = count;
                // Wenn ein neuer Knoten angelegt wird, dann auch direkt ein trieNode Knoten
                trieNode = new TrieNode();
            }
        }

        public SearchTree() {
            root = null;
        }

        public void inc(char key, int count) {
            if (root == null) {
                // insertroot node into searchTree
                root = new Node(key, count);
                // Da root der einzige Knoten ist wird er auch maxNode
                updateNode = root;
                return;
            } // Ansonsten ruf inc mit Parametern auf
            inc(root, null, key, count);
        }

        // überladene inc Methode um Knoten zu finden
        private void inc(Node n, Node parent, char key, int count) {

            if (n == null) {
                Node newNode = new Node(key, count);
                // neuen Knoten unter parent hinzufügen
                if (key < parent.key) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }
                // schauen ob max null ist und count größer als aktueller maxCount
                incUpdateNodeCount(newNode, key, count);
                return;
            }

            if (n.key == key) {
                incFittingKeyNodeCount(n, key, count);
                return;
            }
            // key in den beiden teilhälften suchen
            if (key < n.key) {
                inc(n.left, n, key, count);
            } else {
                inc(n.right, n, key, count);
            }
        }

        //
        /*
         * public int getCount(char key) { //holt mir der get Methode den richtigen
         * Knoten //und prüft auf null oder gibt den count zurück Node n =
         * getSearchTreeNode(root,key); if(n == null) { return 0; } return (int)
         * n.count; //return (int) ((n == null) ? 0 : n.count); }
         */
        // gibt Objekt TrieNode zurück,
        public TrieNode getNode(char key) {
            Node n = getSearchTreeNode(root, key);
            if (n == null) {
                return null;
            }
            return n.trieNode;
            // return(n == null) ? null : n.trieNode;
        }

        private Node getSearchTreeNode(Node n, char key) {
            // Wenn kein Knoten besteht, return null
            if (n == null) {
                return null;
            }
            // Wenn n den Schlüssel enthält, return n
            if (n.key == key) {
                return n;
            }
            // Ansonsten rekursiver Aufruf der beiden teilbäume
            // Wenn der Schlüssel kleiner als der von n ist, dann gehe nach links,
            // sonst nach Rechts
            if (key < n.key) {
                return getSearchTreeNode(n.left, key);
            } else {
                return getSearchTreeNode(n.right, key);
            }
        }

        public void incUpdateNodeCount(Node n, char key, int count) {
            if (updateNode == null || count > updateNode.count) {
                updateNode = n; // update maxCountNode
            }
        }

        // richtigen key gefunden, also count erhöhen
        public void incFittingKeyNodeCount(Node n, char key, int count) {
            n.count += count;
            if (updateNode == null || n.count > updateNode.count) {
                updateNode = n; // update maxCountNode;
            }
        }

        public long getCount(Node n, char key) {
            if (n == null) {
                return 0;
            }
            long leftSide = getCount(n.left, n.key);
            long rightSide = getCount(n.right, n.key);
            // 1.Fall: Übergegeben key gefunden
            if (n.key == key) {
                return n.count;
                // 2: Fall gesuchte key ist kleiner als key dann die linken Teil anschauen
            } else if (n.key > key) {
                return leftSide;
            } else
                // 3. Fall: Sonst den den rechten teil anschauen
                return rightSide;

        }

        public long getCount(char key) {
            if (root == null)
                return 0;
            Node n = root;
            long c = getCount(n, n.key);
            return c;
        }

    }

    // Trie-Klasse

    TrieNode root;

    // Stufe 3
    public Trie() {
        root = new TrieNode('*', 0);
    }

    public void add(String s, int count) {
        TrieNode tnode = root;
        char[] charArray = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            tnode.add(charArray[i], count);
            tnode = tnode.get(charArray[i]);
        }
        // stern signalisiert ende des Strings
        tnode.add('*', count);
    }

    public String predict(String prefix) {
        TrieNode travNode = root;
        if (travNode == null) {
            return null;
        }
        char[] charPrefix = prefix.toCharArray();
        for (int i = 0; i < prefix.length(); i++) {
            travNode = travNode.get(charPrefix[i]);
            if (travNode == null) {
                return null;
            }
        }

        StringBuilder result = new StringBuilder(prefix);
        // Durch Trie gehen bis wir '*' finden
        while (travNode.searchTree.updateNode.key != '*') {
            // char mit höchstem count an prefix anhängen
            char c = travNode.searchTree.updateNode.key;
            result.append(c);

            // nächsten höchsten count suchen und in travNode legen
            travNode = travNode.maxCountNode();
        }
        // return
        return result.toString();
    }

    public static void eval() {
        // neuen Trie erzeugen
        Trie trie = new Trie();
        // Datei einlesen
        try (Stream<String> lines = Files.lines(Paths.get("keyphrases.txt"))) {
            lines.forEach(line -> {
                // nach semikolon wert splitten
                String[] s = line.split(";");
                // zu Trie hinzufügen
                trie.add(s[0], Integer.parseInt(s[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Testdaten predicten
        String[] testArray = { "trump", "german", "mo", "paw", "secret", "best", "pro", "small", "snow", "soc" };
        int count = 0;
        while (testArray.length > count) {

            String temp;
            temp = testArray[count];
            System.out.println(trie.predict(temp));
            count++;
        }

    }

    public static void main(String[] args) {
        eval();
    }

}

