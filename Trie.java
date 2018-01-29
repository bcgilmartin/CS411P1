import java.io.*;
import java.util.Scanner;

public class Trie {

	private int[] switchArray;
	private char[] symbolArray;
	private int[] nextArray;
	private int counter;

	public Trie() {
		// initialize switch array
		switchArray = new int[52];
		for (int i = 0; i < switchArray.length; i++) {
			switchArray[i] = -1;
		}

		// initialize symbol array
		symbolArray = new char[500];

		// initialize next array
		nextArray = new int[500];
		for (int j = 0; j < nextArray.length; j++) {
			nextArray[j] = -1;
		}

		// pointer for next available position in symbol array
		counter = 0;
	}

	public void addKeywords() throws FileNotFoundException {
		File keywordsFile = new File("keywords.txt");
		Scanner readKeywords = new Scanner(keywordsFile);
		String temp;
		char[] word;
		int switchValue, pointer, next;

		while (readKeywords.hasNext()) {
			temp = readKeywords.next();
			word = new char[temp.length() + 1];
			for (int k = 0; k < temp.length(); k++) {
				word[k] = temp.charAt(k);
			}
			word[temp.length()] = '@';

			next = 0;
			switchValue = checkSwitchValue(word[next++]);
			pointer = switchArray[switchValue];

			if (pointer < 0) {
				addWord(word, switchValue, pointer, next);
			}
			else {
				boolean exit = false;
				while (exit == false) {
					if (symbolArray[pointer] == word[next]) {
						if (word[next] != '@') {
							pointer++;
							next++;
						}
						else {
							exit = true;
						}
					}
					else if (nextArray[pointer] >= 0) {
						pointer = nextArray[pointer];
					}
					else {
						addWord(word, switchValue, pointer, next);
						exit = true;
					}
				}
			}
		}
		readKeywords.close();
	}

	public void addWord(char[] word, int switchNumber, int pointer, int next) {
		// If the first symbol / letter does not exist yet
		if (switchArray[switchNumber] < 0) {
			switchArray[switchNumber] = counter;
			for (int i = 1; i < word.length; i++) {
				symbolArray[counter++] = word[i];
			}
		}
		// If the beginning symbol / letter has been used
		else {
			nextArray[pointer] = counter;
			for (int j = next; j < word.length; j++) {
				symbolArray[counter++] = word[j];
			}
		}
	}

	public void checkWord(String input) {
		char[] word = new char[input.length() + 1];
		// copy word
		for (int i = 0; i < input.length(); i++) {
			word[i] = input.charAt(i);
		}
		word[input.length()] = '@';

		int next = 0;
		int switchValue = checkSwitchValue(word[next++]);
		int pointer = switchArray[switchValue];

		// first switch value doesn't exist yet
		if (pointer < 0) {
			// is id
			addWord(word, switchValue, pointer, next);
		}
		else {
			boolean exit = false;
			while (exit == false) {
				if (symbolArray[pointer] == word[next]) {
					if (word[next] != '@') {
						pointer++;
						next++;
					}
					else {
						exit = true;
					}
				}
				else if (nextArray[pointer] >= 0) {
					pointer = nextArray[pointer];
				}
				else {
					addWord(word, switchValue, pointer, next);
					exit = true;
				}
			}
		}
	}

	public int checkSwitchValue(char switchValue) {
		// if a - z [0-25]
		if (switchValue >= 97 && switchValue <= 122) {
			return switchValue - 97;
		}
		// if A - Z [26-51]
		else {
			return switchValue - 39;
		}
	}

	public void print() {
		String switchPrint = "", symbolPrint = "", nextPrint = "";
		for (int i = 0; i < switchArray.length; i++) {
			switchPrint = switchPrint + " " + switchArray[i];
		}
		System.out.println(switchPrint);

		for (int j = 0; j < symbolArray.length; j++) {
			symbolPrint = symbolPrint + " " + symbolArray[j];
		}
		System.out.println(symbolPrint);

		for (int k = 0; k < nextArray.length; k++) {
				nextPrint = nextPrint + " " + nextArray[k];
		}
		System.out.println(nextPrint);
	}

}
