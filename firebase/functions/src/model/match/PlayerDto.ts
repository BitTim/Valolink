/**
 * Class to hold information about players in match
 */
class PlayerDto {
  puuid: string;
  gameName: string;
  tagLine: string;
  teamId: string;
  partyId: string;
  characterId: string;
  stats: PlayerStatsDto;
  competitiveTier: number;
  playerCard: string;
  playerTitle: string;

  /**
     * Constructs a new PlayerDto object
     * @param {string} puuid Player UUID
     * @param {string} gameName Player Name
     * @param {string} tagLine Player Tagline
     * @param {string} teamId Id of team
     * @param {string} partyId Id of party
     * @param {string} characterId Id of played agent
     * @param {PlayerStatsDto} stats Performance stats
     * @param {number} competitiveTier Competitive Tier ID
     * @param {string} playerCard Used player card
     * @param {string} playerTitle Used Player Title
     */
  constructor(
    puuid: string,
    gameName: string,
    tagLine: string,
    teamId: string,
    partyId: string,
    characterId: string,
    stats: PlayerStatsDto,
    competitiveTier: number,
    playerCard: string,
    playerTitle: string
  ) {
    this.puuid = puuid;
    this.gameName = gameName;
    this.tagLine = tagLine;
    this.teamId = teamId;
    this.partyId = partyId;
    this.characterId = characterId;
    this.stats = stats;
    this.competitiveTier = competitiveTier;
    this.playerCard = playerCard;
    this.playerTitle = playerTitle;
  }
}
