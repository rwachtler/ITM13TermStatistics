/**
 *
 * @param arr - array with duplicate elements
 * @returns {Array} - array with unique elements
 */
var eliminateDuplicates = function(arr) {
    var i,
        len = arr.length,
        out = [],
        obj = {};

    for (i = 0; i < len; i++) {
        obj[arr[i]] = 0;
    }
    for (i in obj) {
        out.push(i);
    }
    return out;
}

/**
 * Calculates seconds for given milliseconds amount
 * @param milliSeconds
 * @returns seconds
 */
var toSeconds = function(milliSeconds){
    return milliSeconds/1000;
}

/**
 * Calculates minutes for given milliseconds amount
 * @param milliSeconds
 * @returns minutes
 */
var toMinutes = function(milliSeconds){
    return (milliSeconds/1000)/60;
}

/**
 * Rounds the given value (2 decimals)
 * @param x - given number to round
 * @returns rounded value
 */
var round2 = function(x){
    return Math.round(x*100)/100;
}