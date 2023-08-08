
package ganador;import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Ganador {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            crearInterfaz();
        });
    }

    private static void crearInterfaz() {
        JFrame frame = new JFrame("Juego del Producto Más Grande");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JLabel resultadoLabel = new JLabel("Resultado Final: ");
        frame.add(resultadoLabel, BorderLayout.NORTH);

        JPanel panelIngreso = new JPanel();
        panelIngreso.setLayout(new FlowLayout());

        JTextField ingresoMultiplicacion = new JTextField(10);
        JButton verificarButton = new JButton("Verificar");
        verificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarMultiplicacion(textArea, ingresoMultiplicacion, frame);
            }
        });

        panelIngreso.add(new JLabel("Ingrese una multiplicación (ej: 10*15): "));
        panelIngreso.add(ingresoMultiplicacion);
        panelIngreso.add(verificarButton);
        frame.add(panelIngreso, BorderLayout.SOUTH);

        jugarJuego(textArea, resultadoLabel, frame);

        frame.setVisible(true);
    }

    private static void jugarJuego(JTextArea textArea, JLabel resultadoLabel, JFrame frame) {
        Random random = new Random();

        int numeroValores = random.nextInt(49) + 2; // Generar un número aleatorio entre 2 y 50
        int[] valores = new int[numeroValores];

        textArea.setText("");
        textArea.append("Valores generados aleatoriamente entre -100 y 100:\n");

        for (int i = 0; i < numeroValores; i++) {
            int valor = random.nextInt(201) - 100; // Generar un número aleatorio entre -100 y 100
            valores[i] = valor;
            textArea.append("Dato Generado " + (i + 1) + ": " + valor + "\n");
        }

        int cantidadMostrar = random.nextInt(numeroValores - 1) + 2; // Generar un número aleatorio entre 2 y numeroValores
        textArea.append("\nMostrando " + cantidadMostrar + " valores de forma aleatoria:\n");

        int[] indicesMostrar = new int[cantidadMostrar];
        for (int i = 0; i < cantidadMostrar; i++) {
            int indiceAleatorio;
            do {
                indiceAleatorio = random.nextInt(numeroValores);
            } while (yaExiste(indiceAleatorio, indicesMostrar, i));
            indicesMostrar[i] = indiceAleatorio;
            textArea.append("Valor " + (indiceAleatorio + 1) + ": " + valores[indiceAleatorio] + "\n");
        }

        int productoMasGrande = Integer.MIN_VALUE;
        int valor1 = 0, valor2 = 0;
        for (int i = 0; i < cantidadMostrar - 1; i++) {
            for (int j = i + 1; j < cantidadMostrar; j++) {
                int productoActual = valores[indicesMostrar[i]] * valores[indicesMostrar[j]];
                if (productoActual > productoMasGrande) {
                    productoMasGrande = productoActual;
                    valor1 = valores[indicesMostrar[i]];
                    valor2 = valores[indicesMostrar[j]];
                }
            }
        }

        textArea.append("\nEl producto más grande entre los valores " + valor1 + " y " + valor2 + " es: " + productoMasGrande + "\n");
        resultadoLabel.setText("Resultado Final: " + productoMasGrande);

        moverVentanaAleatoriamente(frame);
    }

    private static void verificarMultiplicacion(JTextArea textArea, JTextField ingresoMultiplicacion, JFrame frame) {
        String ingreso = ingresoMultiplicacion.getText().trim();
        try {
            String[] partes = ingreso.split("\\*");
            int num1 = Integer.parseInt(partes[0].trim());
            int num2 = Integer.parseInt(partes[1].trim());

            int resultadoFinal = obtenerResultadoFinal(textArea);
            int resultadoIngresado = num1 * num2;

            if (resultadoIngresado > resultadoFinal) {
                JOptionPane.showMessageDialog(null, "¡Ganaste! El resultado ingresado es mayor al resultado final del juego.");
            } else {
                JOptionPane.showMessageDialog(null, "¡Perdiste! El resultado ingresado no es mayor al resultado final del juego.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese una multiplicación válida (ej: 10*15).");
        }
    }

    private static int obtenerResultadoFinal(JTextArea textArea) {
        String resultadoFinal = textArea.getText().substring(textArea.getText().lastIndexOf(":") + 1).trim();
        return Integer.parseInt(resultadoFinal);
    }

    private static boolean yaExiste(int indice, int[] indices, int longitud) {
        for (int i = 0; i < longitud; i++) {
            if (indices[i] == indice) {
                return true;
            }
        }
        return false;
    }

    private static void moverVentanaAleatoriamente(JFrame frame) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int duration = 20; // Duration of animation in milliseconds

                long startTime = System.currentTimeMillis();
                long endTime = startTime + duration;
                int startX = frame.getLocation().x;
                int startY = frame.getLocation().y;

                while (System.currentTimeMillis() < endTime) {
                    int newX = random.nextInt(800); // Set the maximum x-coordinate to 800
                    int newY = random.nextInt(600); // Set the maximum y-coordinate to 600
                    frame.setLocation(newX, newY);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // After the animation, reset the frame position to its original location
                frame.setLocation(startX, startY);
            }
        });
        thread.start();
    }
}
