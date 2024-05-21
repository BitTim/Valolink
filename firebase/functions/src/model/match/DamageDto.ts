/**
 * Class that holds information about damage events
 */
class DamageDto {
  receiver: string;
  damage: number;
  legshots: number;
  bodyshots: number;
  headshots: number;

  /**
     * Constructs a new DamageDto objects
     * @param {string} receiver PUUID of receiver of damage event
     * @param {number} damage Amount of damage dealt
     * @param {number} legshots Amount of legshots
     * @param {number} bodyshots Amount of bodyshots
     * @param {number} headshots Amount of headshots
     */
  constructor(
    receiver: string,
    damage: number,
    legshots: number,
    bodyshots: number,
    headshots: number
  ) {
    this.receiver = receiver;
    this.damage = damage;
    this.legshots = legshots;
    this.bodyshots = bodyshots;
    this.headshots = headshots;
  }
}
