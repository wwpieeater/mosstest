package net.mosstest.scripting;

import net.mosstest.servercore.MossFile;

public class MossTool extends MossItem {

	public MossTool(String invTex, String wieldTex, double invWeight,
			boolean mayStack, String displayName, String technicalName) {
		super(invTex, wieldTex, invWeight, mayStack, displayName, technicalName);
	}



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

		/**
		 * Interact as a hard block
		 */
		INTERACT_BLOCKY,
	}


	
	public interface IToolParams {
		public double wearToolDig(MapNode dug);
	}
}
