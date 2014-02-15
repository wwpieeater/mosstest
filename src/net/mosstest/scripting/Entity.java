package net.mosstest.scripting;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public class Entity {
	
	/** The id. */
	int id;
	
	/** The name. */
	public String name;
	
	/** The hp. */
	int hp;
	
	/** The max health. */
	int maxHealth;
	
	/**
	 * Destroy.
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Instantiates a new entity.
	 *
	 * @param name the name
	 * @param maxHealth the max health
	 */
	protected Entity(String name, int maxHealth) {
		//TODO DB lookup for entities
	}
}
