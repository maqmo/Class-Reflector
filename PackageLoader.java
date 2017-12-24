
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

public class PackageLoader
{

	private PackagePreparer pkp;
	String dir;
	public PackageLoader(String dirName) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		dir = dirName;
		pkp = new PackagePreparer(dirName);
	}

	public void showMainMenu()
	{
		String[] choices = {"List all classes", "View a class", "Save all classes", "Load Class info from XML"};
		TerminalMenu.showMenu(choices,"Welcome to PackageExplorer -- Main Menu");
		Scanner scan = new Scanner(System.in);
		String input = TerminalMenu.promptForInput(scan, "Enter your choice (1-"+choices.length+") or press q to quit");
		handleInput(input);
		if(input.equalsIgnoreCase("q"))
			handleInput("q");
		else if (input.equals("2"))
			return;
		else
			showMainMenu();
	}

	public void showAllClasses() throws ClassNotFoundException
	{
		String[] classes = pkp.getClassNames();
		TerminalMenu.showMenu(classes, " List of Classes:" );
		Scanner scan = new Scanner(System.in);
		String choice = TerminalMenu.promptForInput(scan, "Enter (1-"+classes.length+") to view details or m for main menu");
		Integer num = null;
		try{
			num = Integer.parseInt(choice);

		}catch(Exception e)
		{
			handleInput(choice);
		}
		if (num != null)
			showSingleClass(classes[num - 1]);
		else
			throw new ClassNotFoundException();
	}
	public void showSelectedClass() throws ClassNotFoundException
	{
		Scanner scan = new Scanner(System.in);
		String input = TerminalMenu.promptForInput(scan, "Enter the name of the class you want to view: ");
		showSingleClass(input);
		String nuinp = TerminalMenu.promptForInput(scan, "Enter s to save or m for Main Menu");
		if (nuinp.equalsIgnoreCase("s"))
			pkp.save(input);
		else
			handleInput("m");
		scan.close();
	}
	public void showSingleClass(String cn) throws ClassNotFoundException
	{
		int index = pkp.findClass(cn);
		if (index > -1)
		{
			MetaData mo = new MetaData(pkp.getPM().getHashMap().get(cn).getClassType());
			Class<?>[] inters = mo.getInterfaces();
			sop ("", true);
			System.out.println("Class Details:\nName: " + cn );
			System.out.print("Superclass: ");
			if (mo.getSupers().length > 0)
			{
				for (Class<?> elt : mo.getSupers())
					sop(elt + " ", false);
				sop("", true);
			}
			else
				sop("None", true);
			if (inters.length != 0)
				System.out.println("Interfaces: " + inters);
			else
				System.out.println("Interfaces: None");
			System.out.println("Methods:");
			if (mo.getMethods().length == 0)
				sop("None" , true);
			else
				for (Method elt: mo.getMethods())
					System.out.println("\t" + elt.getName());
			System.out.println("Fields:");
			if (mo.getFields().length == 0)
				sop("None", true);
			else
				for (Field elt: mo.getFields())
					sop("\t" + elt.getType(), true);
			sop("Providers: ", false);
			if (mo.getProviders().length == 0)
				sop("None", true);
			else
				for (Class elt: mo.getProviders())
					sop("\t" + elt.getName(), true);
			sop("Clients: ", false);
			if (mo.getClients().length == 0)
				sop("None", true);
			else
				for (Class elt: mo.getProviders())
					sop("\t" + elt.getName(), true);
			sop ("", true);
		}

	}

	public void sop(Object x, boolean skipLine)
	{
		TerminalMenu.sop(x, skipLine);
	}

	public void saveAllToXML() throws IOException
	{
		Scanner scan = new Scanner(System.in);
		String prompt = "Enter the name of file: ";
		String fileName = TerminalMenu.promptForInput(scan, prompt);
		File file = new File(getDir(), fileName);
		pkp.saveAllClasses(file);
	}

	public void readInFromXML()
	{
		TerminalMenu.sop("This is in progress", true);
	}

	public void handleInput(String input)
	{
		switch(input)
		{
		case "1": try {
			showAllClasses();
		} catch (ClassNotFoundException e1) {
			sop("Problem when entering 1, showAllClasses()", true);
		}
		break;
		case "2":
			try {
				showSelectedClass();
			} catch (ClassNotFoundException e) {
				System.out.println("option 2 error, cnfe");
			}
			break;
		case "3": try {
			saveAllToXML();
		} catch (IOException e) {
			TerminalMenu.sop("option 3 error IOEx", true);
		}
		break;
		case "4": readInFromXML();
		break;
		case "q": TerminalMenu.sop("Thanks, bye now!", true);
			return;
		default:
			sop("", true);
			showMainMenu();
		}

	}

	public String getDir()
	{
		return dir;
	}

	public static void main(String[] args)
	{
		String dir = "";
		if (args.length == 0)
			dir = System.getProperty("user.dir");
		else if (args.length == 1)
			dir = args[0];
		else
		{
			Scanner scan = new Scanner(System.in);
			dir = TerminalMenu.promptForInput(scan, "Please enter a valid directory name!");
			scan.close();
		}
		PackageLoader pl = null;
		try {
			pl = new PackageLoader(dir);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		pl.showMainMenu();
		TerminalMenu.sop("Fin", true);

	}
}
