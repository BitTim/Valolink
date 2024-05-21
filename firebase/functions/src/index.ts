import * as cloudFun from "firebase-functions";
import * as uuid from "uuid";

export const onRequestMatchHistory =
    cloudFun.https.onRequest((request, response) => {
      const puuid = request.params[0];
      const seasonId = request.params[1];
      const matchDto = generateMatchDto(puuid, seasonId);
      response.send(matchDto);
    });

/**
 * Generates a new MatchDto
 * @param {string} puuid PUUID
 * @param {string} seasonId Current season id
 * @return {MatchDto} Returns the generated MatchDto object
 */
function generateMatchDto(puuid: string, seasonId: string): MatchDto {
  const gameDurationMillis = randomIntInRange(
    MIN_GAME_LENGTH_MILLIS,
    MAX_GAME_LENGTH_MILLIS
  );
  const gameStartMillis = Date.now() - gameDurationMillis;

  const queue = QUEUES[Math.floor(Math.random() * QUEUES.length)];
  const isRanked = queue == QUEUES[1];

  return new MatchDto(
    new MatchInfoDto(
      uuid.v4(),
      MAPS[Math.floor(Math.random() * MAPS.length)],
      gameDurationMillis,
      gameStartMillis,
      PROVISIONING_FLOW_ID,
      true,
      "",
      queue,
      GAMEMODE,
      isRanked,
      seasonId
    ),
    [],
    [],
    [],
    []
  );
}
