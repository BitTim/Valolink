/**
 * Class holding information about a match
 */
class MatchDto {
  matchInfo: MatchInfoDto;
  players: Array<PlayerDto>;
  coaches: Array<CoachDto>;
  teams: Array<TeamDto>;
  roundResults: Array<RoundResultDto>;

  /**
     * Constructs a new MatchDto object
     * @param {MatchInfoDto} matchInfo MatchInfo object
     * @param {Array<PlayerDto>} players Array of PlayerDto objects
     * @param {Array<CoachDto>} coaches Array of CoachDto objects
     * @param {Array<TeamDto>} teams Array of TeamDto objects
     * @param {Array<RoundResultDto>} roundResults Array of RoundResultDto
     *                                             objects
     */
  constructor(
    matchInfo: MatchInfoDto,
    players: Array<PlayerDto>,
    coaches: Array<CoachDto>,
    teams: Array<TeamDto>,
    roundResults: Array<RoundResultDto>
  ) {
    this.matchInfo = matchInfo;
    this.players = players;
    this.coaches = coaches;
    this.teams = teams;
    this.roundResults = roundResults;
  }
}
