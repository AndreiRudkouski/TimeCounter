package timeCounter.init.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import timeCounter.init.IInitializer;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.init.annotation.Run;
import timeCounter.init.annotation.Setter;
import timeCounter.logger.MainLogger;

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
	 * Creates instances of classes which have appropriate method with {@link Instance} annotation and the method is
	 * situated in class with {@link Config} annotation.
	 *
	 * @param configs names of config files with {@link Config} annotation (Files should be located in the same package
	 * as the initializer)
	 */
	private void createInstances(String... configs)
	{
		try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			for (String config : configs)
			{
				Class cl = classLoader.loadClass(
						Initializer.class.getName().split(Initializer.class.getSimpleName())[0] + config);
				classInstances.put(cl.getSimpleName(), cl.newInstance());
			}
			for (Map.Entry<String, Object> classInstance : classInstances.entrySet())
			{
				Class clazz = classInstance.getValue().getClass();
				if (clazz.isAnnotationPresent(Config.class))
				{
					for (Method method : clazz.getDeclaredMethods())
					{
						if (method.isAnnotationPresent(Instance.class))
						{
							Instance instance = method.getAnnotation(Instance.class);
							if (instance.name().equals(""))
							{
								classInstances.put(method.getReturnType().getSimpleName(),
										method.invoke(classInstance.getValue()));
							}
							else
							{
								classInstances.put(instance.name(), method.invoke(classInstance.getValue()));
							}
						}
					}
				}
			}
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e)
		{
			MainLogger.getLogger().severe(e.toString());
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
				List<Method> setterMethods = Arrays.stream(obj.getClass().getDeclaredMethods()).filter(
						m -> m.isAnnotationPresent(Setter.class)).collect(Collectors.toList());
				List<Field> setterFields = Arrays.stream(obj.getClass().getDeclaredFields()).filter(
						f -> f.isAnnotationPresent(Setter.class)).collect(Collectors.toList());
				// If class has superclass and the superclass is abstract and not Object, add superclass methods
				Class clazz = obj.getClass();
				while (!clazz.getSuperclass().getSimpleName().equals("Object") && Modifier.isAbstract(
						clazz.getSuperclass().getModifiers()))
				{
					setterMethods.addAll(Arrays.stream(obj.getClass().getSuperclass().getDeclaredMethods())
							.filter(m -> m.isAnnotationPresent(Setter.class)).collect(Collectors.toList()));
					setterFields.addAll(Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())
							.filter(f -> f.isAnnotationPresent(Setter.class)).collect(Collectors.toList()));
					clazz = clazz.getSuperclass();
				}

				for (Method method : setterMethods)
				{
					Setter setter = method.getAnnotation(Setter.class);
					if (setter.name().equals(""))
					{
						method.invoke(obj, classInstances.get(method.getParameterTypes()[0].getSimpleName()));
					}
					else
					{
						method.invoke(obj, classInstances.get(setter.name()));
					}
				}
				for (Field field : setterFields)
				{
					Setter setter = field.getAnnotation(Setter.class);
					field.setAccessible(true);
					if (setter.name().equals(""))
					{
						field.set(obj, classInstances.get(field.getType().getSimpleName()));
					}
					else
					{
						field.set(obj, classInstances.get(setter.name()));
					}
				}
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(e.toString());
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
				Method method = Arrays.stream(classInstance.getValue().getClass().getDeclaredMethods())
						.filter(m -> m.isAnnotationPresent(Run.class)).findFirst().orElse(null);
				if (method != null)
				{
					method.setAccessible(true);
					method.invoke(classInstance.getValue());
					return;
				}
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(e.toString());
		}
	}
}
