import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Матрицы комплексных чисел ===");
        while (true) {
            System.out.println("\nМеню:");
            System.out.println(" 1. A + B");
            System.out.println(" 2. A * B");
            System.out.println(" 3. A / B   (A * B^(-1))");
            System.out.println(" 4. Транспонирование A");
            System.out.println(" 5. Определитель A");
            System.out.println(" 0. Выход");
            System.out.print("Ваш выбор: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Некорректный ввод.");
                continue;
            }
            if (choice == 0) break;

            try {
                switch (choice) {
                    case 1, 2, 3 -> {
                        Matrix a = readMatrix(sc, "A");
                        Matrix b = readMatrix(sc, "B");
                        Matrix r = switch (choice) {
                            case 1 -> a.add(b);
                            case 2 -> a.multiply(b);
                            default -> a.divide(b);
                        };
                        System.out.println("Результат:");
                        System.out.print(r);
                    }
                    case 4 -> {
                        Matrix a = readMatrix(sc, "A");
                        System.out.println("Результат:");
                        System.out.print(a.transpose());
                    }
                    case 5 -> {
                        Matrix a = readMatrix(sc, "A");
                        System.out.println("Определитель: " + a.determinant());
                    }
                    default -> System.out.println("Такого пункта нет.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        sc.close();
    }

    /** Формат: сначала "rows cols", затем для каждой ячейки строка "re im"
     *  (одно число = действительное, im = 0). */
    private static Matrix readMatrix(Scanner sc, String name) {
        System.out.println("Ввод матрицы " + name + ":");
        System.out.print("  Размер (строки столбцы): ");
        String[] sz = sc.nextLine().trim().split("\\s+");
        int r = Integer.parseInt(sz[0]);
        int c = Integer.parseInt(sz[1]);
        Matrix m = new Matrix(r, c);
        System.out.println("  Элементы построчно, формат: re im  (или просто re)");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print("  " + name + "[" + i + "][" + j + "]: ");
                String[] p = sc.nextLine().trim().split("\\s+");
                double re = Double.parseDouble(p[0]);
                double im = (p.length > 1) ? Double.parseDouble(p[1]) : 0.0;
                m.set(i, j, new Complex(re, im));
            }
        }
        return m;
    }
}