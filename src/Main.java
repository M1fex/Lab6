import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static RedBlackTree tree = new RedBlackTree();
    private static int[] lastGenerated;

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Ваш вибір: ");

            switch (choice) {
                case 1 -> {
                    int value = readInt("Введіть число: ");
                    System.out.println(tree.insert(value)
                            ? "Число додано." : "Таке число вже є в дереві.");
                }
                case 2 -> insertFromKeyboard();
                case 3 -> generateRandomTree();
                case 4 -> buildSortedTree();
                case 5 -> tree.inOrder();
                case 6 -> tree.printTree();
                case 7 -> {
                    int value = readInt("Введіть число для видалення: ");
                    System.out.println(tree.delete(value)
                            ? "Число видалено." : "Число не знайдено.");
                }
                case 8 -> System.out.println(tree.isValid()
                        ? "Усі властивості червоно-чорного дерева дотримано."
                        : "Помилка: властивості дерева порушено!");
                case 9 -> {
                    tree = new RedBlackTree();
                    System.out.println("Дерево очищено.");
                }
                case 0 -> {
                    System.out.println("Завершення програми.");
                    return;
                }
                default -> System.out.println("Невідомий пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== ЧЕРВОНО-ЧОРНЕ ДЕРЕВО ===");
        System.out.println("1. Додати число");
        System.out.println("2. Додати декілька чисел із клавіатури");
        System.out.println("3. Згенерувати випадкові числа та побудувати дерево");
        System.out.println("4. Побудувати дерево з тих самих чисел за зростанням");
        System.out.println("5. Обхід in-order");
        System.out.println("6. Показати дерево");
        System.out.println("7. Видалити число");
        System.out.println("8. Перевірити властивості дерева");
        System.out.println("9. Очистити дерево");
        System.out.println("0. Вихід");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть ціле число.");
            }
        }
    }

    private static void insertFromKeyboard() {
        System.out.print("Введіть числа через пробіл: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Ви не ввели жодного числа.");
            return;
        }
        String[] parts = input.split("\\s+");
        int[] numbers = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: усі елементи мають бути цілими числами.");
            return;
        }
        int added = 0;
        for (int number : numbers) {
            if (tree.insert(number)) added++;
        }
        System.out.println("Додано: " + added + ", пропущено повторів: " + (numbers.length - added));
    }

    private static void generateRandomTree() {
        int count;
        do {
            count = readInt("Кількість чисел (від 1 до 1000): ");
        } while (count < 1 || count > 1000);

        lastGenerated = new int[count];
        Set<Integer> used = new HashSet<>();
        int i = 0;
        while (i < count) {
            int number = random.nextInt(10_000);
            if (used.add(number)) {
                lastGenerated[i++] = number;
            }
        }

        System.out.println("Порядок вставлення: " + Arrays.toString(lastGenerated));
        tree = new RedBlackTree();
        for (int number : lastGenerated) tree.insert(number);
        tree.printTree();
    }

    private static void buildSortedTree() {
        if (lastGenerated == null) {
            System.out.println("Спочатку виконайте пункт 3 (генерація чисел).");
            return;
        }
        int[] sorted = Arrays.copyOf(lastGenerated, lastGenerated.length);
        Arrays.sort(sorted);
        System.out.println("Порядок вставлення: " + Arrays.toString(sorted));
        tree = new RedBlackTree();
        for (int number : sorted) tree.insert(number);
        tree.printTree();
    }
}

