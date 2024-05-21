/**
 * Class that holds location information
 */
class LocationDto {
  x: number;
  y: number;

  /**
     * Constructs a new LocationDto object
     * @param {number} x X Position
     * @param {number} y Y Position
     */
  constructor(
    x: number,
    y: number
  ) {
    this.x = x;
    this.y = y;
  }
}
