/**
 * Class that holds information about Abilities cast
 */
class AbilityCastsDto {
  grenadeCasts: number;
  ability1Casts: number;
  ability2Casts: number;
  ultimateCasts: number;

  /**
     * Constructs a new AbilityCastsDto
     * @param {number} grenadeCasts Number of Grenades casts
     * @param {number} ability1Casts Number of Ability 1 casts
     * @param {number} ability2Casts Number of Ability 2 casts
     * @param {number} ultimateCasts Number of Ultimate casts
     */
  constructor(
    grenadeCasts: number,
    ability1Casts: number,
    ability2Casts: number,
    ultimateCasts: number,
  ) {
    this.grenadeCasts = grenadeCasts;
    this.ability1Casts = ability1Casts;
    this.ability2Casts = ability2Casts;
    this.ultimateCasts = ultimateCasts;
  }
}
