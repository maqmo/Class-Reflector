
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class PackagePreparer
{

	ArrayList<String> classNames;
	PackageMapper pm;
	public PackagePreparer(String path) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		classNames = new ArrayList<String>();
		System.out.println("Accepted package: " + path);
		FilenameFilter classFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".class");
			}
		};

		File f = new File(path); // the directory
		File[] fa = f.listFiles(classFilter);
		for (int i = 0; i < fa.length; i++)
		{
			if (!fa[i].getName().contains("$"))
				classNames.add(fa[i].getName().replace(".class", ""));
		}

		pm = new PackageMapper(classNames.toArray(new String[classNames.size()]));
	}

	public String[] getClassNames()	{
		return classNames.toArray(new String[classNames.size()]);
	}
	public int findClass(String target)
	{
		return classNames.indexOf(target);
	}

	public static String[] toStringArray(Object[] m)
	{
		if (!(m instanceof String[]))
		{
			String[] str = new String[m.length];
			int i = 0;
			for (Object elt: m)
				str[i++] = ((Class) elt).getName();
			return str;
		}
		return (String[]) m;
	}

	public PackageMapper getPM()
	{
		return pm;
	}

	public void displayClass(String cn)
	{
		int index = findClass(cn);
		if (index > -1)
		{
			MetaData mo = new MetaData(pm.getHashMap().get(cn).getClassType());
			Class[] inters = mo.getInterfaces();
			System.out.println("Class Details: \n Name: " + cn );
			System.out.println("Superclass: " + mo.getClassType());
			System.out.println("Interfaces: " + inters);
			System.out.println("Methods:");
			for (Method elt: mo.getMethods())
				System.out.println("\t" + elt.getName());
			System.out.println("Fields:");
			for (Field elt: mo.getFields())
				System.out.println("\t" + elt.getName());

		}
	}

	public void save(String classToSave)
	{
		try {
			XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(new File(classToSave+".xml"))));
			MetaData met = pm.hashMap.get(classToSave);
			xmlOut.writeObject(met.getClassType());
			xmlOut.writeObject(met.getInterfaces());
			xmlOut.writeObject(met.getSupers());
			xmlOut.writeObject(met.getFields());
			xmlOut.writeObject(met.getMethods());
			xmlOut.flush();
			xmlOut.close();
		}catch (Exception e) {
			System.out.println("Cant xml it out");
			e.printStackTrace();
		}
//
//		MetaData met = pm.hashMap.get(classToSave);
//		System.out.println(met.getClassType());
//		System.out.println(Arrays.toString(met.getFields()));
//		System.out.println(Arrays.toString(met.getMethods()));
//		System.out.println(Arrays.toString(met.getSupers()));


	}

	public void saveAllClasses(File file)
	{
		XMLEncoder xmlOut;
		try {
			xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));

		for (MetaData elt : pm.getHashMap().values())
		{
			xmlOut.writeObject(elt.getClassType());
			xmlOut.writeObject(elt.getInterfaces());
			xmlOut.writeObject(elt.getSupers());
			xmlOut.writeObject(elt.getFields());
			xmlOut.writeObject(elt.getMethods());

			xmlOut.flush();
		}

		xmlOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file at saveAllClasses is the problem");
			e.printStackTrace();
		}

	}

}