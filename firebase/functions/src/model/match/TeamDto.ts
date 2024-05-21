/**
 * Class that holds information about teams in match
 */
class TeamDto {
  teamId: string;
  won: boolean;
  roundsPlayed: number;
  roundsWon: number;
  numPoints: number;

  /**
     * Constructs a new TeamDto
     * @param {string} teamId This is an arbitrary string. Red and Blue in
     *                        bomb modes. The puuid of the player in deathmatch.
     * @param {boolean} won Set if team has won match
     * @param {number} roundsPlayed Number of rounds played
     * @param {number} roundsWon Number of rounds won
     * @param {number} numPoints Team points scored. Number of kills in
     *                           deathmatch.
     */
  constructor(
    teamId: string,
    won: boolean,
    roundsPlayed: number,
    roundsWon: number,
    numPoints: number
  ) {
    this.teamId = teamId;
    this.won = won;
    this.roundsPlayed = roundsPlayed;
    this.roundsWon = roundsWon;
    this.numPoints = numPoints;
  }
}
