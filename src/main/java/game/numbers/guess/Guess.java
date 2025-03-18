package game.numbers.guess;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static game.numbers.Main.WelcomeMessage;

public class Guess {
    public Scanner sc = new Scanner(System.in);

    private static final Random random = new Random();
    private int target;

    private boolean changeDifficulty = true;

    ArrayList<String> dependentHintsList;
    ArrayList<String> generalHintList;

    int tries;
    static int getHints;
    static int hintsGiven;
    static int[] bounds = new int[] {1, 100, 2};
    static int lastInput;

    public void game() throws InterruptedException {
        System.out.println("""
                
                _______________________________________________________
                Welcome to the Number Guessing Game!
                I will think of a number between 1 and 100 or between a 100 and 1000.
                Try and guess it :D
                """);
        while (true) {
            hintsGiven = 0;
            boolean win = false;
            if (changeDifficulty) {
                System.out.print("""
                        Please select the Difficulty level:
                        1. Very Easy
                        (1-100)  (10 chances) (0 hints)
                        
                        2. Easy
                        (1-100)  (5 chances)  (2 hint)
                        
                        3. Medium
                        (100-999)  (5 chances)  (5 hints)
                        
                        4. hard
                        (100-999) (3 chances) (4 hints)
                        
                        5. brutal (Good luck, you will need it)
                        (1000-9999) (5 chances)  (5 hints)
                        
                         >\s""");
                bounds = chooseDifficulty();
                changeDifficulty = false;
            }
            target = random.nextInt(bounds[1]) + bounds[0];
            generalHintList = generalHint(target);

            System.out.print("""
                    
                    And OK, Let's begin...
                    Your first guess
                     >\s""");

            for (int i = 0; i < tries; i++) {
                try {
                    int input = sc.nextInt();
                    dependentHintsList = dependantHints(target, input);
                    if (input > bounds[1] + bounds[0] || input <= bounds[0]) {
                        System.out.print("Please input a number between " + bounds[0] + " and " + (bounds[1] + bounds[0] - 1) + ".\n > ");
                        i--;
                    } else {
                        lastInput = input;
                        int sl = mechanic(input);
                        if (sl == 1) {
                            if (i == tries - 2) System.out.print("Too high! Last Chance!!!\n > ");
                            else if (i == tries - 1) { System.out.println("Welp, hmm..."); Thread.sleep(1000); }
                            else System.out.print("Too High! try again\n > ");
                        }
                            else if (sl == 2) {
                            if (i == tries - 2) System.out.print("Too low! Last Chance!!!\n > ");
                            else if (i == tries - 1) { System.out.println("Welp, hmm..."); Thread.sleep(1000); }
                            else System.out.print("Too low! try again\n > ");
                        }
                        else if (sl == 0) {
                            System.out.println("You got it! congrats 8I...");
                            win = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    String error = sc.nextLine();
                    if (error.equals("hint") && hintsGiven < getHints) {
                        System.out.print(getHint() + "\n > ");
                        hintsGiven++;
                        i--;
                    } else if (error.equals("hint")) {
                        System.out.print("Sorry no more hints for you B)...\n > ");
                        i--;
                    }else {
                        System.out.print("Please input a number!! (integer)\n > ");
                        i--;
                    }
                }
            }

            if (win) System.out.println("Seems like the game was too easy for you.");
            else System.out.println("You ran out of tries. The number was "+ target +". Better luck next time.\n" +
                    "I'll tell you something, I will still say \"Too high!\" even if you are one number lower ;]");
            Thread.sleep(1000);
            System.out.print("You want to play again? (y/n)\n > ");
            isNewGame();
        }
    }

    private void isNewGame(){
        try {
            switch (sc.next()) {
                case "n" -> WelcomeMessage();
                case "y" -> {
                    System.out.print("""
                            Glad you're playing more with me!
                            Ok, want to change the difficulty?(y/n)
                             >\s""");
                    switch (sc.next()) {
                        case "y" -> changeDifficulty = true;
                        case "n" -> changeDifficulty = false;
                        default -> {
                            System.out.println("Please enter y or n for yes or no! ... I will take that as a NO");
                            Thread.sleep(500);
                        }
                    }
                }
                default -> {
                    System.out.print("Please enter y or n for yes or no!\n > ");
                    isNewGame();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    int[] chooseDifficulty() {
        while (true) {
            try {
                switch (sc.nextInt()) {
                    case 1 -> {
                        tries = 10;
                        getHints = 0;
                        return new int[]{1, 100, 1};
                    }
                    case 2 -> {
                        tries = 5;
                        getHints = 2;
                        return new int[]{1, 100, 1};
                    }
                    case 3 -> {
                        tries = 5;
                        getHints = 3;
                        return new int[]{100, 900, 2};
                    }
                    case 4 -> {
                        tries = 3;
                        getHints = 4;
                        return new int[]{100, 900, 2};
                    }
                    case 5 -> {
                        tries = 5;
                        getHints = 5;
                        return new int[]{1000, 9000, 3};
                    }
                }
            } catch (Exception e) {
                if (Objects.equals(sc.nextLine(), "exit")) { WelcomeMessage(); }
                System.out.print("Please enter a valid choice.\n > ");
                sc.nextLine();
            }
        }
    }

    public int mechanic(int i) {
        if (i > target)
            return 1;
        else if (i < target)
            return 2;
        else
            return 0;
    }

    public static ArrayList<String> generalHint(int target) {
        ArrayList<String> hints = new ArrayList<>();
        ArrayList<Integer> digits = new ArrayList<>();
        ArrayList<Integer> notDigits = new ArrayList<>();
        int temp = target;
        boolean isSquare = false;
        boolean prime = true;

        if (target % 2 == 0) hints.add("The number is even!");
        else hints.add("The number is odd!");

        if (bounds[2] != 1) {
            while (temp % 10 >= 1) {
                digits.add(temp % 10);
                temp = temp / 10;
            }
            int sum = 0;
            for (Integer digit : digits) {
                sum += digit;
            }
            for (int i = 0; i < 10; i++) {
                for (Integer digit : digits) {
                    if (i != digit && !notDigits.contains(i)) notDigits.add(i);
                }
            }
            hints.add("The sum of the digits of the number is " + sum);
            hints.add("The number contains the digit " + digits.get(random.nextInt(digits.size())));
            hints.add("The number does not contain the digit " + notDigits.get(random.nextInt(notDigits.size())));
            hints.add("The number does not contain the digit " + notDigits.get(random.nextInt(notDigits.size())));
        }

        for (int i = 0; i < 32; i++) {
            if (i * i == target) { isSquare = true; break;}
        } if (isSquare) hints.add("The number is a perfect square.");
        else hints.add("The number is not a perfect square.");

        for (int i = 2; i < target; i++)
            if (target % i == 0) { prime = false; break; }
        if (prime) hints.add("The number is a prime number");
        else hints.add("The number is not a prime number");

        return hints;
    }

    public static ArrayList<String> dependantHints (int target, int lastInput){
        ArrayList<String> hints = new ArrayList<>();
        ArrayList<Integer> targetDigits = new ArrayList<>();
        ArrayList<Integer> inputDigits = new ArrayList<>();
        int targetTemp = target;
        int inputTemp = lastInput;
        int correctPlace = 0;
        int wrongPlace = 0;

        if (bounds[2] != 1) {
        while (targetTemp % 10 >= 1) {
            targetDigits.add(targetTemp % 10);
            targetTemp =  targetTemp / 10;
        }while (inputTemp % 10 >= 1) {
            inputDigits.add(inputTemp % 10);
            inputTemp =  inputTemp / 10;
        }
        for (int i = 0; i < inputDigits.size(); i++) {
            if (Objects.equals(inputDigits.get(i), targetDigits.get(i))) correctPlace++;
            else {
                boolean taken = false;
                for (int j = i; j < targetDigits.size(); j++) {
                    if (Objects.equals(inputDigits.get(i), targetDigits.get(j)) && !taken) {
                        wrongPlace++; taken = true;
                    }
                }
            }
        }
            hints.add("Your last input contained " + correctPlace + " digits in the correct place.");
            hints.add("Your last input contained " + wrongPlace + " correct digits, but in the wrong place.");
        }

        int isClose = 5;
        if (bounds[2] == 2) {isClose = 50;}
        if (bounds[2] == 3) {isClose = 500;}

        if ((target - lastInput) < 0 && (target - lastInput) < -isClose)
            hints.add("Well... I will tell you this, it's warmer than you think! in a negative way");
        else if ((target - lastInput) > 0 && (target - lastInput) < isClose)
            hints.add("Well... I will tell you this, it's warmer than you think! in a positive way");
        else hints.add("pssst, hey listen it's still far away");

        return hints;
    }

    public String getHint() {
        while (true) {
            int rand = random.nextInt(2);
            if (rand == 0 && !dependentHintsList.isEmpty())
                return dependentHintsList.get(random.nextInt(dependentHintsList.size()));
            else if (rand == 1 && !generalHintList.isEmpty()) {
                int num = random.nextInt(generalHintList.size());
                return generalHintList.remove(num);
            }
        }
    }


}
