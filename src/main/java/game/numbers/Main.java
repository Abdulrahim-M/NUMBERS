package game.numbers;

import game.numbers.guess.Guess;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Guess guess = new Guess();

    public static void main(String[] args) {
        WelcomeMessage();
    }

    public static void WelcomeMessage(){
        System.out.print("""
                
                _______________________________________________________
                 ** Welcome the the \u001B[1m\u001B[34mNUMBERS\u001B[0m
                    Please do choose your game:
                    1. Guess the number
                
                 >\s""");

        while (true) {
            try {
                int input = sc.nextInt();
                switch (input) {
                    case 1 -> guess.game();
                    default -> System.out.print("Please input the number of the game!\n > ");
                }
            } catch (Exception e) {
                System.out.print("Please input the number of the game!\n > ");
                sc.nextLine();
            }
        }
    }

}
