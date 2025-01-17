# Byte Me - Food Ordering System (CLI)

Byte Me is a CLI-based food ordering application where customers can browse a menu, add items to their cart, and place orders. Admin users can manage the menu, view pending orders, and generate sales reports. The system includes VIP customer handling and order prioritization.

## Features

### Admin Functionality
1. **Manage Menu**: Add, update, or remove items from the menu.
2. **View Pending Orders**: See all pending orders, with VIP orders prioritized.
3. **Update Order Status**: Change the status of any pending order.
4. **Process Refunds**: Handle refunds for cancelled or denied orders.
5. **Generate Daily Sales Report**: View the daily summary of completed orders, including total sales.

### Customer Functionality
1. **Browse Menu**: View, search, and filter available menu items.
2. **Cart Management**: Add items to the cart, modify quantities, and add special requests.
3. **Order Tracking**: Track the status of placed orders and view order history.
4. **Order Cancellation**: Cancel orders that are still in the "Received" status.
5. **Leave Review**: Customers can leave reviews for their orders.

### VIP Customer Handling
- **VIP Priority**: VIP customers’ orders are prioritized in the pending order list.
- **Upgrade to VIP**: Customers can upgrade to VIP status for prioritized order handling.

### Data Persistence
The application saves menu items and orders, which can be reloaded during subsequent application runs.

## Project Structure

The project is organized into several main packages and classes:

### 1. **main.java.byteme.CLI** - Command Line Interface (CLI)

- **AdminCLI**: Provides admin functionality, including menu management, order viewing, status updating, refunds, and report generation.
- **CustomerCLI**: Provides customer functionality for browsing the menu, managing the cart, tracking orders, and leaving reviews.

### 2. **main.java.byteme.model** - Model Classes

- **Customer**: Represents a customer with attributes for name and type (VIP or Regular).
- **MenuItem**: Represents a menu item with attributes for name, price, category, and availability.
- **Order**: Represents an order with details such as order ID, customer, items, status, special requests, and order time.
- **OrderItem**: Represents an item within an order, including its quantity and any special requests.
- **Cart**: Manages items in the customer's cart, allowing addition, removal, and checkout.

### 3. **main.java.byteme.service** - Service Classes

- **MenuService**: Manages menu operations, including loading, saving, and filtering menu items.
- **OrderService**: Manages order operations, including order placement, status updates, and retrieval of pending/completed orders.

### 4. **main.java.byteme.util** - Utility Classes

- **OrderComparator**: Comparator class used to prioritize VIP customers’ orders over regular customers’ orders in the queue.

### 5. Main Class

- **Main**: The entry point of the application, where users select between `Admin` and `Customer` interfaces.

## Usage

1. **Run the application** by compiling and executing the `Main` class:
   ```bash
   javac main.java.byteme.Main.java
   java main.java.byteme.Main
   