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