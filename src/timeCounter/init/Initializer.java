package timeCounter.init;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.init.annotation.Setter;
import timeCounter.init.config.AppConfig;
import timeCounter.logger.MainLogger;

public class Initializer
{
	private static final Map<String, Object> CLASS_INSTANCES = new HashMap<>();
	private static final Initializer INSTANCE = new Initializer(AppConfig.class);

	private Initializer(Class... configClasses)
	{
		createInstances(configClasses);
		initFields();
	}

	/**
	 * Creates instances of classes which have appropriate method with {@link Instance} annotation and the method is
	 * situated in class with {@link Config} annotation.
	 *
	 * @param configClasses config files with {@link Config} annotation
	 */
	private static void createInstances(Class... configClasses)
	{
		try
		{
			for (Class configClass : configClasses)
			{
				CLASS_INSTANCES.put(configClass.getSimpleName(), configClass.newInstance());
			}
			for (Map.Entry<String, Object> classInstance : CLASS_INSTANCES.entrySet())
			{
				Class clazz = classInstance.getValue().getClass();
				if (clazz.isAnnotationPresent(Config.class))
				{
					for (Method method : clazz.getDeclaredMethods())
					{
						if (method.isAnnotationPresent(Instance.class))
						{
							Instance instanceAnnotation = method.getAnnotation(Instance.class);
							if (instanceAnnotation.name().equals(""))
							{
								CLASS_INSTANCES.put(method.getReturnType().getSimpleName(),
										method.invoke(classInstance.getValue()));
							}
							else
							{
								CLASS_INSTANCES.put(instanceAnnotation.name(), method.invoke(classInstance.getValue()));
							}
						}
					}
				}
			}
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	/**
	 * Initializes all fields which are marked by {@link Setter} annotation or have setter methods with the annotation.
	 */
	private static void initFields()
	{
		try
		{
			for (Map.Entry<String, Object> classInstance : CLASS_INSTANCES.entrySet())
			{
				Object classObject = classInstance.getValue();
				// Collect all setter methods and fields of class which are marked by Setter annotation
				List<Method> setterAnnotatedMethods = Arrays.stream(classObject.getClass().getDeclaredMethods()).filter(
						m -> m.isAnnotationPresent(Setter.class) && m.getName().startsWith("set")).collect(
						Collectors.toList());
				List<Field> setterAnnotatedFields = Arrays.stream(classObject.getClass().getDeclaredFields()).filter(
						f -> f.isAnnotationPresent(Setter.class)).collect(Collectors.toList());
				// If class has superclass and the superclass is abstract and not Object, add superclass methods
				Class clazz = classObject.getClass();
				while (!clazz.getSuperclass().getSimpleName().equals("Object") && Modifier.isAbstract(
						clazz.getSuperclass().getModifiers()))
				{
					setterAnnotatedMethods.addAll(Arrays.stream(
							classObject.getClass().getSuperclass().getDeclaredMethods())
							.filter(m -> m.isAnnotationPresent(Setter.class) && m.getName().startsWith("set"))
							.collect(Collectors.toList()));
					setterAnnotatedFields.addAll(Arrays.stream(
							classObject.getClass().getSuperclass().getDeclaredFields())
							.filter(f -> f.isAnnotationPresent(Setter.class)).collect(Collectors.toList()));
					clazz = clazz.getSuperclass();
				}

				for (Method method : setterAnnotatedMethods)
				{
					Setter setterMethodAnnotation = method.getAnnotation(Setter.class);
					if (setterMethodAnnotation.name().equals(""))
					{
						method.invoke(classObject, CLASS_INSTANCES.get(method.getParameterTypes()[0].getSimpleName()));
					}
					else
					{
						method.invoke(classObject, CLASS_INSTANCES.get(setterMethodAnnotation.name()));
					}
				}
				for (Field field : setterAnnotatedFields)
				{
					Setter setterFieldAnnotation = field.getAnnotation(Setter.class);
					field.setAccessible(true);
					if (setterFieldAnnotation.name().equals(""))
					{
						field.set(classObject, CLASS_INSTANCES.get(field.getType().getSimpleName()));
					}
					else
					{
						field.set(classObject, CLASS_INSTANCES.get(setterFieldAnnotation.name()));
					}
				}
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	public static Object getClassInstanceByName(String name)
	{
		return CLASS_INSTANCES.get(name);
	}
}