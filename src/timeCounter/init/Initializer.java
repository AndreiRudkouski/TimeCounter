package timeCounter.init;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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

public final class Initializer
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
			createConfigClassInstances(configClasses);
			for (Map.Entry<String, Object> classInstance : CLASS_INSTANCES.entrySet())
			{
				if (isAnnotatedAsConfig(classInstance))
				{
					createInstancesOfAnnotadedMethods(classInstance);
				}
			}
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private static void createConfigClassInstances(Class... configClasses)
			throws IllegalAccessException, InstantiationException
	{
		for (Class configClass : configClasses)
		{
			CLASS_INSTANCES.put(configClass.getSimpleName(), configClass.newInstance());
		}
	}

	private static boolean isAnnotatedAsConfig(Map.Entry<String, Object> classInstance)
	{
		Class clazz = classInstance.getValue().getClass();
		return clazz.isAnnotationPresent(Config.class);
	}

	private static void createInstancesOfAnnotadedMethods(Map.Entry<String, Object> classInstance)
			throws InvocationTargetException, IllegalAccessException
	{
		Class clazz = classInstance.getValue().getClass();
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

	/**
	 * Initializes all fields which are marked by {@link Setter} annotation or have setter methods with the annotation.
	 */
	private static void initFields()
	{
		try
		{
			for (Map.Entry<String, Object> classInstance : CLASS_INSTANCES.entrySet())
			{
				List<Field> annotatedFields = new ArrayList<>();
				List<Method> annotatedMethods = new ArrayList<>();
				Object instance = classInstance.getValue();
				Class clazz = instance.getClass();

				addSetterAnnotatedFields(annotatedFields, clazz);
				addSetterAnnotatedMethods(annotatedMethods, clazz);
				addSetterAnnotatedFieldsAndMethodsFromAbstractSuperClasses(annotatedFields, annotatedMethods, clazz);

				initAnnotatedFields(annotatedFields, instance);
				initFieldsByAnnotatedMethods(annotatedMethods, instance);
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private static void addSetterAnnotatedFields(List<Field> annotatedFields, Class clazz)
	{
		annotatedFields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Setter.class))
				.collect(Collectors.toList()));
	}

	private static void addSetterAnnotatedMethods(List<Method> annotatedMethods, Class clazz)
	{
		annotatedMethods.addAll(Arrays.stream(clazz.getDeclaredMethods())
				.filter(m -> m.isAnnotationPresent(Setter.class) && m.getName().startsWith("set"))
				.collect(Collectors.toList()));
	}

	private static void addSetterAnnotatedFieldsAndMethodsFromAbstractSuperClasses(List<Field> annotatedFields,
			List<Method> annotatedMethods, Class clazz)
	{
		while (!clazz.getSuperclass().getSimpleName().equals("Object") && Modifier.isAbstract(
				clazz.getSuperclass().getModifiers()))
		{
			clazz = clazz.getSuperclass();
			addSetterAnnotatedFields(annotatedFields, clazz);
			addSetterAnnotatedMethods(annotatedMethods, clazz);
		}
	}

	private static void initAnnotatedFields(List<Field> annotatedFields, Object instance) throws IllegalAccessException
	{
		for (Field field : annotatedFields)
		{
			Setter setterFieldAnnotation = field.getAnnotation(Setter.class);
			field.setAccessible(true);
			if (setterFieldAnnotation.name().equals(""))
			{
				field.set(instance, CLASS_INSTANCES.get(field.getType().getSimpleName()));
			}
			else
			{
				field.set(instance, CLASS_INSTANCES.get(setterFieldAnnotation.name()));
			}
		}
	}

	private static void initFieldsByAnnotatedMethods(List<Method> annotatedMethods, Object instance)
			throws InvocationTargetException, IllegalAccessException
	{
		for (Method method : annotatedMethods)
		{
			Setter setterMethodAnnotation = method.getAnnotation(Setter.class);
			if (setterMethodAnnotation.name().equals(""))
			{
				method.invoke(instance, CLASS_INSTANCES.get(method.getParameterTypes()[0].getSimpleName()));
			}
			else
			{
				method.invoke(instance, CLASS_INSTANCES.get(setterMethodAnnotation.name()));
			}
		}
	}

	public static Object getClassInstanceByName(String name)
	{
		return CLASS_INSTANCES.get(name);
	}
}