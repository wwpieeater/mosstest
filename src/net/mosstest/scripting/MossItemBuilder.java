package net.mosstest.scripting;

import org.jetbrains.annotations.NonNls;

public class MossItemBuilder {
    private String invTex;
    private String wieldTex;
    private double invWeight;
    private MossItem.StackMode mayStack = MossItem.StackMode.STACK_UNIT;
    private String displayName;
    private String internalName;

    public MossItemBuilder setInvTex(String invTex) {
        this.invTex = invTex;
        return this;
    }

    public MossItemBuilder setWieldTex(@NonNls String wieldTex) {
        this.wieldTex = wieldTex;
        return this;
    }

    public MossItemBuilder setInvWeight(double invWeight) {
        this.invWeight = invWeight;
        return this;
    }

    public String getInvTex() {
        return invTex;
    }

    public String getWieldTex() {
        return wieldTex;
    }

    public double getInvWeight() {
        return invWeight;
    }

    public MossItem.StackMode getStackMode() {
        return mayStack;
    }

    public MossItemBuilder setStackMode(MossItem.StackMode mayStack) {
        this.mayStack = mayStack;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public MossItemBuilder setDisplayName(@NonNls String displayName) {
        this.displayName = displayName;
        return this;
    }

    public MossItemBuilder setInternalName(@NonNls String internalName) {
        this.internalName = internalName;
        return this;
    }

    public MossItem createMossItem() {
        return new MossItem(invTex, wieldTex, invWeight, mayStack, displayName, internalName);
    }
}