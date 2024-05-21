/**
 * Class that holds information about abilities used
 */
class AbilityDto {
  grenadeEffects: string;
  ability1Effects: string;
  ability2Effects: string;
  ultimateEffects: string;

  /**
     * Constructs a new AbilityDto object
     * @param {string} grenadeEffects Effects of grenade ability
     * @param {string} ability1Effects Effects of ability 1
     * @param {string} ability2Effects Effects of ability 2
     * @param {string} ultimateEffects Effects of ultimate ability
     */
  constructor(
    grenadeEffects: string,
    ability1Effects: string,
    ability2Effects: string,
    ultimateEffects: string
  ) {
    this.grenadeEffects = grenadeEffects;
    this.ability1Effects = ability1Effects;
    this.ability2Effects = ability2Effects;
    this.ultimateEffects = ultimateEffects;
  }
}
