package net.mosstest.scripting;

public class MossTool extends MossItem {

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
		 * Interact with a soft object, i.e. a hand.
		 */
		INTERACT_FLESHY,

		/**
		 * Interact as a hard block
		 */
		INTERACT_BLOCKY,
	}


	
	public interface IToolParams {
		public double wearToolDig(MapNode dug);
	}
}
