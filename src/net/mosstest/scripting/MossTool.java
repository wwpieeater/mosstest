package net.mosstest.scripting;


// TODO: Auto-generated Javadoc
/**
 * The Class MossTool.
 */
public class MossTool extends MossItem {


	public MossTool(String invTex, String wieldTex, double invWeight,
			StackMode stackMode, String displayName, String technicalName) {
		super(invTex, wieldTex, invWeight, stackMode, displayName, technicalName);
	}



	/**
	 * The Enum InteractType.
	 */
	public enum InteractType {
		/**
		 * Interact by cracking, i.e. by a pickaxe.
		 */
		INTERACT_CRACKING,

		/**
		 * Interact by splitting, i.e. an axe
		 */
		INTERACT_SPLITTING,

		/**
		 * Interact by slashing, i.e. with a sword.
		 */
		INTERACT_SLASHING,

		/**
		 * Interact by moving a mass, i.e. a shovel.
		 */
		INTERACT_MASS,

		/**
		 * Interact with a soft object, i.e. a hand or a pile of leaves.
		 */
		INTERACT_FLESHY,

		/** Interact as a hard block. */
		INTERACT_BLOCKY,
	}


	
	/**
	 * The Interface IToolParams.
	 */
	public interface IToolParams {
		
		/**
		 * Wear tool dig.
		 *
		 * @param dug the dug
		 * @return the double
		 */
		public double wearToolDig(MapNode dug);
	}
}
