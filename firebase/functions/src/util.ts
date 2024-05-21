/**
 * Generates a random integer within a range including the min and max values
 * @param {number} min Minimum value of range
 * @param {number} max Maximum value of range
 * @return {number} Returns the generated int
 */
function randomIntInRange(min: number, max: number) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}
