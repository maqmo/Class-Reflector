
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

public class PackageMapper{

	/**
	 *
	 */
	HashMap<String,MetaData> hashMap;
	String[] classes;

	public PackageMapper(String[] classes) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.classes = classes;
		hashMap = new HashMap<String , MetaData>();
		for (String elt: classes)
		{
			//skip anonymous classes
			if ( elt.charAt(elt.length() - 2) != '$')
			{
				Class c = Class.forName(elt);
				hashMap.put(elt, new MetaData(c));
			}
		}
		for (MetaData elt: hashMap.values())
		{
			findProvidersAndClients(elt);
		}
	}

	private void findProvidersAndClients(MetaData subject)
	{
		if (hashMap != null)
		{
			Collection<MetaData> metas = hashMap.values();
			for (MetaData elt: metas)
			{
				Field[] fa = elt.getFields();
				int i = 0;
				if (!(elt.getClassType().equals(subject)))
				{
					while(i < fa.length)
					{
						if (fa[i++].getDeclaringClass().getName().contains(subject.getClassType().getName()))
						{
							elt.addProvider(subject.getClassType());
							subject.addClient(elt.getClassType());
						}
					}
				}

			}
		}
	}

	public HashMap<String, MetaData> getHashMap()
	{
		return hashMap;
	}

}
