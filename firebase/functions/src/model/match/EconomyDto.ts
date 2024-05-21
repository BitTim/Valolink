/**
 * Class that holds information about player economy
 */
class EconomyDto {
  loadoutValue: number;
  weapon: string;
  armor: string;
  remaining: number;
  spent: number;

  /**
     * Constructs a new EconomyDto object
     * @param {number} loadoutValue Value of the current loadout
     * @param {string} weapon Purchased weapon
     * @param {string} armor Purchased Armor
     * @param {number} remaining Remaining Credits
     * @param {number} spent Spent Credits
     */
  constructor(
    loadoutValue: number,
    weapon: string,
    armor: string,
    remaining: number,
    spent: number
  ) {
    this.loadoutValue = loadoutValue;
    this.weapon = weapon;
    this.armor = armor;
    this.remaining = remaining;
    this.spent = spent;
  }
}
