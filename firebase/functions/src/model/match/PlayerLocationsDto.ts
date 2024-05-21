/**
 * Class that holds location information about a player
 */
class PlayerLocationsDto {
  puuid: string;
  viewRadians: number;
  location: LocationDto;

  /**
     * Constructs a new PlayerLocationsDto object
     * @param {string} puuid PUUID of player
     * @param {number} viewRadians Looking angle in rad
     * @param {LocationDto} location Location of player
     */
  constructor(
    puuid: string,
    viewRadians: number,
    location: LocationDto
  ) {
    this.puuid = puuid;
    this.viewRadians = viewRadians;
    this.location = location;
  }
}
