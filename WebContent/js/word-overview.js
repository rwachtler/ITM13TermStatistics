$(document).ready(function(){
    
});

$(window).resize(function(){
    calcCenter();
});

var calcCenter = function(){
    $('.next-section').css('left', (window.innerWidth / 2) - $('.next-section').width() / 2);
}