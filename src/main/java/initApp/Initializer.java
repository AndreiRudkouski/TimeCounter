package main.java.initApp;

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

import main.java.initApp.annotation.Config;
import main.java.initApp.annotation.Instance;
import main.java.initApp.annotation.Setter;
import main.java.initApp.config.AppConfig;
import main.java.logger.MainLogger;

public final class Initializer
{
	private static final String EMPTY_STRING = "";
	private static final String SET_VALUE = "set";

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
				createInstancesOfAnnotatedMethods(classInstance);
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
			if (isAnnotatedAsConfig(configClass))
			{
				CLASS_INSTANCES.put(configClass.getSimpleName(), configClass.newInstance());
			}
		}
	}

	private static boolean isAnnotatedAsConfig(Class clazz)
	{
		return clazz.isAnnotationPresent(Config.class);
	}

	private static void createInstancesOfAnnotatedMethods(Map.Entry<String, Object> classInstance)
			throws InvocationTargetException, IllegalAccessException
	{
		Class clazz = classInstance.getValue().getClass();
		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.isAnnotationPresent(Instance.class))
			{
				Instance instanceAnnotation = method.getAnnotation(Instance.class);
				if (instanceAnnotation.name().equals(EMPTY_STRING))
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
				.filter(m -> m.isAnnotationPresent(Setter.class) && m.getName().startsWith(SET_VALUE))
				.collect(Collectors.toList()));
	}

	private static void addSetterAnnotatedFieldsAndMethodsFromAbstractSuperClasses(List<Field> annotatedFields,
			List<Method> annotatedMethods, Class clazz)
	{
		while (!clazz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName()) && Modifier.isAbstract(
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
			Setter setterAnnotation = field.getAnnotation(Setter.class);
			field.setAccessible(true);
			if (setterAnnotation.name().equals(EMPTY_STRING))
			{
				field.set(instance, CLASS_INSTANCES.get(field.getType().getSimpleName()));
			}
			else
			{
				field.set(instance, CLASS_INSTANCES.get(setterAnnotation.name()));
			}
		}
	}

	private static void initFieldsByAnnotatedMethods(List<Method> annotatedMethods, Object instance)
			throws InvocationTargetException, IllegalAccessException
	{
		for (Method method : annotatedMethods)
		{
			Setter setterAnnotation = method.getAnnotation(Setter.class);
			if (setterAnnotation.name().equals(EMPTY_STRING))
			{
				method.invoke(instance, CLASS_INSTANCES.get(method.getParameterTypes()[0].getSimpleName()));
			}
			else
			{
				method.invoke(instance, CLASS_INSTANCES.get(setterAnnotation.name()));
			}
		}
	}

	public static Initializer instance()
	{
		return INSTANCE;
	}

	public Object getClassInstanceByName(String name)
	{
		return CLASS_INSTANCES.get(name);
	}
}