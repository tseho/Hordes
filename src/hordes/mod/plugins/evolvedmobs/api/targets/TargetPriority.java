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
}
