/**
 * Class to hold information about player performance stats
 */
class PlayerStatsDto {
  score: number;
  roundsPlayed: number;
  kills: number;
  deaths: number;
  assists: number;
  playtimeMillis: number;
  abilityCasts: AbilityCastsDto;

  /**
     * Constructs a new PlayerStatsDto object
     * @param {number} score Average combat score
     * @param {number} roundsPlayed Rounds Played
     * @param {number} kills Amount of kills
     * @param {number} deaths Amount of deaths
     * @param {number} assists Amount of assist
     * @param {number} playtimeMillis Playtime in ms
     * @param {AbilityCastsDto} abilityCasts Information about ability casts
     */
  constructor(
    score: number,
    roundsPlayed: number,
    kills: number,
    deaths: number,
    assists: number,
    playtimeMillis: number,
    abilityCasts: AbilityCastsDto,
  ) {
    this.score = score;
    this.roundsPlayed = roundsPlayed;
    this.kills = kills;
    this.deaths = deaths;
    this.assists = assists;
    this.playtimeMillis = playtimeMillis;
    this.abilityCasts = abilityCasts;
  }
}
