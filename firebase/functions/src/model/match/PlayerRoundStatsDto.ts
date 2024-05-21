/**
 * Class that hold player stats per round
 */
class PlayerRoundStatsDto {
  puuid: string;
  kills: Array<KillDto>;
  damage: Array<DamageDto>;
  score: number;
  economy: EconomyDto;
  ability: AbilityDto;

  /**
     * Constructs a new PlayerRoundStatsDto object
     * @param {string} puuid PUUID of player
     * @param {Array<KillDto>} kills List of kills
     * @param {Array<DamageDto>} damage List of damage events
     * @param {number} score Round score
     * @param {EconomyDto} economy Round economy
     * @param {AbilityDto} ability Abilities used
     */
  constructor(
    puuid: string,
    kills: Array<KillDto>,
    damage: Array<DamageDto>,
    score: number,
    economy: EconomyDto,
    ability: AbilityDto
  ) {
    this.puuid = puuid;
    this.kills = kills;
    this.damage = damage;
    this.score = score;
    this.economy = economy;
    this.ability = ability;
  }
}
