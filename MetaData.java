
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MetaData implements Serializable {

	/**
	 *
	 */

	Class classType;
	Field[] fields;
	Method[] methods;
	Class[] supers;
	Class[] interfaces;
	ArrayList<Class> providers;
	ArrayList<Class> clients;

	public MetaData(Class cl)
	{
		classType = cl;
		fields = cl.getDeclaredFields();
		methods = cl.getDeclaredMethods();
		supers = getAllSuperClasses();
		interfaces = cl.getInterfaces();
		clients = new ArrayList<Class>();
		providers = new ArrayList<Class>();
	}

	public void addClient(Class cl)
	{
		clients.add(cl);
	}

	public void addProvider(Class pr)
	{
		providers.add(pr);
	}

	private Class[] getAllSuperClasses()
	{
		ArrayList<Class> list = new ArrayList<>();
		while (!classType.getSuperclass().getName().contains("Object"))
			list.add(classType.getSuperclass());
		if (list.size() == 1)
			list.remove(0);
		return list.toArray(new Class[list.size()]);
	}


	public Class getClassType()
	{
		return classType;
	}

	public Field[] getFields() {
		return fields;
	}


	public Method[] getMethods() {
		return methods;
	}


	public Class[] getSupers() {
		return supers;
	}


	public Class[] getInterfaces() {
		return interfaces;
	}

	public Class[] getProviders() {
		return providers.toArray(new Class[providers.size()]);
	}

	public Class[] getClients()
	{
		return clients.toArray(new Class[clients.size()]);
	}














}
