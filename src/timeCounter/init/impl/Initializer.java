package timeCounter.init.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timeCounter.init.IInitializer;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.init.annotation.Run;
import timeCounter.init.annotation.Setter;

public class Initializer implements IInitializer
{
	private final Map<String, Object> classInstances = new HashMap<>();

	public Initializer(String... configs)
	{
		createInstances(configs);
		initFields();
		runMethod();
	}

	/**
	 * Creates instances of all classes which are marked by {@link Config} annotation.
	 *
	 * @param configs names of config files (Files should be located in the same package as the initializer)
	 */
	private void createInstances(String... configs)
	{
		try
		{
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			for (String config : configs)
			{
				classLoader.loadClass(
						Initializer.class.getName().split(Initializer.class.getSimpleName())[0] + config);
			}
			Set<Class> loadedClasses = new HashSet<>((List<Class>) f.get(classLoader));
			for (Class clazz : loadedClasses)
			{
				if (clazz.isAnnotationPresent(Config.class))
				{
					for (Method method : clazz.getDeclaredMethods())
					{
						if (method.isAnnotationPresent(Instance.class))
						{
							Annotation annotation = method.getAnnotation(Instance.class);
							Instance instance = (Instance) annotation;
							if (instance.name().equals(""))
							{
								classInstances.put(method.getName(), method.invoke(clazz.newInstance()));
							}
							else
							{
								classInstances.put(instance.name(), method.invoke(clazz.newInstance()));
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Initializes all fields which are marked by {@link Setter} annotation.
	 */
	private void initFields()
	{
		try
		{
			for (Map.Entry<String, Object> classInstance : classInstances.entrySet())
			{
				Object obj = classInstance.getValue();
				List<Method> methods = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredMethods()));

				// If class has superclass and the superclass is abstract and not Object, add superclass methods
				Class clazz = obj.getClass();
				while (!clazz.getSuperclass().getSimpleName().equals("Object") && Modifier.isAbstract(
						clazz.getSuperclass().getModifiers()))
				{
					methods.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredMethods()));
					clazz = clazz.getSuperclass();
				}

				for (Method method : methods)
				{
					if (method.isAnnotationPresent(Setter.class))
					{
						Annotation annotation = method.getAnnotation(Setter.class);
						Setter setter = (Setter) annotation;
						if (setter.name().equals(""))
						{
							method.invoke(obj, classInstances.get(method.getParameterTypes()[0].getSimpleName()));
						}
						else
						{
							method.invoke(obj, classInstances.get(setter.name()));
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Runs the first found method which is marked by {@link Run} annotation.
	 */
	private void runMethod()
	{
		try
		{
			for (Map.Entry<String, Object> classInstance : classInstances.entrySet())
			{
				for (Method method : classInstance.getValue().getClass().getDeclaredMethods())
				{
					if (method.isAnnotationPresent(Run.class))
					{
						method.invoke(classInstance.getValue());
						return;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
