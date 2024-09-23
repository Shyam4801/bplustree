import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Product {
    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class BPlusTreeNode {
    List<Integer> keys;
    List<Object> values;
    BPlusTreeNode next;
    BPlusTreeNode parent; // {{ edit_1 }}
    boolean isLeaf;

    public BPlusTreeNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        keys = new ArrayList<>();
        values = new ArrayList<>();
        next = null;
        parent = null; // {{ edit_2 }}
    }
}

class BPlusTree {
    private BPlusTreeNode root;
    private int order;

    public BPlusTree(int order) {
        this.order = order;
        root = new BPlusTreeNode(true);
    }

    public void insert(int key, Product product) {
        if (root == null) {
            root = new BPlusTreeNode(true);
        }
        insert(root, key, product);
    }

    private void insert(BPlusTreeNode node, int key, Product product) {
        if (node.isLeaf) {
            int insertionPoint = 0;
            while (insertionPoint < node.keys.size() && node.keys.get(insertionPoint) < key) {
                insertionPoint++;
            }
            node.keys.add(insertionPoint, key);
            node.values.add(insertionPoint, product);

            if (node.keys.size() > order - 1) {
                split(node);
            }
        } else {
            int i = 0;
            while (i < node.keys.size() && key > node.keys.get(i)) {
                i++;
            }
            insert((BPlusTreeNode) node.values.get(i), key, product);
        }
    }

    private void split(BPlusTreeNode node) {
        int midIndex = node.keys.size() / 2;
        BPlusTreeNode newNode = new BPlusTreeNode(node.isLeaf);

        newNode.keys = new ArrayList<>(node.keys.subList(midIndex, node.keys.size()));
        newNode.values = new ArrayList<>(node.values.subList(midIndex, node.values.size()));

        if (!node.isLeaf) {
            for (Object child : newNode.values) {
                ((BPlusTreeNode) child).parent = newNode;
            }
        }

        int newKey = node.keys.get(midIndex);
        node.keys = new ArrayList<>(node.keys.subList(0, midIndex));
        node.values = new ArrayList<>(node.values.subList(0, midIndex));

        if (node == root) {
            BPlusTreeNode newRoot = new BPlusTreeNode(false);
            newRoot.keys.add(newKey);
            newRoot.values.add(node);
            newRoot.values.add(newNode);
            root = newRoot;
            node.parent = newRoot;
            newNode.parent = newRoot;
        } else {
            BPlusTreeNode parent = node.parent;
            int insertionPoint = 0;
            while (insertionPoint < parent.keys.size() && parent.keys.get(insertionPoint) < newKey) {
                insertionPoint++;
            }
            parent.keys.add(insertionPoint, newKey);
            parent.values.add(insertionPoint + 1, newNode);
            newNode.parent = parent;

            if (parent.keys.size() > order - 1) {
                split(parent);
            }
        }

        if (node.isLeaf) {
            newNode.next = node.next;
            node.next = newNode;
        }
    }

    public Product search(int key) {
        BPlusTreeNode node = root;
        while (!node.isLeaf) {
            int i = 0;
            while (i < node.keys.size() && key > node.keys.get(i)) {
                i++;
            }
            node = (BPlusTreeNode) node.values.get(i);
        }

        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                return (Product) node.values.get(i);
            }
        }
        return null;
    }
}

class ProductDataGenerator {
    private static final String[] ADJECTIVES = {"Smart", "Elegant", "Durable", "Compact", "Wireless", "Portable", "High-Performance", "Eco-Friendly", "Ergonomic", "Premium"};
    private static final String[] PRODUCTS = {"Laptop", "Smartphone", "Headphones", "Smartwatch", "Tablet", "Camera", "Printer", "Monitor", "Keyboard", "Mouse"};
    private static final String[] BRANDS = {"TechPro", "ElectroMax", "InnoGadget", "SmartTech", "FutureBrand", "NextGen", "TechNova", "QuantumTech", "CyberCore", "OmegaElectronics"};

    private Random random;

    public ProductDataGenerator() {
        this.random = new Random();
    }

    public List<Product> generateProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String name = generateProductName();
            double price = generatePrice();
            products.add(new Product(i, name, price));
        }
        return products;
    }

    private String generateProductName() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String product = PRODUCTS[random.nextInt(PRODUCTS.length)];
        String brand = BRANDS[random.nextInt(BRANDS.length)];
        return brand + " " + adjective + " " + product;
    }

    private double generatePrice() {
        return 10 + (1000 - 10) * random.nextDouble(); // Prices between $10 and $1000
    }
}
class ProductCatalogSearch {
    private BPlusTree bPlusTree;

    public ProductCatalogSearch(int order) {
        bPlusTree = new BPlusTree(order);
    }

    public void addProduct(Product product) {
        bPlusTree.insert(product.id, product);
    }

    public Product searchProduct(int id) {
        return bPlusTree.search(id);
    }
}

public class Main {
    public static void main(String[] args) {
        ProductDataGenerator generator = new ProductDataGenerator();
        ProductCatalogSearch catalog = new ProductCatalogSearch(4);

        // Generate and add products
        List<Product> products = generator.generateProducts(100); // Generate 100 products
        for (Product product : products) {
            catalog.addProduct(product);
        }

        // Search for a product
        Product foundProduct = catalog.searchProduct(2);
        if (foundProduct != null) {
            System.out.println("Found product: " + foundProduct.name + ", Price: $" + String.format("%.2f", foundProduct.price));
        } else {
            System.out.println("Product not found");
        }
    }
}