//Precondition: no spaces! Well, this program will remove all of the spaces, but then it may not be able to detect some mistakes 
//(for instance, if there are two proposition constants separated only by a space)
import java.util.ArrayList;
import java.util.Scanner;
public class WellFormedLogic {

	public static void main(String[] args) {
		System.out.println("This checks whether a propositional logic expression is correct.");
		System.out.println("Precondition: no spaces! Well, this program will remove all of the spaces, but then it may not be able to detect some mistakes");
		System.out.println("(for instance, if there are two proposition constants separated only by a space)");
		System.out.println("Also, all positions listed will start from 1 rather than from 0.");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String expression = new String(scan.nextLine().replaceAll("\\s",""));
		
		
		boolean incorrectChar = false;
		for (int i = 0; i < expression.length(); i++) {
			String s = expression.substring(i, i+1);
			char c = s.charAt(0);
			if (!(Character.isLetterOrDigit(c) || s.equals("|") || s.equals("&") || s.equals("~") || s.equals("<") ||
					s.equals("=") || s.equals(">") || s.equals("_") || s.equals("(") || s.equals(")"))) {
				System.out.println("This is incorrect: there is a character " + s + "that does not belong in this kind of logic.");
				incorrectChar = true;
				
			}
		}
		if (incorrectChar) {
			return;
		}
		
		boolean incorrectPStart = false;
		for (int i = 0; i < expression.length(); i++) {
			String s = expression.substring(i, i+1);
			char c = s.charAt(0);
			if ((i == 0 && Character.isLetterOrDigit(c) || s.equals("_"))
					|| (Character.isLetterOrDigit(c) || s.equals("_") && 
							!(Character.isLetterOrDigit(expression.substring(i-1, i).charAt(0)) || expression.substring(i-1, i).equals("_")))) { 
				if ((Character.isLetter(c) && Character.isUpperCase(c)) || Character.isDigit(c) || s.equals("_")) {
					incorrectPStart = true;
					System.out.print("This is incorrect: the proposition constant beginning at position ");
					System.out.println(i+1);
					System.out.print(" should not start with ");
					if (Character.isDigit(c)) {
						System.out.println("a digit.");
					} else if (s.equals("_")) {
						System.out.println("an underscore.");
					} else {
						System.out.println("an uppercase letter.");
					}
				}
				
			}
		}
		if(incorrectPStart) {
			return;
		}
		
		boolean incorrectArrow = false;
		ArrayList<Integer> locations = new ArrayList<Integer>();
		for (int i = 0; i < expression.length(); i++) {
			String s = expression.substring(i, i+1);
			if (i == 0 ) {
				if (s.equals("=") || s.equals(">") || s.equals("<")) {
				   incorrectArrow = true;
				   locations.add(1);
				}	
			} else if (i == expression.length()-1) {
				if (s.equals("=") || s.equals("<") || s.equals(">")) {
				   incorrectArrow = true;
				   locations.add(expression.length());
				}
			} else if (!(i == 0 || i == expression.length()-1)) {
				if (s.equals(">") && !expression.substring(i-1, i).equals("=")) {
				   incorrectArrow = true;
				   locations.add(i+1);
				}
				if (s.equals("=") && !expression.substring(i+1, i+2).equals(">")) {
				   incorrectArrow = true;
				   locations.add(i+1);
				}
			}		
		}
		if (incorrectArrow) {
			System.out.print("This expression is incorrect. Parts of arrows may be missing at position(s):");
			System.out.print(locations.get(0));
			if (locations.size() > 1) {
				for (int j = 1; j < locations.size(); j++) {
				    System.out.print(", ");
				    System.out.print(locations.get(j));
				}
			}
			System.out.println();
			System.out.println("Note: For errors at the ends of the string, the arrow may actually be complete.");
			System.out.println("Nevertheless, it still shouldn't be there, as arrows need to point to something.");
			return;
		}
		
		int p = 0;
		for (int i = 0; i < expression.length(); i++) {
			String s = expression.substring(i, i+1);
			if (s.equals("(")) {
				p--;
			}
			if (s.equals(")")) {
				p++;
			}
			if (p > 0) {
				System.out.print("Incorrect: no matching left parenthesis for the right parenthesis at position ");
				System.out.println(i+1);
			}
			
		}
		if (p != 0) {
		   if (p < 0) {
			   System.out.print("There is/are ");
			   System.out.print(Math.abs(p));
			   System.out.println(" unmatched left parenthesis/es. This expression is incorrect.");
		   }
		   return;
		}
		
		boolean wrongOperators = false;
		for (int i = 0; i < expression.length(); i++) {
			String s = expression.substring(i, i+1);
			if (i == 0 || i == expression.length()-1) {
			   if (s.equals("|") || s.equals("&") || s.equals("<") || s.equals("=") || s.equals(">")) {
				  wrongOperators = true;
				  System.out.println("Some operators cannot be at the ends of this expression. This is incorrect.");
			   }
			   if (i == expression.length()-1 && s.equals("~")) {
				  System.out.println("A ~ sign cannot be at the end of this expression. This is incorrect.");
			   }
			} else {
				String fs = expression.substring(i+1, i+2);
				char fc = fs.charAt(0);
				String hs = expression.substring(i-1, i);
				char hc = hs.charAt(0);
				if (s.equals("|") || s.equals("&") || s.equals("=") || s.equals(">")) {
					if (fs.equals("|") || fs.equals("&") || fs.equals("<") || fs.equals("=") || fs.equals(")")) {
						wrongOperators = true;
						System.out.println("Some operators can't be next to each other. This expression is incorrect.");   	
					}
					if (hs.equals("|") || hs.equals("&") || hs.equals("<") || hs.equals("(")) {
						wrongOperators = true;
						System.out.println("Some operators can't be next to each other. This expression is incorrect.");   	
					}
				}
				if (s.equals("<") && !fs.equals("=")) {
					wrongOperators = true;
					System.out.println("A < symbol needs a = symbol after it. This expression is incorrect.");
				}
				if (s.equals("~") && (hs.equals("_") || Character.isLetterOrDigit(hc) || hs.equals(")") 
						|| !(Character.isLowerCase(fc) || fs.equals("(") || fs.equals("~")))) {
					wrongOperators = true;
					System.out.print("This expression is incorrect. A ~ symbol in position ");
					System.out.print(i+1);
					System.out.println(" has an symbol near it which shouldn't be there.");
					
				}
			}
		}
		if(wrongOperators) {
			return;
		}
		
		System.out.println("This expression seems to have the correct Propositional Logic syntax.");
		
		
		
		
		
		

			
	}
		
		
		

}
