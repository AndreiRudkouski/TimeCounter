package timeCounter.init;

/**
 * This interface identified methods for initialization the application.
 */
public interface IInitializer
{
	/**
	 * Returns the instance of class by its name.
	 *
	 * @param name class name
	 */
	Object getClassInstanceByName(String name);
}
