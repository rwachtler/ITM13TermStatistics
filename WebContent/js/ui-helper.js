/**
 * Applies some changes to UI and some smooth stuff
 */

$(document).ready(function(){
    calcCenter();
});

$(window).resize(function(){
    calcCenter();
});

var calcCenter = function(){
    $('.next-section').css('left', (window.innerWidth / 2) - $('.next-section').width() / 2);
    $('.spinner').css('left', (window.innerWidth / 2) - $('.spinner').width() / 2);
    $('.spinner').css('top', (window.innerHeight / 2) - $('.spinner').height() / 2);
}


$(window).scroll(function () {
    if ($(this).scrollTop() > 400) {
        $('#top-scroll').show(500);
    } else {
        $('#top-scroll').hide(500);
    }
});

$('#top-scroll').click(function (e) {
    e.preventDefault();
    $("html, body").animate({
        scrollTop: 0
    }, 600);
});

$('.next-section').click(function (e) {
    e.preventDefault();
    var $targetOffset = $($(this).attr('href')).offset().top;
    $("html, body").animate({
        scrollTop: $targetOffset - 50
    }, 600);
});