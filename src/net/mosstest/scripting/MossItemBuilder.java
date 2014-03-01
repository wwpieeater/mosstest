package net.mosstest.scripting;

public class MossItemBuilder {
    private String invTex;
    private String wieldTex;
    private double invWeight;
    private boolean mayStack = true;
    private String displayName;
    private String technicalName;

    public MossItemBuilder setInvTex(String invTex) {
        this.invTex = invTex;
        return this;
    }

    public MossItemBuilder setWieldTex(String wieldTex) {
        this.wieldTex = wieldTex;
        return this;
    }

    public MossItemBuilder setInvWeight(double invWeight) {
        this.invWeight = invWeight;
        return this;
    }

    public MossItemBuilder setMayStack(boolean mayStack) {
        this.mayStack = mayStack;
        return this;
    }

    public MossItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public MossItemBuilder setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
        return this;
    }

    public MossItem createMossItem() {
        return new MossItem(invTex, wieldTex, invWeight, mayStack, displayName, technicalName);
    }
}