/**
 * A class holding general information about a match
 */
class MatchInfoDto {
  matchId: string;
  mapId: string;
  gameLengthMillis: number;
  gameStartMillis: number;
  provisioningFlowId: string;
  isCompleted: boolean;
  customGameName: string;
  queueId: string;
  gameMode: string;
  isRanked: boolean;
  seasonId: string;

  /**
     * Constructs a new MatchDto object
     * @param {string} matchId Id of the match
     * @param {string} mapId Id of the map
     * @param {number} gameLengthMillis Length of match in ms
     * @param {number} gameStartMillis Start time of the match in ms
     * @param {string} provisioningFlowId Id of the type of matchmaking
     * @param {boolean} isCompleted Set if the match has been completed
     * @param {string} customGameName Name of custom game
     * @param {string} queueId Id of the matchmaking queue
     * @param {string} gameMode Game mode that has been played
     * @param {boolean} isRanked Set if match was ranked
     * @param {string} seasonId Id of the corresponding season
     */
  constructor(
    matchId: string,
    mapId: string,
    gameLengthMillis: number,
    gameStartMillis: number,
    provisioningFlowId: string,
    isCompleted: boolean,
    customGameName: string,
    queueId: string,
    gameMode: string,
    isRanked: boolean,
    seasonId: string
  ) {
    this.matchId = matchId;
    this.mapId = mapId;
    this.gameLengthMillis = gameLengthMillis;
    this.gameStartMillis = gameStartMillis;
    this.provisioningFlowId = provisioningFlowId;
    this.isCompleted = isCompleted;
    this.customGameName = customGameName;
    this.queueId = queueId;
    this.gameMode = gameMode;
    this.isRanked = isRanked;
    this.seasonId = seasonId;
  }
}
