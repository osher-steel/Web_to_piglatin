import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WebToPigLatin {

	public static boolean isContent(char c) {
		if (c == '<' || c == '&')
			return false;
		else
			return true;
	}

	public static boolean isAlphaNum(char c) {
		if ((Character.toLowerCase(c) >= 97 && Character.toLowerCase(c) <= 122) || c == 39)
			return true;
		else
			return false;
	}

	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);

		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
			return true;
		else
			return false;

	}

	public static boolean isVowelWithoutY(char c) {
		c = Character.toLowerCase(c);

		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
			return true;
		else
			return false;
	}

	public static int writeWord(BufferedReader br, BufferedWriter bw, int intRead, char firstLetter, boolean upperCase)
			throws IOException {
		BufferedReader tempBr = new BufferedReader(new FileReader("temp2"));
		int first = 0;
		int tempRead;
		while (isAlphaNum((char) intRead)) {
			if (first != 0)
				intRead = br.read();

			if (isAlphaNum((char) intRead)) {
				if (first == 0 && upperCase == true) // Sends first letter as an uppercase
					bw.write(Character.toUpperCase((char) intRead));
				else
					bw.write((char) intRead);
			} else // If end of word
			{
				if (isVowelWithoutY(firstLetter)) // If word started with a vowel (not y) then add a 'w'
					bw.write('w');
				else {
					while ((tempRead = tempBr.read()) != ' ')// If word started with a consonant
					{
						bw.write(Character.toLowerCase((char) tempRead)); // print all the consonants from the temp file
					}

				}

				bw.write("ay"); // add "ay" at end of word
				tempBr.close();

				return intRead; // returns intRead to make sure that the last letter is analyzed
			}

			first++; // To make sure that only the first letter can be capitalized

		}

		tempBr.close();
		return 0;
	}

	public static void fileRead(String in, String out) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));

		int intRead; // To hold each character from the file
		char firstLetter; // To hold the first letter of every word
		boolean upperCase;

		intRead = br.read();

		while (intRead != -1) // Loops until br gets to the end of the file
		{
			int tempRead = 0;

			if (isContent((char) intRead)) // If it represents the content of an html tag
			{
				if (isAlphaNum((char) intRead)) // Checks if first letter is a letter
				{
					BufferedWriter tempBw = new BufferedWriter(new FileWriter("temp2")); // Use to store consonants

					firstLetter = (char) intRead; // Holds first letter

					if (Character.isUpperCase(firstLetter)) // To check if first letter is uppercase
						upperCase = true;
					else
						upperCase = false;

					if ((isVowelWithoutY((char) intRead)) == false) // If first letter is not a vowel
					{
						if (Character.toUpperCase(firstLetter) =='Y') {
							tempBw.write(intRead);
							intRead = br.read();
						}
						while ((isVowel((char) intRead) == false)) // Loop that gets all the successive consonants at										// the
						{ 											//the beginning of the word
							tempBw.write(intRead); // Prints them in a temporary file
							intRead = br.read();
						}
						tempBw.write(' ');
						tempBw.close();
					}
					tempRead = writeWord(br, bw, intRead, firstLetter, upperCase);// Function that prints the word to
																					// the output file

				} else if (intRead != -1) // As long as it it is not the end of the file, print char
				{
					bw.write((char) intRead);
				}
			} else // If the file is inside html code (not content)
			{
				if ((char) intRead == '<') // If the character is '<' then loop and print the character until reaches
											// meets '>'
				{
					bw.write((char) intRead);
					while ((intRead = br.read()) != 62)
						bw.write((char) intRead);
					bw.write((char) intRead);
				} else if ((char) intRead == '&') // If the character is & loop and print until it reaches a non letter
													// character
					{
						bw.write((char) intRead);
						intRead = br.read();
						while (isAlphaNum((char) intRead)) {
							bw.write((char) intRead);
							intRead = br.read();
						}
						bw.write((char) intRead);
					}
			}
			if (tempRead == 0) // If tempRead is unchanged, read the next character in the file
				intRead = br.read();
			else // Else use the tempRead value as the next character that is to be analyzed
				intRead = tempRead;
		}
		br.close();
		bw.close();

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 0) {
			System.out.println("Usage: java WebToPigLatin inputFile outputFile");
			System.out.println("Must have two command-line parameters");
		} else
			fileRead("new", args[1]);

	}

}