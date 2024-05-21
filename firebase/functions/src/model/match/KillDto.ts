/**
 * Class that holds information about kill events
 */
class KillDto {
  timeSinceGameStartMillis: number;
  timeSinceRoundStartMillis: number;
  killer: string;
  victim: string;
  victimLocation: LocationDto;
  assistants: Array<string>;
  playerLocations: Array<PlayerLocationsDto>;
  finishingDamage: FinishingDamageDto;

  /**
     * Constructs a new KillDto object
     * @param {number} timeSinceGameStartMillis Time since game started in ms
     * @param {number} timeSinceRoundStartMillis Time since round started in ms
     * @param {string} killer PUUID of killer
     * @param {string} victim PUUID of victim
     * @param {LocationDto} victimLocation Location of victim
     * @param {Array<string>} assistants List of PUUIDs that assisted
     * @param {Array<PlayerLocationsDto>} playerLocations Locations of all living players
     * @param {FinishingDamageDto} finishingDamage Finishing damage
     */
  constructor(
    timeSinceGameStartMillis: number,
    timeSinceRoundStartMillis: number,
    killer: string,
    victim: string,
    victimLocation: LocationDto,
    assistants: Array<string>,
    playerLocations: Array<PlayerLocationsDto>,
    finishingDamage: FinishingDamageDto
  ) {
    this.timeSinceGameStartMillis = timeSinceGameStartMillis;
    this.timeSinceRoundStartMillis = timeSinceRoundStartMillis;
    this.killer = killer;
    this.victim = victim;
    this.victimLocation = victimLocation;
    this.assistants = assistants;
    this.playerLocations = playerLocations;
    this.finishingDamage = finishingDamage;
  }
}
