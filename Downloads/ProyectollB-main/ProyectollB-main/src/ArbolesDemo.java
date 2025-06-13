import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

// **Árbol AVL

// **Árbol B

// **Árbol B+

//Bùsqueda 

class NodoBPlus {
    List<Integer> claves;
    List<NodoBPlus> hijos;
    boolean esHoja;

    public NodoBPlus(int

 grado) {
        this.claves = new ArrayList<>(grado - 1);
        this.hijos = new ArrayList<>(grado);
        this.esHoja = true;
    }
}

class ArbolBPlus {
    private NodoBPlus raiz;
    private int grado;

    public ArbolBPlus(int grado) {
        this.grado = grado;
        this.raiz = new NodoBPlus(grado);
    }

    public void insertar(int clave) {
        NodoBPlus nodo = raiz;
        if (nodo.claves.size() == grado - 1) {
            NodoBPlus nuevoNodo = new NodoBPlus(grado);
            raiz = nuevoNodo;
            nuevoNodo.esHoja = false;
            nuevoNodo.hijos.add(nodo);
            dividir(nuevoNodo, nodo, 0);
            insertarEnNodo(nuevoNodo, clave);
        } else {
            insertarEnNodo(nodo, clave);
        }
    }

    private

 void insertarEnNodo(NodoBPlus nodo, int clave) {
        int i = nodo.claves.size() - 1;
        if (nodo.esHoja) {
            while (i >= 0 && clave < nodo.claves.get(i)) {
                i--;
            }
            nodo.claves.add(i + 1, clave);
        } else {
            while (i >= 0 && clave < nodo.claves.get(i)) {
                i--;
            }
            i++;
            NodoBPlus hijo = nodo.hijos.get(i);
            if (hijo.claves.size() == grado - 1) {
                dividir(nodo, hijo, i);
                if (clave > nodo.claves.get(i)) {
                    i++;
                }
            }
            insertarEnNodo(nodo.hijos.get(i), clave);
        }
    }

    private void dividir(NodoBPlus padre, NodoBPlus hijo, int indice) {
        NodoBPlus nuevoHijo = new NodoBPlus(grado);
        int mid = grado / 2;
        padre.claves.add(indice, hijo.claves.get(mid));
        padre.hijos.add(indice + 1, nuevoHijo);
        nuevoHijo.esHoja = hijo.esHoja;

        for (int i = mid + 1; i < hijo.claves.size(); i++) {
            nuevoHijo.claves.add(hijo.claves.get(i));
        }
        hijo.claves.subList(mid, hijo.claves.size()).clear();

        if (!hijo.esHoja) {
            for (int i = mid + 1; i < hijo.hijos.size(); i++) {
                nuevoHijo.hijos.add(hijo.hijos.get(i));
            }
            hijo.hijos.subList(mid + 1, hijo.hijos.size()).clear();
        }
    }

    public void buscar(int clave) {
        buscarEnNodo(raiz, clave, "");
    }

    private void buscarEnNodo(NodoBPlus nodo, int clave, String camino) {
        if (nodo == null) {
            System.out.println("No se encontró la clave.");
            return;
        }

        int i = 0;
        while (i < nodo.claves.size() && clave > nodo.claves.get(i)) {
            camino += "Verificando clave " + nodo.claves.get(i) + ": dirección derecha\n";
            i++;
        }

        if (i < nodo.claves.size() && clave == nodo.claves.get(i)) {
            camino += "Clave encontrada: " + clave + "\n";
            System.out.println(camino);
            return;
        }

        if (nodo.esHoja) {
            camino += "No se encontró la clave, el nodo más cercano es: " + (i > 0 ? nodo.claves.get(i - 1) : "Ninguna") + "\n";
            System.out.println(camino);
            return;
        }

        camino += "Verificando clave " + (i < nodo.claves.size() ? nodo.claves.get(i) : "Ninguna") + ": dirección izquierda\n";
        buscarEnNodo(nodo.hijos.get(i), clave, camino);
    }

    public void imprimirArbol() {
        Queue<NodoBPlus> queue = new LinkedList<>();
        queue.add(raiz);
        int nivel = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Nivel " + nivel + ": ");
            for (int i = 0; i < levelSize; i++) {
                NodoBPlus nodo = queue.poll();
                System.out.print(nodo.claves + " ");
                if (!nodo.esHoja) {
                    queue.addAll(nodo.hijos);
                }
            }
            System.out.println();
            nivel++;
        }
    }
}

public class ArbolesDemo {
    
    public static final String CYAN = "\u001B[36m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String RESET = "\u001B[0m";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        ArbolBPlus arbol = new ArbolBPlus(4);
        System.out.println(ROJO + "Proyecto Segundo Bimestre");
        System.out.println("Bienvenido al programa de árboles" + RESET);
        
        while (true) {
            try {
                System.out.println(CYAN +"\nSeleccione el tipo de árbol:" + RESET);
                System.out.println("1. Árbol AVL (no implementado)");
                System.out.println("2. Árbol B (no implementado)");
                System.out.println("3. Árbol B+");
                System.out.println("0. Salir");
                System.out.print("Ingrese el número del tipo de árbol: ");
                int tipoArbol = Integer.parseInt(scanner.nextLine());

                if (tipoArbol == 0) {
                    break;
                } else if (tipoArbol == 3) {
                    while (true) {
                        System.out.println(CYAN + "\nMenu:" + RESET);
                        System.out.println("1. Búsqueda de la clave de un nodo");
                        System.out.println("2. Inserción de un nuevo nodo en el arbol(no implementado)");
                        System.out.println("3. Eliminación de un nodo en el arbol(no implementado)");
                        System.out.println("4. Recorrido del arbol(no implementado)");
                        System.out.println("0. Volver al menú principal");
                        System.out.print("Ingrese el número de la operación: ");
                        int operacion = Integer.parseInt(scanner.nextLine());

                        if (operacion == 0) {
                            break;
                        } else if (operacion == 1) {
                            System.out.println(CYAN + "----------------------------------------------" + RESET);
                            for (int i = 0; i < 15; i++) {
                                arbol.insertar((int) (Math.random() * 100));
                            }
                            System.out.println("Arbol B+ generado");
                            arbol.imprimirArbol();
                            System.out.print("Ingrese la clave a buscar: ");
                            int claveBuscada = Integer.parseInt(scanner.nextLine());
                            arbol.buscar(claveBuscada);
                            System.out.println(CYAN + "----------------------------------------------" + RESET);
                            
                        } else if (operacion == 2) {
                            System.out.print("Ingrese la clave a insertar: ");
                            // Aquí iría la lógica de inserción
                        } else if (operacion == 4) {
                            System.out.println(CYAN + "----------------------------------------------" + RESET);
                            while (true) {
                                System.out.println(CYAN + "\nMenú de Recorridos:" + RESET);
                                System.out.println("1. Recorrido In-order");
                                System.out.println("2. Recorrido Pre-order");
                                System.out.println("3. Recorrido Post-order");
                                System.out.println("4. Recorrido Level-order");
                                System.out.println("5. Volver al menú principal");
                                System.out.print("Seleccione un recorrido: ");
                                
                                int recorrido = scanner.nextInt();
                                
                                switch (recorrido) {
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                       
                                        break;
                                    case 4:
                                        
                                        break;
                                    case 5:
                                        System.out.println("Volviendo al menú principal...");
                                        break;
                                    default:
                                        System.out.println("Opción no válida.");
                                }
                                if (recorrido == 5) break;
                            }
                            break;
                        } else {
                            System.out.println(ROJO + "Opción no válida." + RESET);
                        }
                    }
                } else {
                    System.out.println(ROJO + "Opción no válida." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ROJO + "Entrada no válida. Por favor, ingrese un número." + RESET);
            }
        }

        scanner.close();
    }
}

