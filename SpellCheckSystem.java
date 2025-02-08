import java.util.*;

// Node class for BST
class BSTNode {
    String word;
    BSTNode left, right;

    public BSTNode(String word) {
        this.word = word;
        left = right = null;
    }
}

// Binary Search Tree class
class BST {
    private BSTNode root;

    public BST() {
        root = null;
    }

    // Insert a word into the BST
    public void insert(String word) {
        root = insertRec(root, word);
    }

    private BSTNode insertRec(BSTNode root, String word) {
        if (root == null) {
            root = new BSTNode(word);
            return root;
        }
        if (word.compareToIgnoreCase(root.word) < 0) {
            root.left = insertRec(root.left, word);
        } else if (word.compareToIgnoreCase(root.word) > 0) {
            root.right = insertRec(root.right, word);
        }
        return root;
    }

    // Search for a word in the BST
    public boolean search(String word) {
        return searchRec(root, word);
    }

    private boolean searchRec(BSTNode root, String word) {
        if (root == null) {
            return false;
        }
        if (root.word.equalsIgnoreCase(word)) {
            return true;
        }
        if (word.compareToIgnoreCase(root.word) < 0) {
            return searchRec(root.left, word);
        }
        return searchRec(root.right, word);
    }

    // Get suggestions for a misspelled word using in-order traversal
    public void getSuggestions(String inputWord) {
        List<String> suggestions = new ArrayList<>();
        inOrderTraversalForSuggestions(root, inputWord, suggestions);
        if (suggestions.isEmpty()) {
            System.out.println("No suggestions available.");
        } else {
            System.out.println("Did you mean: " + suggestions);
        }
    }

    private void inOrderTraversalForSuggestions(BSTNode root, String inputWord, List<String> suggestions) {
        if (root != null) {
            inOrderTraversalForSuggestions(root.left, inputWord, suggestions);
            if (calculateEditDistance(root.word, inputWord) <= 2) {
                suggestions.add(root.word);
            }
            inOrderTraversalForSuggestions(root.right, inputWord, suggestions);
        }
    }

    // Edit distance calculation
    private int calculateEditDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    // Display all words in the BST in sorted order
    public void displayWords() {
        System.out.println("Words in the dictionary:");
        inOrderTraversalForDisplay(root);
    }

    private void inOrderTraversalForDisplay(BSTNode root) {
        if (root != null) {
            inOrderTraversalForDisplay(root.left);
            System.out.println(root.word);
            inOrderTraversalForDisplay(root.right);
        }
    }
}

// Main class
public class SpellCheckSystem {
    public static void main(String[] args) {
        BST dictionary = new BST();
        Scanner scanner = new Scanner(System.in);

        // Preload dictionary
        String[] words = {"apple", "banana", "orange", "grape", "peach", "pear"};
        for (String word : words) {
            dictionary.insert(word);
        }

        System.out.println("Binary Search Tree Spell Check System");
        while (true) {
            System.out.println("\nMenu:\n1. Check Spelling\n2. Add Word\n3. Display Words\n4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter a word to check: ");
                    String wordToCheck = scanner.nextLine();
                    if (dictionary.search(wordToCheck)) {
                        System.out.println("The word is correct.");
                    } else {
                        System.out.println("The word is misspelled.");
                        dictionary.getSuggestions(wordToCheck);
                    }
                    break;
                case 2:
                    System.out.print("Enter a word to add: ");
                    String wordToAdd = scanner.nextLine();
                    dictionary.insert(wordToAdd);
                    System.out.println("Word added to dictionary.");
                    break;
                case 3:
                    dictionary.displayWords();
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
