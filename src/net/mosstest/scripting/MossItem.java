package net.mosstest.scripting;


// TODO: Auto-generated Javadoc


/**
 * The Class MossItem.
 */
public class MossItem {

    public enum StackMode {
        STACK_NET_AMOUNT,
        STACK_UNIT,
        STACK_TOOL,
        STACK_SINGLE
    }

    public MossItem(String invTex, String wieldTex, double invWeight,
                    StackMode stackMode, String displayName, String internalName) {
        this.invTex = invTex;
        this.wieldTex = wieldTex;
        this.invWeight = invWeight;
        this.stackMode = stackMode;
        this.displayName = displayName;
        this.internalName = internalName;
    }

    final String invTex;

    final String wieldTex;

    final double invWeight;

    StackMode stackMode;

    final String displayName;

    final String internalName;

    /**
     * The Class Stack.
     */
    public static class Stack {

        /**
         * Instantiates a new stack.
         *
         * @param item   the item
         * @param amount the amount
         */
        public Stack(MossItem item, double amount) {
            this.item = item;
            this.amount = amount;
        }

        public double getRemovable(StackMode stackMode, double maxAmount) {
            switch (stackMode) {
                case STACK_NET_AMOUNT:
                    return Math.min(this.amount, maxAmount);
                case STACK_SINGLE:
                    return (maxAmount >= 1) ? 1 : 0;
                case STACK_TOOL:
                    double frac = (this.amount - Math.floor(amount));
                    return frac + (Math.floor(maxAmount - frac));
                case STACK_UNIT:
                    return Math.floor(Math.max(this.amount, maxAmount));
                default:
                    return 0;
            }
        }

        public double getAddable(StackMode stackMode, double maxAmount, int stackSize) {
            switch (stackMode) {
                case STACK_NET_AMOUNT:
                    return Math.min(maxAmount, stackSize - this.amount);
                case STACK_SINGLE:
                    return (this.amount == 0) ? 1 : 0;
                case STACK_TOOL:
                    // this stack is integer value
                    if (this.amount == Math.floor(this.amount)) return Math.min((stackSize - this.amount), maxAmount);

                        // other stack is integer value
                    else if (maxAmount == Math.floor(maxAmount))
                        return Math.floor(Math.min(stackSize - this.amount, maxAmount));

                        // neither are, but we have more than one in other stack
                    else if (maxAmount >= 1) {
                        return Math.floor(Math.min(maxAmount, stackSize - this.amount));
                    } else return 0;
                case STACK_UNIT:
                    return Math.floor(Math.min(maxAmount, stackSize - this.amount));
                default:
                    return 0;
            }
        }

        public static double getMaxSize(StackMode stackMode, double maxAmount, int stackSize) {
            switch (stackMode) {
                case STACK_NET_AMOUNT:
                    return Math.min(maxAmount, stackSize);
                case STACK_SINGLE:
                    return 1;
                case STACK_TOOL:
                    // this stack is integer value
                    return Math.min((stackSize), maxAmount);

                case STACK_UNIT:
                    return Math.floor(Math.min(maxAmount, stackSize));
                default:
                    return 0;
            }
        }

        /**
         * The item.
         */
        final MossItem item;
        /**
         * If amount is not an integer then it is a stack with wear. Once zero
         * it is no longer a stack.
         */
        double amount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.internalName == null) ? 0 : this.internalName
                .hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MossItem other = (MossItem) obj;
        if (this.internalName == null) {
            if (other.internalName != null)
                return false;
        } else if (!this.internalName.equals(other.internalName))
            return false;
        return true;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the technical name.
     *
     * @return the technical name
     */
    public String getInternalName() {
        return internalName;
    }

}
