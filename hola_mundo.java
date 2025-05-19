package curso_java;
import java.util.*;

public class hola_mundo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Carrito carrito = new Carrito();
        Pedido listaPedido = new Pedido();
        
        int opcion;
        do {
            mostrarMenu();
            opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> agregarProducto(sc, carrito);
                case 2 -> carrito.mostrarCarrito();
                case 3 -> actualizarProducto(sc, carrito);
                case 4 -> eliminarProducto(sc, carrito);
                case 5 -> crearPedido(sc, carrito, listaPedido);
                case 6 -> listaPedido.mostrarPedido();
                case 7 -> System.out.println("¡Gracias por usar el sistema!");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 7);

        sc.close();
    }

    public static void mostrarMenu() {
        System.out.println("\nSISTEMA DE GESTION - TECHLAB");
        System.out.println("1 - Agregar producto");
        System.out.println("2 - Listar productos");
        System.out.println("3 - Buscar / Actualizar producto");
        System.out.println("4 - Eliminar producto");
        System.out.println("5 - Crear un pedido");
        System.out.println("6 - Listar un pedido");
        System.out.println("7 - Salir");
        System.out.print("Elija una opción: ");
    }

    public static void agregarProducto(Scanner sc, Carrito carrito) {
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = sc.nextDouble();
        System.out.print("Stock: ");
        int stock = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        Producto producto = new Producto(nombre, precio, stock);
        carrito.agregarProductoCarrito(producto);
    }

    public static void actualizarProducto(Scanner sc, Carrito carrito) {
        System.out.print("Nombre del producto a buscar: ");
        String nombre = sc.nextLine();
        carrito.actualizarProducto(sc, nombre);
    }

    public static void eliminarProducto(Scanner sc, Carrito carrito) {
        System.out.print("Nombre del producto a eliminar: ");
        String nombre = sc.nextLine();
        carrito.eliminarProducto(nombre);
    }

    public static void crearPedido(Scanner sc, Carrito carrito, Pedido pedido) {
        String opcion;
        do {
            System.out.print("Nombre del producto: ");
            String nombre = sc.nextLine();
            System.out.print("Cantidad: ");
            int cantidad = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            Producto producto = carrito.obtenerProducto(nombre);
            if (producto != null) {
                pedido.agregarProductoPedido(new LineaPedido(producto, cantidad));
            } else {
                System.out.println("Producto no encontrado en el carrito.");
            }

            System.out.print("¿Desea agregar otro producto? (s/n): ");
            opcion = sc.nextLine();
        } while (!opcion.equalsIgnoreCase("n"));
    }
}

// ============================================

class Producto {
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio + " - Stock: " + stock;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
}

// ============================================

class Carrito {
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public void agregarProductoCarrito(Producto producto) {
        listaProductos.add(producto);
        System.out.println("Producto agregado: " + producto.getNombre());
    }

    public void mostrarCarrito() {
        if (listaProductos.isEmpty()) {
            System.out.println("Carrito vacío.");
            return;
        }

        System.out.println("Productos en el carrito:");
        for (int i = 0; i < listaProductos.size(); i++) {
            System.out.println((i + 1) + ". " + listaProductos.get(i));
        }
    }

    public void actualizarProducto(Scanner sc, String nombre) {
        boolean encontrado = false;
        for (Producto producto : listaProductos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                encontrado = true;
                System.out.println("Producto encontrado: " + producto);

                System.out.print("¿Desea actualizar precio o stock? (p/s): ");
                String opcion = sc.nextLine();

                if (opcion.equalsIgnoreCase("p")) {
                    System.out.print("Nuevo precio: ");
                    double nuevoPrecio = sc.nextDouble();
                    producto.setPrecio(nuevoPrecio);
                    sc.nextLine(); // limpiar buffer
                } else if (opcion.equalsIgnoreCase("s")) {
                    System.out.print("Nuevo stock: ");
                    int nuevoStock = sc.nextInt();
                    producto.setStock(nuevoStock);
                    sc.nextLine(); // limpiar buffer
                } else {
                    System.out.println("Opción inválida.");
                }
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Producto no encontrado.");
        }
    }

    public void eliminarProducto(String nombre) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getNombre().equalsIgnoreCase(nombre)) {
                listaProductos.remove(i);
                System.out.println("Producto eliminado exitosamente.");
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }

    public Producto obtenerProducto(String nombre) {
        for (Producto producto : listaProductos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }
}

// ============================================

class LineaPedido {
    Producto producto;
    int cantidad;

    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}

// ============================================

class Pedido {
    private ArrayList<LineaPedido> listaPedidos = new ArrayList<>();

    public void agregarProductoPedido(LineaPedido pedido) {
        if (pedido.producto.getStock() >= pedido.cantidad) {
            listaPedidos.add(pedido);
            pedido.producto.setStock(pedido.producto.getStock() - pedido.cantidad);
            System.out.println("Producto agregado al pedido.");
        } else {
            System.out.println("Stock insuficiente.");
        }
    }

    public double calcularTotalPedido() {
        double total = 0;
        for (LineaPedido lp : listaPedidos) {
            total += lp.calcularSubtotal();
        }
        return total;
    }

    public void mostrarPedido() {
        if (listaPedidos.isEmpty()) {
            System.out.println("El pedido está vacío.");
            return;
        }

        System.out.println("Detalles del pedido:");
        for (LineaPedido lp : listaPedidos) {
            System.out.println("- " + lp.producto.getNombre() + " x" + lp.cantidad +
                    " = $" + lp.calcularSubtotal());
        }
        System.out.println("Total del pedido: $" + calcularTotalPedido());
    }
}
