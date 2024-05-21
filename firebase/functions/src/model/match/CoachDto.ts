/**
 * Class that holds information about coaches
 */
class CoachDto {
  puuid: string;
  teamId: string;

  /**
     * Constructs a new CoachDto object
     * @param {string} puuid PUUID of coach
     * @param {string} teamId Team ID of coach
     */
  constructor(
    puuid: string,
    teamId: string
  ) {
    this.puuid = puuid;
    this.teamId = teamId;
  }
}
