/**
 * Class that holds information about each round result in a match
 */
class RoundResultDto {
  roundNum: number;
  roundResult: string;
  roundCeremony: string;
  winningTeam: string;
  bombPlanter: string;
  bombDefuser: string;
  plantRoundTime: number;
  plantPlayerLocations: Array<PlayerLocationsDto>;
  plantLocation: LocationDto;
  plantSite: string;
  defuseRoundTime: number;
  defusePlayerLocations: Array<PlayerLocationsDto>;
  defuseLocation: LocationDto;
  playerStats: Array<PlayerRoundStatsDto>;
  roundResultCode: string;

  /**
     * Constructs a new RoundResultDto object
     * @param {number} roundNum Current round number
     * @param {string} roundResult Result of round
     * @param {string} roundCeremony Ceremony of round
     * @param {string} winningTeam Team that won this round
     * @param {string} bombPlanter PUUID of player
     * @param {string} bombDefuser PUUID of player
     * @param {number} plantRoundTime Time in round when planted
     * @param {Array<PlayerLocationsDto>} plantPlayerLocations
     * All player locations on plant
     * @param {LocationDto} plantLocation Location of planter whilst planting
     * @param {string} plantSite Site that has been planted on
     * @param {number} defuseRoundTime Time in round when defused
     * @param {Array<PlayerLocationsDto>} defusePlayerLocations
     * All player locations on defuse
     * @param {LocationDto} defuseLocation Location of defuser whilst defusing
     * @param {Array<PlayerRoundStatsDto>} playerStats
     * Stats of each player for this round
     * @param {string} roundResultCode Result of round (Code)
     */
  constructor(
    roundNum: number,
    roundResult: string,
    roundCeremony: string,
    winningTeam: string,
    bombPlanter: string,
    bombDefuser: string,
    plantRoundTime: number,
    plantPlayerLocations: Array<PlayerLocationsDto>,
    plantLocation: LocationDto,
    plantSite: string,
    defuseRoundTime: number,
    defusePlayerLocations: Array<PlayerLocationsDto>,
    defuseLocation: LocationDto,
    playerStats: Array<PlayerRoundStatsDto>,
    roundResultCode: string
  ) {
    this.roundNum = roundNum;
    this.roundResult = roundResult;
    this.roundCeremony = roundCeremony;
    this.winningTeam = winningTeam;
    this.bombPlanter = bombPlanter;
    this.bombDefuser = bombDefuser;
    this.plantRoundTime = plantRoundTime;
    this.plantPlayerLocations = plantPlayerLocations;
    this.plantLocation = plantLocation;
    this.plantSite = plantSite;
    this.defuseRoundTime = defuseRoundTime;
    this.defusePlayerLocations = defusePlayerLocations;
    this.defuseLocation = defuseLocation;
    this.playerStats = playerStats;
    this.roundResultCode = roundResultCode;
  }
}
