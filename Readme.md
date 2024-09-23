# Product Catalog with B+ Tree

## Description
This project implements a product catalog using a B+ Tree data structure. It allows for efficient insertion and searching of products based on their IDs. The project includes a product data generator to create sample products for testing.

## Features
- B+ Tree implementation for storing and retrieving products.
- Dynamic product generation with random names and prices.
- Search functionality to find products by their ID.

## Installation

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Maven (optional, for dependency management)

## Usage
The program generates 100 random products and inserts them into a B+ Tree. It then searches for a product with ID `2` and prints its details.

## Example Output
Found product: TechPro Elegant Laptop, Price: $123.45

## Yet to implement
- Concurrency support: for thread-safe operations.
- Improved search performance: Utilizes Collections.binarySearch() for faster key lookups.
- Range search functionality: Allows searching for products within a specified ID range.
- Persistence: Implements methods to save and load the B+ tree structure to/from disk.
- Error handling: Includes basic error handling and logging.
- Serialization: Makes relevant classes Serializable for easy persistence and network transfer if needed.
- Optimized node size: Uses a larger order (128) for better performance with large datasets.
