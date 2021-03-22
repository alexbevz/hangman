package ru.bevz;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
	private final String[] words = "молоко лось кит крот".split(" ");
	private final String[] hangmanPictures = {
					"game-1.txt",
					"game-2.txt",
					"game-3.txt",
					"game-4.txt",
					"game-5.txt",
					"game-6.txt",
					"game-7.txt",
	};
	private final Scanner sc = new Scanner(System.in);
	private List<String> hangmanPicture;
	private String secretWord;
	private String alreadyGuessed;
	private String missedLetters;
	private String correctLetters;

	public Game() {
		preparingData();
	}

	public void start() {
		System.out.println("В И С Е Л И Ц А");
		boolean gameIsDone = false;
		while (true) {
			displayBoard();
			alreadyGuessed = correctLetters + missedLetters;

			String guess = getGuess();
			if (secretWord.contains(guess)) {
				correctLetters += guess;

				if (isWin()) {
					System.out.println("Да! Секретное слово: " + secretWord + ". Вы угадали!");
					gameIsDone = true;
				}
			} else {
				missedLetters += guess;
				hangmanPicture = readFileData(hangmanPictures[missedLetters.length()]);

				if (missedLetters.length() == (hangmanPictures.length - 1)) {
					displayBoard();
					System.out.println("Вы исчерпали все попытки!");
					System.out.println("Не угадали " + missedLetters.length() + " букв");
					System.out.println("Угадано букв: " + correctLetters.length());
					System.out.println("Загаданное слово: " + secretWord);
					gameIsDone = true;
				}
			}

			if (gameIsDone) {
				if (playAgain()) {
					gameIsDone = false;
					preparingData();
				} else {
					break;
				}
			}
		}
	}

	private void preparingData() {
		hangmanPicture = readFileData(hangmanPictures[0]);
		secretWord = getRandomWord();
		alreadyGuessed = "";
		missedLetters = "";
		correctLetters = "";
	}

	private List<String> readFileData(String name) {
		String path = "./src/main/resources/GameData/";
		List<String> data = new ArrayList<>();
		try {
			data = Files.readAllLines(Paths.get(path + name));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
							null,
							"Файл не найден!",
							"Ошибка",
							JOptionPane.ERROR_MESSAGE,
							null
			);
		}
		return data;
	}

	private String getRandomWord() {
		return words[getRandomValueBetweenRange(0, words.length - 1)];
	}

	public int getRandomValueBetweenRange(int min, int max) {
		return (int) (Math.random() * ((max - min) + 1) + min);
	}

	private boolean playAgain() {
		System.out.println("Хотите играть еще раз?(да или нет)");
		return sc.nextLine().toLowerCase().startsWith("д");
	}

	private String getGuess() {
		while (true) {
			System.out.print("Введите букву: ");
			String guess = sc.nextLine().toLowerCase();
			if (guess.length() != 1 || !"абвгдеёжзийклмнопрстуфхцчшщьыъэюя".contains(guess)) {
				System.out.println("Пожалуйста, введите одну букву ");
			} else if (alreadyGuessed.contains(guess)) {
				System.out.println("Вы уже указывали эту букву, введите другую:");
			} else {
				return guess;
			}
		}
	}

	private void printWrongLetters() {
		System.out.println("Ошибочные буквы: ");
		Arrays.stream(missedLetters.split("")).forEach(x -> System.out.print(x + " "));
		System.out.println();
	}

	private void printTrueLetters() {
		for (int i = 0; i < secretWord.length(); i++)
			System.out.print((correctLetters.contains(String.valueOf(secretWord.charAt(i))) ? secretWord.charAt(i) : "_") + " ");
		System.out.println();
	}

	private void displayBoard() {
		hangmanPicture.forEach(System.out::println);
		printWrongLetters();
		printTrueLetters();
	}

	private boolean isWin() {
		return secretWord.chars()
						.mapToObj(x -> (char) x)
						.collect(Collectors.toSet())
						.size() == correctLetters.length();
	}
}
