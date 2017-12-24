
package reflection;
//import ;
import java.util.Scanner;
/*

 */

public class TerminalMenu
{

	public static void showMenu(String[] choices, String welcomeMsg)
	{
		sop(welcomeMsg, true);
		showLine(welcomeMsg.length() + 2, "_");
		for (int i = 0; i < choices.length; i++)
		{
			sop((i + 1) + ". " + choices[i], true);
		}
	}

	public static void sop(Object x, boolean doSkip)
	{
		String ln = "";
		if (doSkip)
			ln = "\n";
		System.out.print(x + ln);
	}

	public static void showLine(int c, String lineElt)
	{
		while(c-- > 0)
			sop(lineElt, false);
		sop("",true);
	}
	public static String promptForInput(Scanner scan, String prompt)
	{
		sop(prompt + " : ", false);
		String val = scan.next();
		if (val == null)
			return null;
		return val;
	}

}