package net.mosstest.scripting;


public interface LiquidNodeParams extends NodeParams {

	/**
	 * Calculates the liquid output from this liquid
	 * 
	 * @param pos
	 *            The NodePosition of the liquid's location.
	 * @return A double representing the flow rate, in a fully filled node in 1
	 *         second for 1.0
	 */
	public double calcFlowRate(NodePosition pos);

	/**
	 * Calculate how quickly this liquid should disappear. For
	 * flowRate=drainRate the liquid will be effectively finite.
	 * 
	 * @param pos
	 * @return
	 */
	public double calcDrainRate(NodePosition pos);

	/**
	 * Callback for liquid flow.
	 * 
	 * @param pos
	 *            Position.
	 */
	public void onLiquidFlow(NodePosition pos);

	/**
	 * Tell if a liquid area should fill with source(level 0)
	 * @param contacting Number of sources contacting.
	 * @return Boolean, true if it should.
	 */
	public boolean shouldFill(int contactingSources, int contactingFlowings);

}
