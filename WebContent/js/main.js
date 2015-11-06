$(document).ready(function() {
    $('.next').css('left',(window.innerWidth/2)-$('.next').width()/2);
});

$(window).scroll(function() {
   if($(this).scrollTop() > 400){
       $('#top-scroll').show(500);
   } else{
       $('#top-scroll').hide(500);
   }
});

$('#top-scroll').click(function(e){
   e.preventDefault();
    $("html, body").animate({scrollTop:0},600);
});

$('.next').click(function(e){
   e.preventDefault();
    var targetOffset = $($(this).attr('href')).offset().top;
    $("html, body").animate({scrollTop:targetOffset-50},600);
});