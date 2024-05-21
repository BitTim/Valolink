/**
 * Class that holds information about the finishing damage dealt
 */
class FinishingDamageDto {
  damageType: string;
  damageItem: string;
  isSecondaryFireMode: boolean;

  /**
     * Constructs a new FinishingDamageDto object
     * @param {string} damageType Type of damage
     * @param {string} damageItem UUID of item that was used
     * @param {boolean} isSecondaryFireMode Set if was secondary fire mode
     */
  constructor(
    damageType: string,
    damageItem: string,
    isSecondaryFireMode: boolean
  ) {
    this.damageType = damageType;
    this.damageItem = damageItem;
    this.isSecondaryFireMode = isSecondaryFireMode;
  }
}
