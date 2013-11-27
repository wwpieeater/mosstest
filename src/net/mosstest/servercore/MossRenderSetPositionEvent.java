package net.mosstest.servercore;

public class MossRenderSetPositionEvent extends MossRenderEvent {
	private final double offsetX, offsetY, offsetZ;
	private final int chkX, chkY, chkZ;

	public MossRenderSetPositionEvent(double offsetX, double offsetY,
			double offsetZ, int chkX, int chkY, int chkZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.chkX = chkX;
		this.chkY = chkY;
		this.chkZ = chkZ;
	}

	public double getOffsetX() {
		return this.offsetX;
	}

	public double getOffsetY() {
		return this.offsetY;
	}

	public double getOffsetZ() {
		return this.offsetZ;
	}

	public int getChkX() {
		return this.chkX;
	}

	public int getChkY() {
		return this.chkY;
	}

	public int getChkZ() {
		return this.chkZ;
	}
}
