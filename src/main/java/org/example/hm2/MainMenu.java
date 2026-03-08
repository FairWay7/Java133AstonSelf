package org.example.hm2;

import org.example.hm2.dto.UserRequestDTO;
import org.example.hm2.dto.UserResponseDTO;
import org.example.hm2.dto.UserUpdateRequestDTO;
import org.example.hm2.service.UserService;
import org.example.hm2.service.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static Scanner in = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private static String[] commands = new String[] {
        "Полный список", "Создать", "Прочитать", "Обновить", "Удалить"
    };

    public static void start() {
        while (true) {
            System.out.println("Выберите действие:");
            for (int i = 0; i < commands.length; i++) {
                System.out.printf("%d) %s\n", i + 1, commands[i]);
            }
            int action = getAction();

            switch (action) {
                case 1:
                    getAllUsers();
                    break;
                case 2:
                    createUser();
                    break;
                case 3:
                    getUser();
                    break;
                case 4:
                    update();
                    break;
                case 5:
                    delete();
                    break;
            }
        }
    }

    private static int getAction() {
        while (true) {
            try {
                System.out.printf("Введите число от 1 до %d: ", commands.length);
                int action = Integer.parseInt(in.nextLine());
                if (action >= 0 && action <= commands.length) {
                    return action;
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода. Введите число");
            }
        }
    }

    private static void getAllUsers() {
        List<UserResponseDTO> allUsers = userService.getAllUsers();
        for(var user : allUsers) {
            System.out.println(user.toString());
        }
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String username = in.nextLine();

        System.out.print("Введите email: ");
        String email = in.nextLine();

        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(in.nextLine());

        UserRequestDTO user = new UserRequestDTO(username, email, age);
        try {
            userService.createUser(user);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Пользователь успешно создан!");
    }

    private static void getUser() {
        try {
            System.out.println("Поиск по: \n1) id (по умолчанию)\n2) username");
            int action = Integer.parseInt(in.nextLine());
            switch (action) {
                case 2:
                    getUserByUsername();
                default:
                    getUserById();
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода. Введите число");
        }
    }

    private static void getUserByUsername() {
        System.out.print("Введите имя: ");
        String username = in.nextLine();
        try {
            UserResponseDTO user = userService.getUsersByUsername(username);
            System.out.println(user.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getUserById() {
        System.out.print("Введите id: ");
        try {
            Long id = Long.parseLong(in.nextLine());
            UserResponseDTO user = userService.getUserById(id);
            System.out.println(user.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void update() {
        System.out.print("Введите id: ");
        try {
            Long id = Long.parseLong(in.nextLine());

            System.out.print("Введите имя: ");
            String username = in.nextLine();

            System.out.print("Введите email: ");
            String email = in.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(in.nextLine());

            UserUpdateRequestDTO user = new UserUpdateRequestDTO(id, username, email, age);

            try {
                userService.updateUser(user);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e) {
            System.out.println("Неверный формат ввода");
        }
    }

    private static void delete() {
        System.out.print("Введите id: ");
        try {
            Long id = Long.parseLong(in.nextLine());
            UserResponseDTO user = userService.deleteUser(id);
            System.out.println(user.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
