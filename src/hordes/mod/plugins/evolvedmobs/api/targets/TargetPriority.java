package hordes.mod.plugins.evolvedmobs.api.targets;

/**
 *
 * @author Tseho
 */
public enum TargetPriority {
    LOWEST, LOW, MEDIUM, HIGH, HIGHEST;
    
    public boolean isLowerThan(TargetPriority otherPriority){
        return this.ordinal() < otherPriority.ordinal();
    }
    
    public boolean isHighterThan(TargetPriority otherPriority){
        return this.ordinal() > otherPriority.ordinal();
    }
    
    public TargetPriority getHighterPriority(){
        for (int i = 0; i < TargetPriority.values().length; i++) {
            if(this.isHighterThan(TargetPriority.values()[i])){
                return TargetPriority.values()[i];
            }
        }
        return this;
    }
    
    public TargetPriority getLowerPriority(){
        for (int i = TargetPriority.values().length - 1; i >= 0; i--) {
            if(this.isHighterThan(TargetPriority.values()[i])){
                return TargetPriority.values()[i];
            }
        }
        return this;
    }
}
