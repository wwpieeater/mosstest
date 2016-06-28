package net.mosstest.servercore;

// TODO: Auto-generated Javadoc
/**
 * The Class MossRenderSetPositionEvent.
 */
public class MossRenderSetPositionEvent extends MossRenderEvent {
	
	/** The offset z. */
	private final double offsetX, offsetY, offsetZ;
	
	/** The chk z. */
	private final int chkX, chkY, chkZ;

	/**
	 * Instantiates a new moss render set position event.
	 *
	 * @param offsetX the offset x
	 * @param offsetY the offset y
	 * @param offsetZ the offset z
	 * @param chkX the chk x
	 * @param chkY the chk y
	 * @param chkZ the chk z
	 */
	public MossRenderSetPositionEvent(double offsetX, double offsetY,
			double offsetZ, int chkX, int chkY, int chkZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.chkX = chkX;
		this.chkY = chkY;
		this.chkZ = chkZ;
	}

	/**
	 * Gets the offset x.
	 *
	 * @return the offset x
	 */
	public double getOffsetX() {
		return this.offsetX;
	}

	/**
	 * Gets the offset y.
	 *
	 * @return the offset y
	 */
	public double getOffsetY() {
		return this.offsetY;
	}

	/**
	 * Gets the offset z.
	 *
	 * @return the offset z
	 */
	public double getOffsetZ() {
		return this.offsetZ;
	}

	/**
	 * Gets the chk x.
	 *
	 * @return the chk x
	 */
	public int getChkX() {
		return this.chkX;
	}

	/**
	 * Gets the chk y.
	 *
	 * @return the chk y
	 */
	public int getChkY() {
		return this.chkY;
	}

	/**
	 * Gets the chk z.
	 *
	 * @return the chk z
	 */
	public int getChkZ() {
		return this.chkZ;
	}
}
